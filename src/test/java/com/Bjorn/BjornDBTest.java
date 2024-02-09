package com.Bjorn;

import com.Bjorn.BjornDB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.Bjorn.Util.deleteFolderContentsIfExists;
import static org.junit.jupiter.api.Assertions.*;

public class BjornDBTest {

    private static final String TEST_FOLDER = "src/test/resources/test_folder_simple";
    private static final String TEST_LOG_FILE_1 = "1.log";
    private static final String TEST_LOG_FILE_2 = "2.log";
    private static final String TEST_LOG_FILE_3 = "3.log";

    private BjornDB bjornDB;

    @BeforeEach
    void setUp() throws IOException {
        deleteFolderContentsIfExists(TEST_FOLDER);
        // Create a test folder and log files
        Files.createDirectories(Paths.get(TEST_FOLDER));
        Files.createFile(Paths.get(TEST_FOLDER, TEST_LOG_FILE_1));
        Files.createFile(Paths.get(TEST_FOLDER, TEST_LOG_FILE_2));
        Files.createFile(Paths.get(TEST_FOLDER, TEST_LOG_FILE_3));

        bjornDB = BjornDB.getInstance(TEST_FOLDER);
    }

    @AfterEach
    void tearDown() throws IOException {
        bjornDB.stop();
        deleteFolderContentsIfExists(TEST_FOLDER);
    }

    @Test
    void givenFolderPath_whenStarted_thenInstanceCreatedAndMarkedAsStarted() throws Exception {
        // Given
        // A FireflyDB instance with a folder path

        // When
        bjornDB.start();

        // Then
        assertNotNull(bjornDB);
        assertEquals(TEST_FOLDER, bjornDB.getFolderPath());
        assertTrue(bjornDB.isStarted());
    }

    @Test
    void givenStartedInstance_whenStop_thenLogsClosed() throws Exception {
        // Given
        // A started FireflyDB instance
        bjornDB.start();
        assertTrue(bjornDB.isStarted());

        // When
        bjornDB.stop();

        // Then
        assertFalse(bjornDB.isStarted());
    }

    @Test
    void givenStartedInstance_whenSetAndGet_thenValuesAreCorrect() throws Exception {

        // Given
        bjornDB.start();
        assertTrue(bjornDB.isStarted());

        // Set a value
        byte[] key = "testKey".getBytes();
        byte[] value = "testValue".getBytes();
        bjornDB.set(key, value);

        // Get the value
        byte[] retrievedValue = bjornDB.get(key);
        assertArrayEquals(value, retrievedValue);
    }

    @Test
    void givenUnstartedInstance_whenSet_thenExceptionThrown() {
        // Given
        byte[] key = "testKey".getBytes();
        byte[] value = "testValue".getBytes();

        // When/Then
        // Attempt to set a value without starting the instance
        assertThrows(IllegalStateException.class, () -> bjornDB.set(key, value));
    }

    @Test
    void givenUnstartedInstance_whenGet_thenExceptionThrown() {
        // Given
        byte[] key = "testKey".getBytes();

        // When/Then
        // Attempt to get a value without starting the instance
        assertThrows(IllegalStateException.class, () -> bjornDB.get(key));
    }

    @Test
    void givenNonexistentKey_whenGet_thenExceptionThrown() throws Exception {
        // Given
        bjornDB.start();
        assertTrue(bjornDB.isStarted());
        byte[] key = "nonexistentKey".getBytes();

        // When/Then
        // Attempt to get a nonexistent key
        assertThrows(IllegalArgumentException.class, () -> bjornDB.get(key));
    }

    @Test
    void givenStartedInstance_whenSetMultipleTimes_thenValuesAreCorrect() throws Exception {
        // Given
        bjornDB.start();
        assertTrue(bjornDB.isStarted());

        // Set a value
        byte[] key = "testKey".getBytes();
        byte[] value = "testValue".getBytes();
        bjornDB.set(key, value);

        // Set another value
        byte[] key2 = "testKey2".getBytes();
        byte[] value2 = "testValue2".getBytes();
        bjornDB.set(key2, value2);

        // Get the values
        byte[] retrievedValue = bjornDB.get(key);
        byte[] retrievedValue2 = bjornDB.get(key2);
        assertArrayEquals(value, retrievedValue);
        assertArrayEquals(value2, retrievedValue2);
    }

    @Test
    void givenStartedInstance_whenSetSameKeyMultipleTimes_thenValueIsCorrect() throws Exception {
        // Given
        bjornDB.start();
        assertTrue(bjornDB.isStarted());

        // When
        // Set a value
        byte[] key = "testKey".getBytes();
        byte[] value = "testValue".getBytes();
        bjornDB.set(key, value);

        // Set another value
        byte[] value2 = "testValue2".getBytes();
        bjornDB.set(key, value2);

        // Get the values
        byte[] retrievedValue = bjornDB.get(key);
        assertArrayEquals(value2, retrievedValue);
    }

    @Test
    void givenStartedInstance_whenSetAndRestart_thenValueIsCorrect() throws Exception {
        // Given
        bjornDB.start();
        assertTrue(bjornDB.isStarted());
        byte[] key = "testKey".getBytes();
        byte[] value = "testValue".getBytes();
        bjornDB.set(key, value);
        bjornDB.stop();

        // When
        // Restart the instance
        bjornDB = BjornDB.getInstance(TEST_FOLDER);
        bjornDB.start();

        // Get the values
        byte[] retrievedValue = bjornDB.get(key);
        assertArrayEquals(value, retrievedValue);
    }
}
