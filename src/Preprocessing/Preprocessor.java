package Preprocessing;

import dbComponent.database.DB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preprocessor {
    // Field:
    private List<String> stop_words; //Words that are not considered Key words

    // Constructors:
    // Without specified path for stop words
    public Preprocessor() throws SQLException, ClassNotFoundException {
        this.stop_words = new DB().SELECT_allStopWords();
    }

    public Preprocessor(String path) throws IOException {
        this.stop_words = StopWordFileReader.read_stopwords(path);
    }

    // Methods:
    // Cleans the text for unwanted symbols and words, and returns words as list
    public List<String> cleanText(String response) {
        // Replaces all non-letter or space chars with nothing)
        response = response.replaceAll("[^a-zA-ZæøåÆØÅ ]", "");

        // Converts response to lower case
        response = response.toLowerCase();

        // Replaces all newlines with a space
        response = response.replace("\n", " ").replace("\r", " ");

        // Removes all spacing in the beginning and end of the string
        response = response.trim();

        // Splits the response into a list of words
        ArrayList<String> responseWords = new ArrayList<>(Arrays.asList(response.split("\\s+")));

        // Removes all words in the list that has a length of 1 or 0 and is not i, ø or å
        responseWords.removeIf(word -> word.length() <= 1 && word.matches("[^iøå]"));

        return responseWords;
    }

    // Removes all stop words from the input
    public List<String> removeStopWords(List<String> response) {
        response.removeAll(this.stop_words);
        return response;
    }

    public List<String> process(String response) {
        List<String> cleanResponse = cleanText(response);
        return removeStopWords(cleanResponse);
    }

    // Converts a list of words to one string with spaces
    public String convertListToString(List<String> stringList) {
        StringBuilder sb = new StringBuilder();
        for (String string : stringList) {
            sb.append(string);
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}