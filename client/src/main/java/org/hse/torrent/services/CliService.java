package org.hse.torrent.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import one.util.streamex.StreamEx;
import org.hse.torrent.FileDownloader;
import org.hse.torrent.FileSeedUpdater;
import org.hse.torrent.io.RandomIOFile;
import org.hse.torrent.services.api.Cli.DownloadFileRequest;
import org.hse.torrent.services.api.Cli.ListFileResponse;
import org.hse.torrent.services.api.Cli.UploadFileRequest;
import org.hse.torrent.services.api.Cli.UploadFileResponse;
import org.hse.torrent.services.api.CliServiceGrpc;
import org.hse.torrent.services.api.Server.RegisterSharedFileRequest;
import org.hse.torrent.services.api.Server.RegisterSharedFileResponse;
import org.hse.torrent.services.api.Server.SharedFileMetadataResponse;
import org.hse.torrent.services.api.ServerServiceGrpc.ServerServiceBlockingStub;
import org.hse.torrent.state.FileState;
import org.hse.torrent.state.State;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class CliService extends CliServiceGrpc.CliServiceImplBase {

    @GrpcClient("server")
    private ServerServiceBlockingStub serverService;

    @Autowired
    private State state;

    @Autowired
    private FileSeedUpdater fileSeedUpdater;

    @Autowired
    private FileDownloader fileDownloader;

    @Override
    public void listSharedFiles(Empty request, StreamObserver<ListFileResponse> responseObserver) {
        serverService
                .getAllSharedFileMetadata(Empty.newBuilder().build())
                .forEachRemaining(it -> responseObserver.onNext(ListFileResponse.newBuilder()
                        .setFileId(it.getFileId())
                        .setName(it.getName())
                        .setSizeBytes(it.getSizeBytes())
                        .build()));
        responseObserver.onCompleted();
    }

    @Override
    public void uploadFile(UploadFileRequest request, StreamObserver<UploadFileResponse> responseObserver) {
        File file = Path.of(request.getLocalPath()).toFile();
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File does not exist");
        }

        FileState fileState = FileState.locallyCreatedFile(request.getLocalPath());
        RegisterSharedFileResponse response = serverService.registerSharedFile(RegisterSharedFileRequest.newBuilder()
                .setName(fileState.getName())
                .setSizeBytes(fileState.getSizeBytes())
                .build());
        fileState.setFileId(response.getFileId());
        state.setFileState(fileState);

        responseObserver.onNext(
                UploadFileResponse.newBuilder().setFileId(fileState.getFileId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void downloadFile(DownloadFileRequest request, StreamObserver<Empty> responseObserver) {
        File file = Path.of(request.getLocalPath()).toFile();
        if (file.exists()) {
            throw new IllegalArgumentException("Destination file already exists");
        }

        if (state.getFileState(request.getFileId()).isPresent()) {
            throw new IllegalArgumentException("File is already present");
        }

        SharedFileMetadataResponse fileMetadataResponse = StreamEx.of(serverService.getAllSharedFileMetadata(
                        Empty.newBuilder().build()))
                .findAny(it -> it.getFileId() == request.getFileId())
                .orElseThrow();

        FileState fileState = FileState.downloadingFile(
                fileMetadataResponse.getFileId(),
                request.getLocalPath(),
                fileMetadataResponse.getName(),
                fileMetadataResponse.getSizeBytes());
        AtomicReference<FileState> fileStateReference = state.setFileState(fileState);

        new RandomIOFile(fileState.getLocalPath()).setSize(fileState.getSizeBytes());

        // Start 2 threads for downloading
        fileSeedUpdater.start(fileStateReference);
        fileDownloader.start(fileStateReference);

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
