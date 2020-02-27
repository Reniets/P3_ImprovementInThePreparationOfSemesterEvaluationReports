package Keywordanalyse;

import Enums.SentimentPolarity;
import dbComponent.data.Keyword;
import dbComponent.database.DB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TFIDF_dbHandler {

    // Methods
    public static void export_TFIDF_toDataBase(TFIDF_Iterator tf_idf_iterator, DB db) throws SQLException, IOException, ClassNotFoundException {
        // Calculate tfidf weights for all keywords, and convert it to the correct format
        Map<String, Map<SentimentPolarity, Double>> tfidf_overAllTerms = tf_idf_iterator.find_TFIDF_overAllTerms();

        // Create database and clear it
        db.DELETE_allKeywords();

        // Declare variables for use in foreach loop
        String keywordTerm;
        Double positive, negative;
        Map<SentimentPolarity, Double> sentimentMap;
        List<Keyword> keywords = new ArrayList<>();

        // Iterate over all keywords and add their values to database
        for (Map.Entry<String, Map<SentimentPolarity, Double>> keyword : tfidf_overAllTerms.entrySet()) {
            // Retrieve relevant information form keyword map
            keywordTerm = keyword.getKey();
            sentimentMap = keyword.getValue();
            positive = sentimentMap.get(SentimentPolarity.POSITIVE);
            negative = sentimentMap.get(SentimentPolarity.NEGATIVE);

            // Check if all values are either 0.0 or null, then dont use it
            if ((positive == null || positive == 0.0) && (negative == null || negative == 0.0))
                continue;

            // Insert word, if weight is null give 0.0 instead.
            keywords.add(
                    new Keyword(keywordTerm,
                            positive == null ? 0.0 : positive,
                            negative == null ? 0.0 : negative));
        }

        db.INSERT_newKeywords(keywords);
    }

    public static List<Keyword> import_TFIDF_fromDataBase() throws SQLException, ClassNotFoundException {
        DB db = new DB();

        return db.SELECT_allKeywords();
    }
}
