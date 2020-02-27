package Keywordanalyse;

import java.util.Arrays;
import java.util.List;

public class TFIDF {

    // Constants:
    private static final boolean RAW_TF = false;
    private static final boolean SUBLINEAR_TF = false;
    private static final boolean FREQUENCY_TF = true;

    private static final boolean INV_DOC_FREQ = false;
    private static final boolean INV_DOC_FREQ_SMOOTH = false;
    private static final boolean PROB_INV_DOC_FREQ = true;

    // Find raw frequency in document
    static double termFrequency(String term, List<String> wordsInDocument) {
        int countTerm = 0, numberOfWordsInDocument;
        double frequency;

        // Compare each word to the term and count number of times it occurs
        for (String d : wordsInDocument) {
            if (term.equals(d)) {
                countTerm++;
            }
        }

        numberOfWordsInDocument = wordsInDocument.size();

        // Calculate and return term frequency document depending on the type
        if (SUBLINEAR_TF) {
            frequency = Math.log10(1 + numberOfWordsInDocument);
        } else if (FREQUENCY_TF) {
            frequency = (double) countTerm / (double) numberOfWordsInDocument;
        } else if (RAW_TF) {
            frequency = numberOfWordsInDocument;
        }

        return frequency;
    }

    // Overloading method to allow input of full document
    static double termFrequency(String term, String document) {
        List<String> wordsInDocument = Arrays.asList(document.split("\\s+"));

        return termFrequency(term, wordsInDocument);
    }

    static double inverseDocumentFrequency(String term, List<List<String>> documentsOppositePolarity) {
        // Find N: The number of documents with opposite polarity
        int N = documentsOppositePolarity.size();

        // Find number of documents of opposite polarity containing term
        int numberOfOppositeDocumentsContainingTerm = countDocumentsContainingTerm(term, documentsOppositePolarity);

        double idf;

        // Calculate inverse document frequency
        if (INV_DOC_FREQ) {
            idf = (Math.log10((double) (N) / (1 + numberOfOppositeDocumentsContainingTerm)));
            return Double.isFinite(idf) ? idf : 0.0;

        } else if (INV_DOC_FREQ_SMOOTH) {
            idf = (Math.log10(1 + ((double) (N) / (1 + numberOfOppositeDocumentsContainingTerm))));
            return Double.isFinite(idf) ? idf : 0.0;

        } else if (PROB_INV_DOC_FREQ) {
            idf = (Math.log10(((double) N - numberOfOppositeDocumentsContainingTerm) / (1 + numberOfOppositeDocumentsContainingTerm)));
            return Double.isFinite(idf) ? idf : 0.0;

        } else {
            return 1;
        }
    }

    private static int countDocumentsContainingTerm(String term, List<List<String>> documentsOppositePolarity) {
        int documentsWithTerm = 0;

        for (List<String> document : documentsOppositePolarity) {
            if (termFrequency(term, document) > 0) {
                documentsWithTerm++;
            }
        }

        return documentsWithTerm;
    }
}
