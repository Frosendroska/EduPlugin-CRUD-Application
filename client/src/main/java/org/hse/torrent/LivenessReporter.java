package org.hse.torrent;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.hse.torrent.services.api.Server.ReportSeedRequest;
import org.hse.torrent.services.api.ServerServiceGrpc.ServerServiceBlockingStub;
import org.hse.torrent.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LivenessReporter {

    @Value("${grpc.server.port}")
    String port;

    @Autowired
    State state;

    @GrpcClient("server")
    private ServerServiceBlockingStub serverService;

    /**
     * Загружаем данные о раздаваемых файлах
     */
    public void reportSeeds() {
        try {
            ReportSeedRequest reportSeedRequest = ReportSeedRequest.newBuilder()
                    .setIp(getPublicIp())
                    .setPort(port)
                    .addAllFileIds(state.getAllFileIds())
                    .build();
            /* empty return */
            serverService.reportSeed(reportSeedRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
    private static String getPublicIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
