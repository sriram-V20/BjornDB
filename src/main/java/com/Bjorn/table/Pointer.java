package com.Bjorn.table;

import java.util.Objects;

public class Pointer {
    private String fileName;
    private long offset;

    public Pointer(String fileName, long offset) {
        this.fileName = fileName;
        this.offset = offset;
    }

    public Pointer() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pointer that = (Pointer) o;
        return offset == that.offset && fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, offset);
    }
}
