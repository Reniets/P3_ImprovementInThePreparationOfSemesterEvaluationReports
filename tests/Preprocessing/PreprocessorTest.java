package Preprocessing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class PreprocessorTest {
    private Preprocessor preprocessor;

    // Returns an instantiated preprocessor, with a short, predefined list of stop words
    @BeforeEach
    void get_preprocessor() throws IOException, SQLException, ClassNotFoundException {
        preprocessor = new Preprocessor();
    }

    // Tests the removal of symbols
    @Test
    void cleanText01() {
        String responseSymbols = "hej ,den. der ++ super-mand";
        String[] responseSymbolsClean = {"hej", "den", "der", "supermand"};
        List<String> cleaned = preprocessor.cleanText(responseSymbols);
        assertArrayEquals(responseSymbolsClean, cleaned.toArray());
    }

    // Tests that it converts text to lower case
    @Test
    void cleanText02() {
        String responseCasing = "CAPS";
        String[] responseCasingClean = {"caps"};
        List<String> cleaned = preprocessor.cleanText(responseCasing);
        assertArrayEquals(responseCasingClean, cleaned.toArray());
    }

    @Test
    void cleanText03() {
        String responseNewLines = "new\n lines \r er \n\n gode";
        String[] responseNewLinesClean = {"new", "lines", "er", "gode"};
        List<String> cleaned = preprocessor.cleanText(responseNewLines);
        assertArrayEquals(responseNewLinesClean, cleaned.toArray());
    }

    @Test
    void cleanText04() {
        String responseTrim = "  trim spacing in beginning and end            ";
        String[] responseTrimClean = {"trim", "spacing", "in", "beginning", "and", "end"};
        List<String> cleaned = preprocessor.cleanText(responseTrim);
        assertArrayEquals(responseTrimClean, cleaned.toArray());
    }
    // Test that the preprocessor removes the relevant stop words and keeps every other word
    @Test
    // Test that the preprocessor removes the relevant stop words
    void removeStopWords01() {
        List<String> stop_words = new ArrayList<>();
        List<String> words = new ArrayList<>(stop_words);
        words.add("vigtig");
        assertEquals("vigtig", preprocessor.removeStopWords(words).get(0));
    }

    @Test
    void removeStopWords02() throws IOException {
        List<String> responseWords = new ArrayList<>();
        responseWords.add("en");
        responseWords.add("sætning");

        Preprocessor preprocessor = new Preprocessor("tests/Preprocessing/Data/stopWords.txt");
        assertEquals("sætning", preprocessor.removeStopWords(responseWords).get(0));
        assertEquals(1, preprocessor.removeStopWords(responseWords).size());
    }

    @Test
    void processCollaboration() {
        List<String> processList = preprocessor.process("Dette er en sætning konstrueret til at teste denne metode");
        List<String> cleanList = preprocessor.cleanText("Dette er en sætning konstrueret til at teste denne metode");
        assertEquals(preprocessor.removeStopWords(cleanList), processList);
    }
}