package CSVHandler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSV_WriterTest {

    // Test Field:
    private static CSV_Writer csvWriter;

    // Test Constant:
    private static final File FILE = new File("CSVW.csv");

    @BeforeAll
    static void beforeAll() {
        csvWriter = new CSV_Writer();
    }

    // Tests:
    @Test
    void targetFile() throws IOException {
        csvWriter.targetFile(FILE, true);
        assertEquals(FILE, csvWriter.getFile());
    }

    // Already an existing file attached?
    @Test
    void targetFile02() throws IOException {
        csvWriter.targetFile(FILE, true);
        csvWriter.targetFile(FILE, true);
        assertEquals(FILE, csvWriter.getFile());
    }

    @Test
    void close() throws IOException {
        csvWriter.targetFile(FILE, true);
        csvWriter.close();
        assertEquals(null, csvWriter.getFile());
    }
}