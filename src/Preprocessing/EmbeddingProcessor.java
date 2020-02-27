package Preprocessing;

import CSVHandler.CSV_Reader;
import Enums.SentimentPolarity;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Vectors.NullVector;
import LinearAlgebra.Types.Vectors.OnehotVector;
import LinearAlgebra.Types.Vectors.Vector;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.DefaultData;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.SequentialData;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class EmbeddingProcessor {
    private EmbeddingProcessor() {
        // Private constructor
    }

    // Methods:
    public static List<Pair<String, String>> generateWordPairs(Path filePath, int radius) throws IOException, SQLException, ClassNotFoundException {
        Preprocessor preprocessor = new Preprocessor();
        CSV_Reader CSVR = new CSV_Reader(filePath.toString());

        List<Pair<String, String>> wordPairs = new ArrayList<>();

        for (List<String> readLine : CSVR) {
            List<String> preprocessedWords = preprocessor.cleanText(readLine.get(0));
            wordPairs.addAll(generatePairsFromList(preprocessedWords, radius));
        }
        CSVR.close();

        return wordPairs;
    }

    private static List<Pair<String, String>> generatePairsFromList(List<String> sentenceAsWords, int radius) {
        final int totalWords = sentenceAsWords.size();

        List<Pair<String, String>> pairList = new ArrayList<>();

        for (int i = 0; i < totalWords; i++) {
            final int rangeStart = Math.max(0, i - radius);
            final int rangeEnd = Math.min(totalWords - 1, i + radius);

            final String mainWord = sentenceAsWords.get(i);

            for (int range = rangeStart; range <= rangeEnd; range++) {
                if (range == i) {
                    continue;
                }

                final String rangeWord = sentenceAsWords.get(range);

                pairList.add(new Pair<>(mainWord, rangeWord));
            }
        }

        return pairList;
    }

    public static List<String> getMostUsedWords(Path filePath, int limit) throws IOException, SQLException, ClassNotFoundException {
        Preprocessor preprocessor = new Preprocessor();
        CSV_Reader CSVR = new CSV_Reader(filePath.toString());

        Map<String, Integer> wordCounter = new HashMap<>(5000);

        for (List<String> readLine : CSVR) {
            List<String> split = preprocessor.cleanText(readLine.get(0));

            for (String word : split) {
                if (!wordCounter.containsKey(word)) {
                    wordCounter.put(word, 1);
                } else {
                    wordCounter.put(word, wordCounter.get(word) + 1);
                }
            }
        }

        CSVR.close();

        List<String> mostUsedWords = wordCounter.entrySet().stream()
                .sorted(Comparator.comparing(HashMap.Entry::getValue, Comparator.reverseOrder()))
                .map(HashMap.Entry::getKey)
                .collect(Collectors.toList())
                .subList(0, limit);

        return mostUsedWords;
    }

    public static Pair<Matrix, Matrix> generateEmbeddingInputAndTargetOnehotMatriciesFromPairs(List<Pair<String, String>> wordPairs, List<String> mostUsedWords) {
        List<Pair<String, String>> actualWordPairs = new ArrayList<>(wordPairs.size());

        // Remove all unwanted word pairs
        for (Pair<String, String> pair : wordPairs) {
            String key = pair.getKey();
            String value = pair.getValue();

            if (mostUsedWords.contains(key) && mostUsedWords.contains(value)) {
                actualWordPairs.add(pair);
            }
        }

        // Generate input & target
        Map<String, Integer> mostUsedWordsMap = convertListToMapWithEntryNumber(mostUsedWords);

        List<Vector> inputVectors = new ArrayList<>();
        List<Vector> targetVectors = new ArrayList<>();
        final int totalWords = mostUsedWords.size();

        for (Pair<String, String> pair : actualWordPairs) {
            String key = pair.getKey();
            String value = pair.getValue();

            inputVectors.add(new OnehotVector(mostUsedWordsMap.get(key), totalWords));
            targetVectors.add(new OnehotVector(mostUsedWordsMap.get(value), totalWords));
        }

        Matrix inputMatrix = MatrixBuilder.buildVectorList(inputVectors);
        Matrix targetMatrix = MatrixBuilder.buildVectorList(targetVectors);

        return new Pair<>(inputMatrix, targetMatrix);
    }

    public static void exportListOfStrings(List<String> list, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String elem : list) {
                writer.write(elem);
                writer.write("\n");
            }
        }
    }

    public static List<String> importListOfStrings(Path path) throws IOException {
        CSV_Reader CSVR = new CSV_Reader(path.toString());
        List<String> lines = new ArrayList<>();

        for (List<String> readLine : CSVR) {
            lines.add(readLine.get(0));
        }

        return lines;
    }

    public static Map<String, Integer> convertListToMapWithEntryNumber(List<String> list) {
        Map<String, Integer> map = new HashMap<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
        }

        return map;
    }

    public static SequentialData createSequentialData(Map<String, Vector> embeddingMap, List<List<String>> listOfSentences, int wordLimit) {
        SequentialData data = new SequentialData();
        List<List<Vector>> sentencesAsList = new ArrayList<>(wordLimit);

        final int vectorLen = new ArrayList<>(embeddingMap.values()).get(0).size(); // Contract: All vectors has the same length.

        for (int i = 0; i < wordLimit; i++) {
            sentencesAsList.add(new ArrayList<>(listOfSentences.size()));
        }

        for (List<String> sentence : listOfSentences) {
            List<Vector> sentenceAsVectorList = convertSentenceToEmbeddingVectorList(embeddingMap, sentence, vectorLen, wordLimit);

            for (int i = 0; i < wordLimit; i++) {
                sentencesAsList.get(i).add(sentenceAsVectorList.get(i));
            }
        }

        for (List<Vector> vectorList : sentencesAsList) {
            Matrix matrix = MatrixBuilder.buildVectorList(vectorList);
            data.addSequentialData(matrix);
        }

        return data;
    }

    public static DefaultData createTargetData(List<SentimentPolarity> sentiments) {
        final int totalSentiments = SentimentPolarity.values().length;
        MatrixBuilder builder = new MatrixBuilder(sentiments.size(), totalSentiments, true);

        for (int i = 0; i < sentiments.size(); i++) {
            SentimentPolarity sentiment = sentiments.get(i);
            builder.setRow(i, new OnehotVector(sentiment.getValue(), totalSentiments));
        }

        return new DefaultData(builder.build());
    }

    private static List<Vector> convertSentenceToEmbeddingVectorList(Map<String, Vector> embeddingMap, List<String> sentence, int vectorLen, int wordLimit) {
        List<Vector> vectorList = new ArrayList<>();
        Vector nullVector = new NullVector(vectorLen);

        for (int i = 0; i < wordLimit; i++) {
            if (sentence.size() > i) {
                String word = sentence.get(i);
                vectorList.add(embeddingMap.getOrDefault(word, nullVector));
            } else {
                vectorList.add(nullVector);
            }
        }

        return vectorList;
    }

    public static Map<String, Vector> convertEmbeddingsToMap(Matrix embeddings, Map<String, Integer> mostUsedWords) {
        Map<String, Vector> embeddingMap = new HashMap<>(embeddings.getRows());

        for (String key : mostUsedWords.keySet()) {
            int index = mostUsedWords.get(key);
            embeddingMap.put(key, embeddings.getRowVector(index));
        }

        return embeddingMap;
    }
}
