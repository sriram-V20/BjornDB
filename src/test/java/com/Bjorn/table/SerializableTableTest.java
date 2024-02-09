package com.Bjorn.table;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SerializableTableTest {
    private static final String TEST_FILE_PATH = "src/test/resources/map";
    private SerializableTable fileTable;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        fileTable = SerializableTable.fromEmpty();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @Test
    void given_KeyValue_When_PuttingAndGet_Then_RetrievedValueMatches() {
        // Given
        byte[] key = "testKey".getBytes();
        Pointer expectedValue = new Pointer("test.txt", 42);

        // When
        fileTable.put(key, new Pointer("test.txt", 42));
        Pointer retrievedValue = fileTable.get(key);

        // Then
        assertEquals(expectedValue, retrievedValue);
    }

    @Test
    void given_NullKey_When_PuttingAndGet_Then_RetrievedValueIsNull() {
        // Given
        Pointer value = new Pointer("test.txt", 42);

        // When
        fileTable.put(null, value);
        Pointer retrievedValue = fileTable.get(null);

        // Then
        assertNull(retrievedValue);
    }

    @Test
    void given_NullValue_When_PuttingAndGet_Then_RetrievedValueIsNull() {
        // Given
        byte[] key = "testKey".getBytes();

        // When
        fileTable.put(key, null);
        Pointer retrievedValue = fileTable.get(key);

        // Then
        assertNull(retrievedValue);
    }

    @Test
    void given_KeyValue_When_SavingToDiskAndLoadingFromFile_Then_RetrievedValueMatches() throws Exception {
        // Given
        byte[] key = "testKey".getBytes();
        Pointer value = new Pointer("test.txt", 42);

        // When
        fileTable.put(key, value);
        fileTable.saveToDisk(TEST_FILE_PATH);
        SerializableTable loadedFileTable = SerializableTable.fromFile(TEST_FILE_PATH);
        Pointer retrievedValue = loadedFileTable.get(key);

        // Then
        assertEquals(value, retrievedValue);
    }

    @Test
    void given_NonexistentFile_When_LoadingFromFile_Then_ExceptionIsThrown() {
        // When
        // Then
        assertThrows(Exception.class,
                () -> SerializableTable.fromFile(TEST_FILE_PATH));
    }

    @Test
    void given_CorruptedFile_When_LoadingFromFile_Then_ExceptionIsThrown() throws IOException {
        // Given
        // Create a corrupted file by writing invalid data
        Path filePath = Paths.get(TEST_FILE_PATH);
        List<String> list = new ArrayList<>();
        list.add("Invalid Data");
        Files.write(filePath, list);

        // Then
        assertThrows(Exception.class,
                () -> SerializableTable.fromFile(TEST_FILE_PATH));
    }

}
