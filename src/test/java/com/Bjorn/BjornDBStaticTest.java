package com.Bjorn;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class BjornDBStaticTest {
    private static final String FOLDER_A = "/path/to/folderA";
    private static final String FOLDER_B = "/path/to/folderB";

    @Test
    void givenSameFolder_whenGetInstance_thenSameObjectReferenced() {
        // Given
        // Two instances with the same folder should reference the same object

        // When
        BjornDB dbA1 = BjornDB.getInstance(FOLDER_A);
        BjornDB dbA2 = BjornDB.getInstance(FOLDER_A);

        // Then
        assertSame(dbA1, dbA2);
        assertEquals(FOLDER_A, dbA1.getFolderPath());
        assertEquals(FOLDER_A, dbA1.getFolderPath());
    }

    @Test
    void givenDifferentFolders_whenGetInstance_thenDifferentObjectsReferenced() {
        // Given
        // Two instances with different folders should reference different objects

        // When
        BjornDB dbA = BjornDB.getInstance(FOLDER_A);
        BjornDB dbB = BjornDB.getInstance(FOLDER_B);

        // Then
        assertNotSame(dbA, dbB);
        assertEquals(FOLDER_A, dbA.getFolderPath());
        assertEquals(FOLDER_B, dbB.getFolderPath());
    }

    @Test
    void givenGetInstanceMethod_whenCheckSynchronizedModifier_thenTrue() throws NoSuchMethodException {
        // Given
        Method getInstanceMethod = BjornDB.class.getDeclaredMethod("getInstance", String.class);

        // When/Then
        assertTrue(Modifier.isSynchronized(getInstanceMethod.getModifiers()));
    }
}
