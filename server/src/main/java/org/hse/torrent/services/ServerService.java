package org.hse.torrent.services;

import io.grpc.stub.StreamObserver;
import java.time.OffsetDateTime;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;
import org.hse.torrent.Seed;
import org.hse.torrent.SeedRepository;
import org.hse.torrent.SharedFileMetadata;
import org.hse.torrent.SharedFileMetadataRepository;
import org.hse.torrent.services.Server.SharedFileMetadataResponse;
import org.hse.torrent.services.Server.SourcesQueryResponse;
import org.hse.torrent.services.Server.sourcesQueryRequest;
import org.hse.torrent.services.Server.updateQueryRequest;
import org.hse.torrent.services.Server.uploadQueryRequest;
import org.hse.torrent.services.Server.uploadQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;

@GrpcService
public class ServerService extends ServerServiceGrpc.ServerServiceImplBase {

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private SharedFileMetadataRepository sharedFileMetadataRepository;

    /**
     * Get all Files in torrent
     */
    @Override
    public void listQuery(
            com.google.protobuf.Empty _request,
            StreamObserver<SharedFileMetadataResponse> responseObserver) {
        List<SharedFileMetadata> accessibleFiles = sharedFileMetadataRepository.findAll();
        accessibleFiles.forEach(
                file -> responseObserver.onNext(SharedFileMetadataResponse.newBuilder()
                        .setFileId(file.getFileId())
                        .setName(file.getName())
                        .setSize(file.getSize())
                        .build())
        );
        responseObserver.onCompleted();
    }

    /**
     * Upload new file
     */
    @Override
    public void uploadQuery(
            uploadQueryRequest request,
            StreamObserver<uploadQueryResponse> responseObserver) {

        SharedFileMetadata file = new SharedFileMetadata(request.getName(), request.getSize());
        sharedFileMetadataRepository.save(file);

        responseObserver.onNext(uploadQueryResponse.newBuilder().setFileId(file.getFileId()).build());
        responseObserver.onCompleted();
    }

    /**
     * Get file seeds
     */
    @Override
    public void sourcesQuery(
            sourcesQueryRequest request,
            StreamObserver<SourcesQueryResponse> responseObserver) {
        SharedFileMetadata file = sharedFileMetadataRepository.getById(request.getFileId());
        OffsetDateTime now = OffsetDateTime.now();
        file.getSeeders().stream().filter(it -> it.isAlive(now)).forEach(
                seed -> responseObserver.onNext(SourcesQueryResponse.newBuilder()
                        .setIp(seed.getIp())
                        .setClientPort(seed.getPort())
                        .build())
        );
        responseObserver.onCompleted();
    }

    /**
     * Seed report
     */
    @Override
    public void updateQuery(
            updateQueryRequest request,
            StreamObserver<com.google.protobuf.Empty> responseObserver) {
        String id = String.format("%s:%s", request.getIp(), request.getClientPort());
        Seed seed;
        try {
            seed = seedRepository.getById(id);
        } catch (ObjectRetrievalFailureException _e) {
            seed = new Seed(id, OffsetDateTime.now(), request.getClientPort(), request.getIp());
        }

        List<SharedFileMetadata> files = request.getFileIdList().stream().map(SharedFileMetadata::new).toList();

        seed.setFiles(files);
        seedRepository.save(seed);
        responseObserver.onCompleted();
    }
}
