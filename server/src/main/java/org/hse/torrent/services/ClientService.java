package org.hse.torrent.services;

import io.grpc.stub.StreamObserver;
import org.hse.torrent.services.Client.getQueryRequest;
import org.hse.torrent.services.Client.getQueryResponse;
import org.hse.torrent.services.Client.statQueryRequest;
import org.hse.torrent.services.Client.statQueryResponse;

public class ClientService extends ClientServiceGrpc.ClientServiceImplBase {
    @Override
    public void statQuery(statQueryRequest request, StreamObserver<statQueryResponse> responseObserver) {}

    @Override
    public void getQuery(getQueryRequest request, StreamObserver<getQueryResponse> responseObserver) {}
}
