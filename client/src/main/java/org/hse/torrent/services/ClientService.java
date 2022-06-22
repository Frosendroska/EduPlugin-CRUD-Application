package org.hse.torrent.services;

import io.grpc.stub.StreamObserver;
import org.hse.torrent.services.Client.GetFilePartRequest;
import org.hse.torrent.services.Client.GetFilePartResponse;
import org.hse.torrent.services.Client.GetFilePartitioningInfoRequest;
import org.hse.torrent.services.Client.GetFilePartitioningInfoResponse;

public class ClientService extends ClientServiceGrpc.ClientServiceImplBase {
    @Override
    public void getFilePartitioningInfo(GetFilePartitioningInfoRequest request, StreamObserver<GetFilePartitioningInfoResponse> responseObserver) {
    }

    @Override
    public void getFilePart(GetFilePartRequest request, StreamObserver<GetFilePartResponse> responseObserver) {
    }
}
