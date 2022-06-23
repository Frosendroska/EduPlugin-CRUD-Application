package org.hse.torrent;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.hse.torrent.io.RandomIOFile;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartRequest;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartResponse;
import org.hse.torrent.services.api.PeerToPeerServiceGrpc;
import org.hse.torrent.services.api.PeerToPeerServiceGrpc.PeerToPeerServiceBlockingStub;
import org.hse.torrent.state.FileState;
import org.hse.torrent.state.SeedState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class FileDownloader {

    private static final Duration FAILURE_SLEEP_DURATION = Duration.ofSeconds(10);

    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public void start(AtomicReference<FileState> fileStateReference) {
        threadPoolTaskScheduler.submit(new FileDownloaderImpl(fileStateReference)::downloadPart);
    }

    /* Runnable */
    private class FileDownloaderImpl {

        private final AtomicReference<FileState> fileStateReference;

        public FileDownloaderImpl(AtomicReference<FileState> fileStateReference) {
            this.fileStateReference = fileStateReference;
        }

        public void downloadPart() {
            // Берем сиды из FileState.seeds
            FileState state = fileStateReference.get();
            if (state.getPartsToDownload().isEmpty()) {
                System.out.printf("File %d at path %s was downloaded\n", state.getFileId(), state.getLocalPath());
                // File download is finished.
                return;
            }

            for (int partIndex : state.getPartsToDownload()) {
                for (SeedState seed : state.getSeeds()) {
                    if (!seed.getPartsAvailable().contains(partIndex)) {
                        // Продолжаем цикл при не успешной загрузке
                        continue;
                    }

                    /* Open channel to download one part */
                    ManagedChannel channel = null;
                    try {
                        channel = ManagedChannelBuilder.forAddress(seed.getIp(), Integer.parseInt(seed.getPort()))
                                .usePlaintext()
                                .build();
                        PeerToPeerServiceBlockingStub client = PeerToPeerServiceGrpc.newBlockingStub(channel);
                        GetFilePartResponse response = client.getFilePart(GetFilePartRequest.newBuilder()
                                .setFileId(state.getFileId())
                                .setPartIndex(partIndex)
                                .build());
                        new RandomIOFile(state.getLocalPath()).write(partIndex, response.getContent().toByteArray());
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    } finally {
                        /* Close channel */
                        if (channel != null) {
                            channel.shutdown();
                        }
                    }

                    FileState fileStateToUpdate = fileStateReference.getAcquire();
                    // Удаляем из кусочков для загрузки загруженный кусочек
                    fileStateToUpdate.downloadedPartRemoving(partIndex);
                    fileStateReference.setRelease(fileStateToUpdate);

                    // Перезапускаем эту функцию немедленно
                    threadPoolTaskScheduler.submit(this::downloadPart);
                    return;
                }
            }

            // Если успешной загрузки не случилось, перезапускаемся с задержкой
            threadPoolTaskScheduler
                    .getScheduledExecutor()
                    .schedule(this::downloadPart, FAILURE_SLEEP_DURATION.toSeconds(), TimeUnit.SECONDS);
        }
    }
}
