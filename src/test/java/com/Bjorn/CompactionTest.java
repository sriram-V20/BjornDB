package com.Bjorn;

import com.Bjorn.log.FileRandomLog;
import com.Bjorn.log.RandomLog;
import com.Bjorn.model.Segment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.Bjorn.Util.deleteFolderContentsIfExists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompactionTest {

    private static final String TEST_FOLDER = "src/test/resources/test_folder_compaction";
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

        RandomLog log1 = new FileRandomLog(TEST_FOLDER + "/" + TEST_LOG_FILE_1);
        RandomLog log2 = new FileRandomLog(TEST_FOLDER + "/" + TEST_LOG_FILE_2);
        RandomLog log3 = new FileRandomLog(TEST_FOLDER + "/" + TEST_LOG_FILE_3);

        log1.append(Segment.fromKeyValuePair("key1".getBytes(), "value1".getBytes()).getBytes());
        log1.append(Segment.fromKeyValuePair("key2".getBytes(), "value2".getBytes()).getBytes());
        log1.append(Segment.fromKeyValuePair("key3".getBytes(), "value3".getBytes()).getBytes());

        log2.append(Segment.fromKeyValuePair("key4".getBytes(), "value4".getBytes()).getBytes());
        log2.append(Segment.fromKeyValuePair("key1".getBytes(), "value5".getBytes()).getBytes());
        log2.append(Segment.fromKeyValuePair("key2".getBytes(), "value6".getBytes()).getBytes());

        log3.append(Segment.fromKeyValuePair("key7".getBytes(), "value7".getBytes()).getBytes());
        log3.append(Segment.fromKeyValuePair("key8".getBytes(), "value8".getBytes()).getBytes());
        log3.append(Segment.fromKeyValuePair("key1".getBytes(), "value9".getBytes()).getBytes());

        log1.close();
        log2.close();
        log3.close();

        bjornDB = BjornDB.getInstance(TEST_FOLDER);
    }

    @AfterEach
    void tearDown() throws IOException {
        bjornDB.stop();
        deleteFolderContentsIfExists(TEST_FOLDER);
    }

    @Test
    void givenMultipleLogFiles_whenCompaction_thenAllFilesRenamedCorrectly() throws Exception {
        // Given
        // A FireflyDB instance with a folder path
        bjornDB.start();

        // When
        // Compaction is triggered
        bjornDB.compaction();

        // Then
        // All log files are processed correctly
        assertTrue(Files.exists(Paths.get(TEST_FOLDER, "_1.log")));
        assertTrue(Files.exists(Paths.get(TEST_FOLDER, "_2.log")));
        assertTrue(Files.exists(Paths.get(TEST_FOLDER, "_3.log")));
        assertEquals("value9", new String(bjornDB.get("key1".getBytes())));
        assertEquals("value6", new String(bjornDB.get("key2".getBytes())));
        assertEquals("value3", new String(bjornDB.get("key3".getBytes())));
        assertEquals("value4", new String(bjornDB.get("key4".getBytes())));
        assertEquals("value7", new String(bjornDB.get("key7".getBytes())));
        assertEquals("value8", new String(bjornDB.get("key8".getBytes())));
    }
}
