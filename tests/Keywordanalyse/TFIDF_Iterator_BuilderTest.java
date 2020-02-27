package Keywordanalyse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TFIDF_Iterator_BuilderTest {

    @Test
    void TFIDF_Iterator_BuilderTest01() {
        TFIDF_Iterator tfidf_iteratorWithBuilder = new TFIDF_Iterator_Builder()
                .set_min_df(1)
                .set_max_df(5)
                .set_stop_words(true)
                .set_dataPath("tests/Keywordsanalyse/Data/dataPath.txt")
                .set_dictionaryPositivePath("data/positiveWordsDan.txt")
                .build();

        TFIDF_Iterator tfidf_iteratorWithoutBuilder = new TFIDF_Iterator(
                1,
                5,
                true,
                true,
                false,
                "data/negativeWordsDan.txt",
                "data/positiveWordsDan.txt",
                "tests/Keywordsanalyse/Data/dataPath.txt");

        assertEquals(tfidf_iteratorWithoutBuilder, tfidf_iteratorWithBuilder);
    }
}