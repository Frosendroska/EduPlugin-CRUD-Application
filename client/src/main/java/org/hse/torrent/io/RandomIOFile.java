package org.hse.torrent.io;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomIOFile {

    public static int PART_SIZE_BYTES = 10 * 1024;

    private final RandomAccessFile file;

    public RandomIOFile(String path) {
        try {
            this.file = new RandomAccessFile(path, "rw");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long getSize() {
        try {
            return file.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSize(long byteSize) {
        try {
            file.setLength(byteSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] read(int partIndex) {
        long offset = (long) PART_SIZE_BYTES * partIndex;
        if (offset >= getSize()) {
            throw new IllegalArgumentException();
        }

        int bytesToRead = (int) Math.min(getSize() - offset, PART_SIZE_BYTES);
        try {
            byte[] out = new byte[bytesToRead];
            file.seek(offset);
            file.read(out);
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(int partIndex, byte[] data) {
        try {
            file.seek((long) PART_SIZE_BYTES * partIndex);
            file.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
