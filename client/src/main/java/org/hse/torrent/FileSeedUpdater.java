package org.hse.torrent;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import net.devh.boot.grpc.client.inject.GrpcClient;
import one.util.streamex.StreamEx;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartitioningInfoRequest;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartitioningInfoResponse;
import org.hse.torrent.services.api.PeerToPeerServiceGrpc;
import org.hse.torrent.services.api.PeerToPeerServiceGrpc.PeerToPeerServiceBlockingStub;
import org.hse.torrent.services.api.Server.GetSharedFileSeedsRequest;
import org.hse.torrent.services.api.ServerServiceGrpc.ServerServiceBlockingStub;
import org.hse.torrent.state.FileState;
import org.hse.torrent.state.SeedState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class FileSeedUpdater {

    private static final Duration UPDATE_INTERVAL = Duration.ofMinutes(1);

    @GrpcClient("server")
    private ServerServiceBlockingStub serverService;

    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public void start(AtomicReference<FileState> fileStateReference) {
        threadPoolTaskScheduler.submit(new FileSeedUpdaterImpl(fileStateReference)::updateFileSeeds);
    }

    /* Runnable */
    private class FileSeedUpdaterImpl {

        private final AtomicReference<FileState> fileStateReference;

        public FileSeedUpdaterImpl(AtomicReference<FileState> fileStateReference) {
            this.fileStateReference = fileStateReference;
        }

        void updateFileSeeds() {
            FileState state = fileStateReference.get();
            if (state.getPartsToDownload().isEmpty()) {
                // File download is finished.
                return;
            }

            // Получаем список с информацией о всех сидах файла
            List<SeedState> seeds = StreamEx.of(serverService.getSharedFileSeeds(GetSharedFileSeedsRequest.newBuilder()
                            .setFileId(state.getFileId())
                            .build()))
                    .map(it -> loadSeedState(it.getIp(), it.getPort(), state.getFileId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toImmutableList();

            // Полученную о сидах информацию кладем в FileState.seeds
            FileState fileStateToUpdate = fileStateReference.getAcquire();
            fileStateToUpdate.setSeeds(seeds);
            fileStateReference.setRelease(fileStateToUpdate);

            // Повторяем запуск через несколько секунд
            threadPoolTaskScheduler
                    .getScheduledExecutor()
                    .schedule(this::updateFileSeeds, UPDATE_INTERVAL.toSeconds(), TimeUnit.SECONDS);
        }
    }

    private Optional<SeedState> loadSeedState(String ip, String port, int fileId) {
        /* Open channel to download one part */
        ManagedChannel channel = null;
        try {
            channel = ManagedChannelBuilder.forAddress(ip, Integer.parseInt(port))
                    .usePlaintext()
                    .build();
            PeerToPeerServiceBlockingStub client = PeerToPeerServiceGrpc.newBlockingStub(channel);
            GetFilePartitioningInfoResponse response =
                    client.getFilePartitioningInfo(GetFilePartitioningInfoRequest.newBuilder()
                            .setFileId(fileId)
                            .build());
            return Optional.of(
                    new SeedState(ip, port, StreamEx.of(response.getPartList()).toImmutableSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            /* Close channel */
            if (channel != null) {
                channel.shutdown();
            }
        }
    }
}
