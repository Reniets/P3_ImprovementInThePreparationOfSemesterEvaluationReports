package Preprocessing;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Vectors.Vector;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Word2vecWrapper {
    // Field:
    private HashMap<String, Vector> embeddingMap = new HashMap<>();

    // Constructor:
    public Word2vecWrapper(List<String> mostUsedWords, Matrix embeddings) {
        for (int i = 0; i < mostUsedWords.size(); i++) {
            String word = mostUsedWords.get(i);
            Vector embedding = embeddings.getRowVector(i);

            this.embeddingMap.put(word, embedding);
        }
    }

    // Method:
    public void X_isTo_Y_As_Z_isTo(String X, String Y, String Z, int limit) {
        Vector vectorX = this.embeddingMap.get(X);
        Vector vectorY = this.embeddingMap.get(Y);
        Vector vectorZ = this.embeddingMap.get(Z);

        Vector X_YDiff = vectorY.sub(vectorX);

        Vector newArea = vectorZ.add(X_YDiff);

        System.out.print(X + ":" + Y + "::" + Z + ":[");
        for (String word : getNeighboorVectors(newArea, limit)) {
            System.out.print(word + ", ");
        }
        System.out.println("]");
    }

    // Getters:
    public List<String> getNeighboorVectors(String word, int limit) {
        List<String> neighboors = getNeighboorVectors(this.embeddingMap.get(word), limit + 1).subList(1, limit + 1);
        return neighboors;
    }

    private List<String> getNeighboorVectors(Vector area, int limit) {
        HashMap<String, Double> distances = new HashMap<>(this.embeddingMap.size());

        for (String word : this.embeddingMap.keySet()) {
            double distance = area.cosineSimilarity(this.embeddingMap.get(word));
            //System.out.println(word + ": " + distance);
            distances.put(word, distance);
        }

        List<String> bestCandidates = distances.entrySet().stream()
                .sorted(Comparator.comparing(HashMap.Entry::getValue, Collections.reverseOrder()))
                .map(HashMap.Entry::getKey)
                .collect(Collectors.toList())
                .subList(0, limit);

        for (String word : bestCandidates.subList(1, limit)) {
            System.out.println(word + ": " + area.cosineSimilarity(this.embeddingMap.get(word)));
        }

        return bestCandidates;
    }
}
