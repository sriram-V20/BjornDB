package com.Bjorn.table;

import java.io.FileNotFoundException;

public interface Table {

    void put(byte[] key, Pointer value);

    Pointer get(byte[] key);

    void saveToDisk(String filePath) throws FileNotFoundException;
}
