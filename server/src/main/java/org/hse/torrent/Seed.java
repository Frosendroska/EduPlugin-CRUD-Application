package org.hse.torrent;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "seeds")
public class Seed {

    private static final Duration SEED_TTL = Duration.ofMinutes(5);

    @Id
    String seedId;

    @Column
    OffsetDateTime lastUpdated;

    @Column
    String ip;

    @Column
    String port;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "seeds")
    Set<SharedFileMetadata> files = new HashSet<>();

    public Seed(String seedId, OffsetDateTime lastUpdated, String ip, String port) {
        this.seedId = seedId;
        this.lastUpdated = lastUpdated;
        this.ip = ip;
        this.port = port;
    }

    public Seed() {}

    public boolean isAlive(OffsetDateTime now) {
        return lastUpdated.toInstant().plus(SEED_TTL).isAfter(now.toInstant());
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

    public Set<SharedFileMetadata> getFiles() {
        return files;
    }

    public String getSeedId() {
        return seedId;
    }

    public void setLastUpdated(OffsetDateTime now) {
        this.lastUpdated = now;
    }

    public void addFile(SharedFileMetadata file) {
        this.files.add(file);
        file.addSeedInternal(this);
    }
}
