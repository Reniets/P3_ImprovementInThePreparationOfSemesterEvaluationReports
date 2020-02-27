package Keywordanalyse;

public class TFIDF_Iterator_Builder {

    // Fields:
    private int min_df = 5;
    private int max_df = 500;
    private boolean stop_words = true;
    private boolean applyDictionaryNegative = true;
    private boolean applyDictionaryPositive = false;
    private String dictionaryNegativePath = "data/negativeWordsDan.txt";
    private String dictionaryPositivePath = "data/positiveWordsDan.txt";
    private String dataPath = "data/folds/1/train_1.txt";

    // Methods:
    public TFIDF_Iterator_Builder set_min_df(int min_df) {
        this.min_df = min_df;
        return this;
    }

    public TFIDF_Iterator_Builder set_max_df(int max_df) {
        this.max_df = max_df;
        return this;
    }

    public TFIDF_Iterator_Builder set_stop_words(boolean stop_words) {
        this.stop_words = stop_words;
        return this;
    }

    public TFIDF_Iterator_Builder set_applyDictionaryNegative(boolean applyDictionaryNegative) {
        this.applyDictionaryNegative = applyDictionaryNegative;
        return this;
    }

    public TFIDF_Iterator_Builder set_ApplyDictionaryPositive(boolean applyDictionaryPositive) {
        this.applyDictionaryPositive = applyDictionaryPositive;
        return this;
    }

    public TFIDF_Iterator_Builder set_dictionaryNegativePath(String dictionaryNegativePath) {
        this.dictionaryNegativePath = dictionaryNegativePath;
        return this;
    }

    public TFIDF_Iterator_Builder set_dictionaryPositivePath(String dictionaryPositivePath) {
        this.dictionaryPositivePath = dictionaryPositivePath;
        return this;
    }

    public TFIDF_Iterator_Builder set_dataPath(String dataPath) {
        this.dataPath = dataPath;
        return this;
    }

    public TFIDF_Iterator build() {
        return new TFIDF_Iterator(min_df, max_df, stop_words, applyDictionaryNegative, applyDictionaryPositive, dictionaryNegativePath, dictionaryPositivePath, dataPath);
    }

}
