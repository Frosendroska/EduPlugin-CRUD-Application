package org.hse.torrent.services;

import com.google.common.collect.Sets;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.hse.torrent.Seed;
import org.hse.torrent.SeedRepository;
import org.hse.torrent.SharedFileMetadata;
import org.hse.torrent.SharedFileMetadataRepository;
import org.hse.torrent.services.api.Server.GetSharedFileSeedsRequest;
import org.hse.torrent.services.api.Server.RegisterSharedFileRequest;
import org.hse.torrent.services.api.Server.RegisterSharedFileResponse;
import org.hse.torrent.services.api.Server.ReportSeedRequest;
import org.hse.torrent.services.api.Server.SharedFileMetadataResponse;
import org.hse.torrent.services.api.Server.SharedFileSeedResponse;
import org.hse.torrent.services.api.ServerServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ServerService extends ServerServiceGrpc.ServerServiceImplBase {

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private SharedFileMetadataRepository sharedFileMetadataRepository;

    @Override
    public void getAllSharedFileMetadata(Empty request, StreamObserver<SharedFileMetadataResponse> responseObserver) {
        List<SharedFileMetadata> accessibleFiles = sharedFileMetadataRepository.findAll();
        accessibleFiles.forEach(file -> responseObserver.onNext(SharedFileMetadataResponse.newBuilder()
                .setFileId(file.getFileId())
                .setName(file.getName())
                .setSizeBytes(file.getSizeBytes())
                .build()));
        responseObserver.onCompleted();
    }

    @Override
    public void getSharedFileSeeds(
            GetSharedFileSeedsRequest request, StreamObserver<SharedFileSeedResponse> responseObserver) {
        SharedFileMetadata file = sharedFileMetadataRepository.getById(request.getFileId());
        OffsetDateTime now = OffsetDateTime.now();
        file.getSeeds().stream()
                .filter(it -> it.isAlive(now))
                .forEach(seed -> responseObserver.onNext(SharedFileSeedResponse.newBuilder()
                        .setIp(seed.getIp())
                        .setPort(seed.getPort())
                        .build()));
        responseObserver.onCompleted();
    }

    @Override
    public void registerSharedFile(
            RegisterSharedFileRequest request, StreamObserver<RegisterSharedFileResponse> responseObserver) {
        SharedFileMetadata file = new SharedFileMetadata(request.getName(), request.getSizeBytes());
        sharedFileMetadataRepository.save(file);

        responseObserver.onNext(RegisterSharedFileResponse.newBuilder()
                .setFileId(file.getFileId())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void reportSeed(ReportSeedRequest request, StreamObserver<Empty> responseObserver) {
        Seed seed = getOrCreateSeed(request.getIp(), request.getPort());

        Set<Integer> requestedFileIds = request.getFileIdsList().stream().collect(Collectors.toSet());
        Set<SharedFileMetadata> files = sharedFileMetadataRepository.findAllById(request.getFileIdsList()).stream()
                .collect(Collectors.toSet());
        Set<Integer> missingFileIds = Sets.difference(
                requestedFileIds,
                files.stream().map(SharedFileMetadata::getFileId).collect(Collectors.toUnmodifiableSet()));
        if (!missingFileIds.isEmpty()) {
            throw new IllegalArgumentException(String.format("Missing files: %s", missingFileIds));
        }

        files.forEach(seed::addFile);
        seedRepository.save(seed);

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    private Seed getOrCreateSeed(String ip, String port) {
        String id = String.format("%s:%s", ip, port);
        Optional<Seed> seed = seedRepository.findById(id);
        if (seed.isEmpty()) {
            return new Seed(id, OffsetDateTime.now(), ip, port);
        }
        return seed.get();
    }
}
