package Preprocessing;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Matrices.OnehotMatrix;
import LinearAlgebra.Types.Vectors.OnehotVector;
import LinearAlgebra.Types.Vectors.Vector;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EmbeddingProcessorTest {

    // Test Constant
    private static final int PAIR_RADIUS = 1;
    private static final Path PAIRS_PATH = Paths.get("tests/Preprocessing/Data/embeddingTestFile.txt");
    private static final Path WORDS_PATH = Paths.get("tests/Preprocessing/Data/mostUsedWordsTestFile.txt");
    private static final Path EXPORT_PATH = Paths.get("tests/Preprocessing/Data/exportFile.txt");

    // Tests
    @Test
    void generateWordPairs() throws IOException, SQLException, ClassNotFoundException {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("dette", "er"));
        pairs.add(new Pair<>("er", "dette"));
        pairs.add(new Pair<>("er", "en"));
        pairs.add(new Pair<>("en", "er"));
        pairs.add(new Pair<>("en", "fil"));
        pairs.add(new Pair<>("fil", "en"));

        List<Pair<String, String>> pairsGenerated = EmbeddingProcessor.generateWordPairs(PAIRS_PATH, PAIR_RADIUS);

        for (int i = 0; i < pairs.size(); i++) {
            assertEquals(pairs.get(i).getKey(), pairsGenerated.get(i).getKey());
            assertEquals(pairs.get(i).getValue(), pairsGenerated.get(i).getValue());
        }
    }

    @Test
    void getMostUsedWords() throws IOException, SQLException, ClassNotFoundException {
        List<String> expected = new ArrayList<>();
        expected.add("dette");
        expected.add("er");
        expected.add("en");

        List<String> result = EmbeddingProcessor.getMostUsedWords(WORDS_PATH, 3);
        assertEquals(expected, result);
    }

    @Test
    void generateEmbeddingInputAndTargetOnehotMatricesFromPairs() throws IOException, SQLException, ClassNotFoundException {
        List<Pair<String, String>> pairsGenerated = EmbeddingProcessor.generateWordPairs(WORDS_PATH, PAIR_RADIUS);
        List<String> mostUsedWords = EmbeddingProcessor.getMostUsedWords(WORDS_PATH, 3);

        Matrix oneHotA = new MatrixBuilder()
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 0, 1)
                .addRow(0, 0, 1)
                .addRow(0, 0, 1)
                .build();

        Matrix oneHotB = new MatrixBuilder()
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(1, 0, 0)
                .addRow(0, 1, 0)
                .addRow(1, 0, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 1, 0)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0)
                .addRow(0, 0, 1)
                .addRow(0, 0, 1)
                .build();

        Pair<Matrix, Matrix> expected = new Pair<>(oneHotA, oneHotB);
        Pair<Matrix, Matrix> result = EmbeddingProcessor.generateEmbeddingInputAndTargetOnehotMatriciesFromPairs(pairsGenerated, mostUsedWords);
        assertEquals(expected, result);
    }

    @Test
    void exportListOfStrings() throws IOException {
        List<String> stringList = new ArrayList<>();
        stringList.add("one");
        stringList.add("two");
        stringList.add("three");

        EmbeddingProcessor.exportListOfStrings(stringList, EXPORT_PATH);
        assertTrue(Files.size(EXPORT_PATH) > 0);
    }

    @Test
    void createSequentialData() {
    }

    @Test
    void createTargetData() {
    }

    @Test
    void convertEmbeddingsToMap() throws IOException, SQLException, ClassNotFoundException {
        Matrix matrix = Matrices.getIdentityMatrix(3);
        List<String> mostUsedWords = EmbeddingProcessor.getMostUsedWords(WORDS_PATH, 3);

        Map<String, Integer> mostUsedWordsMap = new HashMap<>();

        Map<String, Vector> expected = new HashMap<>();
        for (int i = 0; i < matrix.getRows(); i++) {
            mostUsedWordsMap.put(mostUsedWords.get(i), i);
            expected.put(mostUsedWords.get(i), matrix.getRowVector(i));
        }

        assertEquals(expected, EmbeddingProcessor.convertEmbeddingsToMap(matrix, mostUsedWordsMap));
    }
}