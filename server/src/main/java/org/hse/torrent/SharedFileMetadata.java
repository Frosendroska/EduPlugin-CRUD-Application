package org.hse.torrent;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class SharedFileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer fileId;

    @Column
    private String name;

    @Column
    private Long sizeBytes;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Seed> seeds = new HashSet<>();

    public SharedFileMetadata() {}

    public SharedFileMetadata(String name, Long sizeBytes) {
        this.name = name;
        this.sizeBytes = sizeBytes;
    }

    public Integer getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public Set<Seed> getSeeds() {
        return seeds;
    }

    public void addSeedInternal(Seed seed) {
        this.seeds.add(seed);
    }
}
