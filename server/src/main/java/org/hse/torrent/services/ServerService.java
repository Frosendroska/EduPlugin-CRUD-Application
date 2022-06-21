package org.hse.torrent.services;

import io.grpc.stub.StreamObserver;
import org.hse.torrent.services.Server.listQueryRequest;
import org.hse.torrent.services.Server.listQueryResponse;
import org.hse.torrent.services.Server.sourcesQueryRequest;
import org.hse.torrent.services.Server.sourcesQueryResponse;
import org.hse.torrent.services.Server.updateQueryRequest;
import org.hse.torrent.services.Server.updateQueryResponse;
import org.hse.torrent.services.Server.uploadQueryRequest;
import org.hse.torrent.services.Server.uploadQueryResponse;

public class ServerService extends ServerServiceGrpc.ServerServiceImplBase {

    @Override
    public void listQuery(
            listQueryRequest request,
            StreamObserver<listQueryResponse> responseObserver) {
    }

    @Override
    public void sourcesQuery(
            sourcesQueryRequest request,
            StreamObserver<sourcesQueryResponse> responseObserver) {
    }

    @Override
    public void updateQuery(
            updateQueryRequest request,
            StreamObserver<updateQueryResponse> responseObserver) {
    }

    @Override
    public void uploadQuery(
            uploadQueryRequest request,
            StreamObserver<uploadQueryResponse> responseObserver) {
    }
}
