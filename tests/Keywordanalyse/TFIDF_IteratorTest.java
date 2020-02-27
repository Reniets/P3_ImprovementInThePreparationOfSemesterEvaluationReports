package Keywordanalyse;

import Enums.SentimentPolarity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TFIDF_IteratorTest {

    // Test Field:
    private static Map<SentimentPolarity, List<String>> polarityCollections;

    // Test Constant:
    private static final String DATA_PATH = "tests/Keywordanalyse/Data/data.txt";
    private static final String DICTIONARY_PATH = "tests/Keywordanalyse/Data/dictionary.txt";

    @BeforeAll
    static void beforeAll() throws IOException, SQLException, ClassNotFoundException {
        TFIDF_Iterator tf_idf_iterator = new TFIDF_Iterator_Builder()
                .set_min_df(2)
                .set_max_df(2)
                .set_applyDictionaryNegative(false)
                .set_ApplyDictionaryPositive(false)
                .set_dataPath(DATA_PATH)
                .build();

        polarityCollections = tf_idf_iterator.makePolarityCollections();
    }

    // Tests:
    // Test that the method imports and categorizes the positive comments correctly
    @Test
    void makePolarityCollections01() {
        assertEquals(2, polarityCollections.get(SentimentPolarity.POSITIVE).size());
    }

    // Test that the method imports and categorizes the negative comments correctly
    @Test
    void makePolarityCollections02() {
        assertEquals(2, polarityCollections.get(SentimentPolarity.NEGATIVE).size());
    }

    // Test that the documents are preprocessed
    @Test
    void makePolarityCollections03() {
        for (char ch : polarityCollections.get(SentimentPolarity.POSITIVE).get(0).toCharArray()) {
            assertTrue(Character.isLowerCase(ch) || Character.isSpaceChar(ch));
        }
    }

    // Test that deleted comments are ignored
    @Test
    void makePolarityCollections04() {
        int numberOfComments = 0;

        for(Map.Entry<SentimentPolarity, List<String>> entry : polarityCollections.entrySet()) {
            numberOfComments += entry.getValue().size();
        }

        assertEquals(4, numberOfComments);
    }

    // Tests that the 2 non-stop words in the positive sentiment are found
    @Test
    void find_TFIDF_overAllTerms01() throws IOException, SQLException, ClassNotFoundException {
        TFIDF_Iterator tf_idf_iterator = new TFIDF_Iterator_Builder()
                .set_dataPath(DATA_PATH)
                .set_applyDictionaryNegative(false)
                .set_ApplyDictionaryPositive(false)
                .build();

        assertEquals(4, tf_idf_iterator.find_TFIDF_overAllTerms().size());
    }

    // Throw exception because path is non-existent
    @Test
    void find_TFIDF_overAllTerms02() {
        TFIDF_Iterator tf_idf_iterator = new TFIDF_Iterator_Builder()
                .set_dataPath("nonexistentfolder/nonexistentdata.txt")
                .build();

        assertThrows(IOException.class, tf_idf_iterator::find_TFIDF_overAllTerms);
    }

    @Test
    void find_TFIDF_overAllTerms03() throws IOException, SQLException, ClassNotFoundException {
        TFIDF_Iterator tf_idf_iterator = new TFIDF_Iterator_Builder()
                .set_min_df(2)
                .set_applyDictionaryNegative(false)
                .set_ApplyDictionaryPositive(true)
                .set_dictionaryPositivePath(DICTIONARY_PATH)
                .set_dataPath(DATA_PATH)
                .build();

        assertEquals(10, tf_idf_iterator.find_TFIDF_overAllTerms().size());
    }

    @Test
    void TF_IDF_Iterator01() {
        TFIDF_Iterator tf_idf_iterator = new TFIDF_Iterator_Builder().build();

        assertEquals("data/folds/1/train_1.txt", tf_idf_iterator.getDataPath());
    }


    @Test
    void equalsSymmetricTest01() {
        TFIDF_Iterator tfidf_iterator1 = new TFIDF_Iterator_Builder().build();

        TFIDF_Iterator tfidf_iterator2 = new TFIDF_Iterator_Builder().build();

        assertTrue(tfidf_iterator1.equals(tfidf_iterator2) && tfidf_iterator2.equals(tfidf_iterator1));
        assertEquals(tfidf_iterator1.hashCode(), tfidf_iterator2.hashCode());
    }
}