package org.hse.torrent;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class SharedFileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer fileId;

    @Column
    private String name;

    @Column
    private Long size;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Seed> seeders = new ArrayList<>();

    public SharedFileMetadata() {}

    public SharedFileMetadata(Integer fileId) {
        this.fileId = fileId;
    }

    public SharedFileMetadata(String name, Long size) {
        this.name = name;
        this.size = size;
    }

    public Integer getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public Long getSize() {
        return size;
    }

    public List<Seed> getSeeders() {
        return seeders;
    }
}
