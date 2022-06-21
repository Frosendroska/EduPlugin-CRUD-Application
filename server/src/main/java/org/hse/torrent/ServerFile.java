package org.hse.torrent;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Map;

public class ServerFile {
    Integer id;
    String name;
    Integer size;
    Map<InetSocketAddress, LocalDateTime> seeders;
}
