package Preprocessing;

import dbComponent.database.DB;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StopWordFileReader {

    // Constant:
    private static final String CHARSET = "UTF-8";

    //Reads stop words from the default database
    public static List<String> read_stopwords() throws IOException, SQLException, ClassNotFoundException {

        return new DB().SELECT_allStopWords();
    }

    //Reads stop words from the parsed path, assuming a lineseparated file, with one word on each line
    public static List<String> read_stopwords(String path) throws IOException {
        Path file_path = Paths.get(path);

        List<String> stop_words;

        try (BufferedReader reader = Files.newBufferedReader(file_path, Charset.forName(CHARSET))) {
            stop_words = reader.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            throw ex;
        }

        return stop_words;
    }

}
