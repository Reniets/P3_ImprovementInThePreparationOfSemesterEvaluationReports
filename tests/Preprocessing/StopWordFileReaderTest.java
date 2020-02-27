package Preprocessing;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StopWordFileReaderTest {

    // Tests:
    //tests the read method with no parameters.
    @Test
    void read_stop_words_no_parameters() throws IOException, SQLException, ClassNotFoundException { //Without parameters
        List<String> stopWords;

        stopWords = StopWordFileReader.read_stopwords();

        assertNotNull(stopWords);
    }

    //Tests with a path in the form of a string as parameter
    @Test
    void read_stop_words_with_parameters() throws IOException {
        List<String> stopWords;

        stopWords = StopWordFileReader.read_stopwords("tests/Preprocessing/Data/stopWords.txt");

        assertNotNull(stopWords);
    }

    //Tests with a path to a non existant file, to check that the proper exception is thrown.
    @Test
    void read_stop_words_with_parameters_exception(){
        String path = "data/doesnotexit.txt";
        File f = new File(path);

        assertTrue(!f.isFile());
        assertThrows(IOException.class, () -> StopWordFileReader.read_stopwords(path));
    }
}