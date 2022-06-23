package org.hse.torrent.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hse.torrent.io.RandomIOFile;

public final class FileState {

    private Integer fileId;

    private String localPath;

    private String name;

    private long sizeBytes;

    private Set<Integer> partsToDownload;

    private List<SeedState> seeds;

    public FileState() {}

    private FileState(
            Integer fileId,
            String localPath,
            String name,
            long sizeBytes,
            Set<Integer> partsToDownload,
            List<SeedState> seeds) {
        this.fileId = fileId;
        this.localPath = localPath;
        this.name = name;
        this.sizeBytes = sizeBytes;
        this.partsToDownload = partsToDownload;
        this.seeds = seeds;
    }

    public static FileState locallyCreatedFile(String localPath) {
        return new FileState(
                null,
                localPath,
                Paths.get(localPath).getFileName().toString(),
                new RandomIOFile(localPath).getSize(),
                ImmutableSet.of(),
                ImmutableList.of());
    }

    public static FileState downloadingFile(int fileId, String localPath, String name, long sizeBytes) {
        return new FileState(fileId, localPath, name, sizeBytes, buildPartsToDownload(sizeBytes), ImmutableList.of());
    }

    public FileState copyWithoutSeeds() {
        return new FileState(
                fileId, localPath, name, sizeBytes, Sets.newConcurrentHashSet(partsToDownload), ImmutableList.of());
    }

    public Integer getFileId() {
        return fileId;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getName() {
        return name;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public Set<Integer> getPartsToDownload() {
        return partsToDownload;
    }

    public List<SeedState> getSeeds() {
        return seeds;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public void setSeeds(List<SeedState> seeds) {
        this.seeds = seeds;
    }

    public void downloadedPartRemoving(int partId) {
        this.partsToDownload.remove(partId);
    }

    public static int getSizeParts(long sizeBytes) {
        return (int) ((sizeBytes + RandomIOFile.PART_SIZE_BYTES - 1) / RandomIOFile.PART_SIZE_BYTES);
    }

    /**
     * Выдаем куски, которых нет в partsToDownload
     */
    public static Set<Integer> buildAvailableParts(FileState state) {
        Set<Integer> ret = new HashSet<>();
        for (int i = 0; i != getSizeParts(state.getSizeBytes()); i++) {
            if (!state.getPartsToDownload().contains(i)) {
                ret.add(i);
            }
        }
        return ret;
    }

    private static Set<Integer> buildPartsToDownload(long sizeBytes) {
        Set<Integer> ret = Sets.newConcurrentHashSet();
        for (int i = 0; i != getSizeParts(sizeBytes); i++) {
            ret.add(i);
        }
        return ret;
    }
}
