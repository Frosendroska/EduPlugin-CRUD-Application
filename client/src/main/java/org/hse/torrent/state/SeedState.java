package org.hse.torrent.state;

import java.util.Set;

public final class SeedState {

    private String ip;

    private String port;

    private Set<Integer> partsAvailable;

    public SeedState() {}

    public SeedState(String ip, String port, Set<Integer> partsAvailable) {
        this.ip = ip;
        this.port = port;
        this.partsAvailable = partsAvailable;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public Set<Integer> getPartsAvailable() {
        return partsAvailable;
    }
}
