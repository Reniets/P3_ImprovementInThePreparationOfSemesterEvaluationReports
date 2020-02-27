package Keywordanalyse;

import CSVHandler.CSV_Reader;
import Enums.SentimentPolarity;
import Preprocessing.Preprocessor;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static Enums.SentimentPolarity.NEGATIVE;
import static Enums.SentimentPolarity.POSITIVE;

public class TFIDF_Iterator {

    // Fields:
    private int min_df;
    private int max_df;
    private boolean stop_words;
    private boolean applyDictionaryNegative;
    private boolean applyDictionaryPositive;
    private String dictionaryNegativePath;
    private String dictionaryPositivePath;
    private String dataPath;

    // Constructors:
    public TFIDF_Iterator(int min_df, int max_df, boolean stop_words, boolean applyDictionaryNegative, boolean applyDictionaryPositive, String dictionaryNegativePath, String dictionaryPositivePath, String dataPath) {
        this.min_df = min_df;
        this.max_df = max_df;
        this.stop_words = stop_words;
        this.applyDictionaryNegative = applyDictionaryNegative;
        this.applyDictionaryPositive = applyDictionaryPositive;
        this.dictionaryNegativePath = dictionaryNegativePath;
        this.dictionaryPositivePath = dictionaryPositivePath;
        this.dataPath = dataPath;
    }

    // Getters
    public String getDataPath() {
        return dataPath;
    }

    // Equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFIDF_Iterator that = (TFIDF_Iterator) o;
        return min_df == that.min_df &&
                max_df == that.max_df &&
                stop_words == that.stop_words &&
                Objects.equals(dataPath, that.dataPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min_df, max_df, stop_words, dataPath);
    }

    // Methods
    public Map<String, Map<SentimentPolarity, Double>> find_TFIDF_overAllTerms() throws IOException, SQLException, ClassNotFoundException {
        double tf, idf, tf_idf;
        Integer documentNumber = 0, numberOfDocuments;

        // Call method to get all comments sorted into polarities
        Map<SentimentPolarity, List<String>> allDocuments = this.makePolarityCollections();
        numberOfDocuments = allDocuments.get(SentimentPolarity.POSITIVE).size() + allDocuments.get(SentimentPolarity.NEGATIVE).size();

        Map<SentimentPolarity, Map<String, Double>> mean_TFIDF_ofTerms = new HashMap<>();

        // Iterate over all sentiment polarities
        for (SentimentPolarity polarity : SentimentPolarity.values()) {

            // Make map to hold all terms and the corresponding tf-idf scores for current polarity
            Map<String, List<Double>> TFIDF_OfTerms = new HashMap<>();

            // Make list to hold all documents for current polarity
            List<String> documentsCurrentPolarity = allDocuments.get(polarity);

            // Make list and iterate over opposite polarities to add all opposite documents to one List
            List<List<String>> documentsOppositePolarity = new ArrayList<>();

            SentimentPolarity oppositePolarity = polarity.getOpposite();

            // Split the documents into words to only use split once
            for (String oppositeDocument : allDocuments.get(oppositePolarity)) {
                documentsOppositePolarity.add(new ArrayList<>(Arrays.asList(oppositeDocument.split("\\s+"))));
            }

            for (String document : documentsCurrentPolarity) {
                // Split document into terms
                List<String> terms = Arrays.asList(document.split("\\s+"));

                if (document.isEmpty()) continue;
                if (++documentNumber % 1000 == 0) System.out.println(documentNumber + "/" + numberOfDocuments);

                // Iterate over all terms in document
                for (String term : terms) {

                    if (!TFIDF_OfTerms.containsKey(term)) {
                        TFIDF_OfTerms.put(term, new ArrayList<>());

                        // If the term is new, we calculate idf and add it to index 0 of its arraylist
                        idf = TFIDF.inverseDocumentFrequency(term, documentsOppositePolarity);
                        TFIDF_OfTerms.get(term).add(idf);

                        // Count number of documents containing this term on index 1
                        TFIDF_OfTerms.get(term).add(0.0);
                    }

                    // If this is the last occurrence of the term in this document, increment docFreq counter
                    if (terms.lastIndexOf(term) == terms.indexOf(term)) {
                        Double docFreq = TFIDF_OfTerms.get(term).get(1);

                        TFIDF_OfTerms.get(term).set(1, ++docFreq);
                    }

                    // Get tf, idf and tfidf to add it to the arraylist
                    tf = TFIDF.termFrequency(term, terms);
                    idf = TFIDF_OfTerms.get(term).get(0);

                    tf_idf = tf * idf;

                    TFIDF_OfTerms.get(term).add(tf_idf);
                }
            }

            mean_TFIDF_ofTerms.put(polarity, sortMapByValue(calcMean_TFIDF_OfTerms(TFIDF_OfTerms)));
        }

        // Convert format of TFIDF, use terms as keys
        Map<String, Map<SentimentPolarity, Double>> TFIDF_ofAllTerms;
        TFIDF_ofAllTerms = convertKeywordFormat(mean_TFIDF_ofTerms);

        //  Apply dictionaries if chosen
        if (applyDictionaryNegative) {
            TFIDF_ofAllTerms = useDictionary(TFIDF_ofAllTerms, NEGATIVE, dictionaryNegativePath);
        }
        if (applyDictionaryPositive) {
            TFIDF_ofAllTerms = useDictionary(TFIDF_ofAllTerms, POSITIVE, dictionaryPositivePath);
        }

        return TFIDF_ofAllTerms;
    }

    private Map<String, Double> calcMean_TFIDF_OfTerms(Map<String, List<Double>> TFIDF_OfTerms) {
        double accum, mean;
        int i, len, docFreq;

        Map<String, Double> mean_TFIDF_OfTerms = new HashMap<>();

        for (Map.Entry<String, List<Double>> entry : TFIDF_OfTerms.entrySet()) {
            // Get number of times term was found and subtract 1 because index 0 has idf-value and index 1 has documentOccurrenceCounter
            len = entry.getValue().size() - 2;
            docFreq = entry.getValue().get(1).intValue();
            accum = 0;

            // If there is only one occurrence of term, set mean to 0
            if (docFreq < min_df || docFreq > max_df) {
                mean_TFIDF_OfTerms.put(entry.getKey(), 0.0);
                continue;
            }

            for (i = 2; i <= len; i++) {
                accum += entry.getValue().get(i);
            }

            mean = accum / len;

            mean_TFIDF_OfTerms.put(entry.getKey(), mean);
        }

        return mean_TFIDF_OfTerms;
    }

    private LinkedHashMap<String, Double> sortMapByValue(Map<String, Double> unsortedHashMap) {
        // Initializing linked list to be a list of the unsorted hash-map
        LinkedList<HashMap.Entry<String, Double>> list = new LinkedList<>(unsortedHashMap.entrySet());

        // Sort list based on custom comparator by value
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

        // Iterate through the sorted list and mirroring it to a map
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private static Map<String, Map<SentimentPolarity, Double>> convertKeywordFormat(Map<SentimentPolarity, Map<String, Double>> oldFormat) {
        Map<String, Map<SentimentPolarity, Double>> newFormat = new HashMap<>();

        // Iterate over all keywords in all sentiments
        for (Map.Entry<SentimentPolarity, Map<String, Double>> sentiment : oldFormat.entrySet()) {
            for (Map.Entry<String, Double> keyword : oldFormat.get(sentiment.getKey()).entrySet()) {

                // If keyword is already in the new format, add the sentiment
                if (!newFormat.containsKey(keyword.getKey())) {
                    Map<SentimentPolarity, Double> tempMap = new HashMap<>();
                    tempMap.put(sentiment.getKey(), keyword.getValue());

                    newFormat.put(keyword.getKey(), tempMap);
                } else {
                    newFormat.get(keyword.getKey()).put(sentiment.getKey(), keyword.getValue());
                }
            }
        }

        return newFormat;
    }

    Map<SentimentPolarity, List<String>> makePolarityCollections() throws IOException, SQLException, ClassNotFoundException {
        // Make csv reader with target of labels file
        CSV_Reader csvr = new CSV_Reader(this.dataPath);

        Preprocessor preprocessor = new Preprocessor();

        // Declare variables for use when looping through the labels file
        List<Pair<SentimentPolarity, String>> data = new ArrayList<>();

        SentimentPolarity sentiment;
        String comment;

        // Loop through each line of labels file
        for (List<String> line : csvr) {
            sentiment = SentimentPolarity.fromString(line.get(0));

            // Value is sentiment polarity(Pos,neu,neg), converted from string
            comment = line.get(1);

            data.add(new Pair<>(sentiment, comment));
        }

        // Initialize Hashmap from polarity to all comments with that polarity
        Map<SentimentPolarity, List<String>> commentsWithPolarities = new HashMap<>();
        commentsWithPolarities.put(POSITIVE, new ArrayList<>());
        commentsWithPolarities.put(NEGATIVE, new ArrayList<>());

        // Iterate over all data points and fill the new hashmap with the correct comments and sentiments
        for (Pair<SentimentPolarity, String> dataPair : data) {
            comment = dataPair.getValue();
            sentiment = dataPair.getKey();

            // Read the comment, and clean it
            List<String> cleanCommentWords = preprocessor.cleanText(comment);

            // Check if stop words should be removed
            if (stop_words) {
                cleanCommentWords = preprocessor.removeStopWords(cleanCommentWords);
            }

            // Set the comment string to whatever has been cleaned
            comment = preprocessor.convertListToString(cleanCommentWords);

            // Check to ensure that the comment has one of the possible sentiments
            if (sentiment != null) {

                // Decide which of the arrays the comment belongs to
                switch (sentiment) {
                    case POSITIVE:
                        commentsWithPolarities.get(POSITIVE).add(comment);
                        break;
                    case NEGATIVE:
                        commentsWithPolarities.get(NEGATIVE).add(comment);
                        break;
                    default:
                }
            }
        }

        return commentsWithPolarities;
    }

    private static Map<String, Map<SentimentPolarity, Double>> useDictionary(Map<String, Map<SentimentPolarity, Double>> tfidf_overAllTerms, SentimentPolarity sentiment, String path) throws IOException {
        String word;
        Double scalar = 2.0, weight = 1.0;

        CSV_Reader csvr = new CSV_Reader(path.toString());

        for (List<String> line : csvr) {
            word = line.get(0).toLowerCase();

            if (tfidf_overAllTerms.containsKey(word)) {
                // Scale the weight of the term
                tfidf_overAllTerms.get(word).replace(sentiment,
                        tfidf_overAllTerms.get(word).get(sentiment) == null
                                ? weight
                                : tfidf_overAllTerms.get(word).get(sentiment) * scalar);
            } else {
                Map<SentimentPolarity, Double> wordMap = new HashMap<>();
                wordMap.put(sentiment, weight);

                // Put the term in the map to
                tfidf_overAllTerms.put(word, wordMap);
            }
        }

        return tfidf_overAllTerms;
    }
}

