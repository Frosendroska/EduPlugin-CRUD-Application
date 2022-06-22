package org.hse.torrent.services;

import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.hse.torrent.SharedFileMetadata;
import org.hse.torrent.services.Server.SharedFileMetadataResponse;
import org.hse.torrent.services.Server.uploadQueryRequest;
import org.hse.torrent.services.Server.uploadQueryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.yaml.snakeyaml.reader.StreamReader;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
public class ServerServiceTest {

    @Autowired
    ServerService serverService;

    @Test
    public void listQueryTest() {
//        uploadQueryRequest request =  createUploadQueryRequest("book.txt", 1000L);
//        StreamObserver<SharedFileMetadataResponse> responseObserver = new StreamObserver<SharedFileMetadata>();
//        serverService.getAllSharedFileMetadata(Empty.newBuilder().build(), responseObserver);

    }


    private uploadQueryRequest createUploadQueryRequest(String name, Long size) {
        return uploadQueryRequest.newBuilder().setName(name).setSize(size).build();
    }

}
