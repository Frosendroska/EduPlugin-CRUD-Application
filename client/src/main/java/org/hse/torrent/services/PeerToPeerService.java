package org.hse.torrent.services;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.hse.torrent.io.RandomIOFile;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartRequest;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartResponse;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartitioningInfoRequest;
import org.hse.torrent.services.api.PeerToPeer.GetFilePartitioningInfoResponse;
import org.hse.torrent.services.api.PeerToPeerServiceGrpc;
import org.hse.torrent.state.FileState;
import org.hse.torrent.state.State;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class PeerToPeerService extends PeerToPeerServiceGrpc.PeerToPeerServiceImplBase {

    @Autowired
    State state;

    @Override
    public void getFilePartitioningInfo(
            GetFilePartitioningInfoRequest request, StreamObserver<GetFilePartitioningInfoResponse> responseObserver) {

        FileState fileState = state.getFileState(request.getFileId()).orElseThrow();
        responseObserver.onNext(GetFilePartitioningInfoResponse.newBuilder()
                .addAllPart(FileState.buildAvailableParts(fileState))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getFilePart(GetFilePartRequest request, StreamObserver<GetFilePartResponse> responseObserver) {

        FileState fileState = state.getFileState(request.getFileId()).orElseThrow();
        responseObserver.onNext(GetFilePartResponse.newBuilder()
                .setContent(
                        ByteString.copyFrom(new RandomIOFile(fileState.getLocalPath()).read(request.getPartIndex())))
                .build());
        responseObserver.onCompleted();
    }
}
