package org.hse.torrent;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "seed")
public class Seed {
    @Id
    String seedId;

    @Column
    OffsetDateTime lastUpdated;

    @Column
    String port;

    @Column
    String ip;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "seeders")
    List<SharedFileMetadata> files = new ArrayList<>();

    public Seed(String seedId, OffsetDateTime lastUpdated, String port, String ip) {
        this.seedId = seedId;
        this.lastUpdated = lastUpdated;
        this.port = port;
        this.ip = ip;
    }

    public Seed() {

    }

    public boolean isAlive(OffsetDateTime now) {
        return lastUpdated.toInstant().plus(Duration.ofMinutes(5)).isAfter(now.toInstant());
    }

    public String getIp() {
        return ip;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getPort() {
        return port;
    }

    public List<SharedFileMetadata> getFiles() {
        return files;
    }

    public void setFiles(List<SharedFileMetadata> files) {
        this.files = files;
    }
}
