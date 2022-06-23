package org.hse.torrent.state;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public final class State {

    private final ConcurrentMap<Integer, AtomicReference<FileState>> files = new ConcurrentHashMap<>();

    public Optional<FileState> getFileState(int fileId) {
        return getFileStateReference(fileId).map(AtomicReference::get);
    }

    public Optional<AtomicReference<FileState>> getFileStateReference(int fileId) {
        return Optional.ofNullable(files.get(fileId));
    }

    public AtomicReference<FileState> setFileState(FileState fileState) {
        int fileId = fileState.getFileId();
        AtomicReference<FileState> reference;
        if (files.containsKey(fileId)) {
            reference = files.get(fileId);
            reference.set(fileState);
        } else {
            reference = new AtomicReference<>(fileState);
            files.put(fileId, new AtomicReference<>(fileState));
        }
        return reference;
    }

    public Set<Integer> getAllFileIds() {
        return files.keySet();
    }

    public List<FileState> getSnapshot() {
        return files.values().stream().map(it -> it.get().copyWithoutSeeds()).toList();
    }

    public void loadSnapshot(List<FileState> snapshot) {
        if (!files.isEmpty()) {
            throw new IllegalStateException("loadSnapshot is only run on start up");
        }
        snapshot.forEach(this::setFileState);
    }
}
