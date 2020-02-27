package Keywordanalyse;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TFIDFTest {
    private static final String document1 = "dette dokument er et positivt dokument";
    private static final String document2 = "dette dokument er et negativt dokument";
    private static final List<String> document2Words = Arrays.asList(document2.split("\\s+"));

    // Test term frequency
    @Test
    void termFrequency01() {
        assertEquals(2.0/6.0, TFIDF.termFrequency("dokument", document1), 0.01);
    }

    // Test that a word not in the document returns TF of 0
    @Test
    void termFrequency02() {
        assertEquals(0, TFIDF.termFrequency("test", document1));
    }

    // Test that a term occuring in all documents of opposite polarities return IDF of 0
    @Test
    void inverseDocumentFrequency01() {
        List<List<String>> documentsOppositePolarity = new ArrayList<>();
        documentsOppositePolarity.add(document2Words);
        assertTrue(TFIDF.inverseDocumentFrequency("dette", documentsOppositePolarity) <= 0);
    }

    // Test that if a term does not occur in any documents of opposite polarity, the IDF returns log(N): Number of documents of opposite polarity in log 10.
    @Test
    void inverseDocumentFrequency02() {
        List<List<String>> documentsOppositePolarity = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            documentsOppositePolarity.add(new ArrayList<>());
        }

        assertEquals(1.0, TFIDF.inverseDocumentFrequency("dette", documentsOppositePolarity));
    }
}