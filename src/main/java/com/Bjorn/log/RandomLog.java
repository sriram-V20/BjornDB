package com.Bjorn.log;

import com.Bjorn.model.Segment;
import com.Bjorn.table.Pointer;

import java.io.IOException;

public interface RandomLog {
    long size() throws IOException;

    String getFilePath();

    Pointer append(byte[] message) throws IOException;

    byte[] read(long offset, long length) throws Exception;

    Segment readSegment(long offset) throws Exception;

    void close() throws IOException;

    Integer getLogId();
}
