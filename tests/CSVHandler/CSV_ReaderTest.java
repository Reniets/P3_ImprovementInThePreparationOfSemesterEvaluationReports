package CSVHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CSV_ReaderTest {

    // Test Fields
    private static CSV_Reader CSVR;
    private static String filePath = "Tests/CSVHandler/Data/CSVR.csv";
    private static int totalLines = 2;
    private static ArrayList<String> stringLines = new ArrayList<>(totalLines);

    @BeforeAll
    static void beforeAll() throws IOException {
        stringLines.add("0;1;2;3;4;5;6;7;8;9");
        stringLines.add("test 1;11-02-18;æ;ø;å;Æ;Ø;Å;-;\\");
        CSVR = new CSV_Reader(filePath);
    }

    // Attach the specific file before each test
    @BeforeEach
    void beforeEach() throws IOException {
        CSVR.targetFile(filePath);
    }

    // Get current Line tests
    @Test
    void getCurrentLine01() {
        int currentLine = CSVR.getCurrentLine();
        assertEquals(0, currentLine);
    }

    @Test
    void getCurrentLine02() throws IOException {
        CSVR.nextLine();

        int currentLine = CSVR.getCurrentLine();

        assertEquals(1, currentLine);
    }

    @Test
    void getCurrentLine03() throws IOException {
        CSVR.nextLine();
        CSVR.nextLine();

        int currentLine = CSVR.getCurrentLine();

        assertEquals(2, currentLine);
    }

    @Test
    void getCurrentLine04() throws IOException {
        CSVR.nextLine();
        CSVR.nextLine();
        CSVR.nextLine();

        int currentLine = CSVR.getCurrentLine();

        assertEquals(1, currentLine);
    }

    // Get filePath test
    @Test
    void getFilePath() {
        String path = CSVR.getFilePath();

        assertEquals(filePath, path);
    }

    // Get total Lines test
    @Test
    void getTotalLines() {
        int lines = CSVR.getTotalLines();

        assertEquals(totalLines, lines);
    }

    // At last line test
    @Test
    void atLastLine() throws IOException {
        CSVR.nextLine();
        CSVR.nextLine();

        assertTrue(CSVR.atLastLine());
    }

    // Target file test
    @Test
    void targetFile() throws IOException {
        CSVR.targetFile(filePath);

        assertEquals(0, CSVR.getCurrentLine());
        assertEquals(filePath, CSVR.getFilePath());
        assertEquals(totalLines, CSVR.getTotalLines());
    }

    // Next line tests

    @Test
    void nextLine01() throws IOException {
        ArrayList<String> readLine = CSVR.nextLine();
        assertArrayEquals(stringLines.get(0).split(";"), readLine.toArray());
    }

    @Test
    void nextLine02() throws IOException {
        CSVR.nextLine();    // Skip line 1
        ArrayList<String> readLine = CSVR.nextLine();   // Read line 2
        assertArrayEquals(stringLines.get(1).split(";"), readLine.toArray());
    }

    @Test
    void nextLine03() throws IOException {
        CSVR.nextLine();    // Skip line 1
        CSVR.nextLine();    // Skip line 2
        ArrayList<String> readLine = CSVR.nextLine();   // Read line "1"
        assertArrayEquals(stringLines.get(0).split(";"), readLine.toArray());
    }

    @Test
    void nextLine04() {
        CSV_Reader CSVR = new CSV_Reader();

        Assertions.assertThrows(NullPointerException.class, CSVR::nextLine);
    }

    // Jump to line tests

    @Test
    void jumpToLine01() {
        assertTrue(CSVR.jumpToLine(2)); // Jump to line 2 should be possible
        assertEquals(2, CSVR.getTotalLines()); // We should be at line 2
    }

    @Test
    void jumpToLine02() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CSVR.jumpToLine(5);    // Jump to line 5 should NOT be possible - So an exception should be thrown
        });

        assertEquals(0, CSVR.getCurrentLine()); // We should still be at "line 0"
    }

    @Test
    void jumpToLine03() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CSVR.jumpToLine(-1);    // Jump to line -1 should NOT be possible
        });

        assertEquals(0, CSVR.getCurrentLine()); // We should still be at "line 0"
    }

    // Iterable test

    @Test
    void iterator01() {
        int counter = 0;

        for (ArrayList<String> readLine : CSVR) {
            assertArrayEquals(stringLines.get(counter++).split(";"), readLine.toArray());
            assertEquals(counter, CSVR.getCurrentLine());
        }

        assertTrue(CSVR.atLastLine());
    }

    @Test
    void iterator02() {
        CSVR = new CSV_Reader();

        Assertions.assertThrows(NullPointerException.class, () -> {
            for (ArrayList<String> readLine : CSVR) {
                // Do stuff
            }
        });
    }
}