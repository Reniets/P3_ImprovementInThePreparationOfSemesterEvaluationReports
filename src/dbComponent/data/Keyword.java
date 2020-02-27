package dbComponent.data;

import Enums.SentimentPolarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Keyword {

    // Fields:
    private String keyword;
    private Double positive;
    private Double negative;

    // Constructors:
    public Keyword(String keyword, Double positive, Double negative) {
        this.keyword = keyword;
        this.positive = positive;
        this.negative = negative;
    }

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    // Getters:
    public String getKeyword() {
        return keyword;
    }

    public Double getPositive() {
        return positive;
    }

    public Double getNegative() {
        return negative;
    }


    public Map<SentimentPolarity, Double> getSentimentWeightMap() {
        Map<SentimentPolarity, Double> sentimentWeightMap = new HashMap<>();

        sentimentWeightMap.put(SentimentPolarity.POSITIVE, positive);
        sentimentWeightMap.put(SentimentPolarity.NEGATIVE, negative);

        return sentimentWeightMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword1 = (Keyword) o;
        return Objects.equals(keyword, keyword1.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword);
    }
}
