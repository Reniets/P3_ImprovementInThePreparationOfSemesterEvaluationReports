package Predictors;


import Enums.SentimentPolarity;

import java.sql.SQLException;
import java.util.List;

public interface PredictionMethod {
    SentimentPolarity predictSentiment(String comment) throws SQLException;

    List<SentimentPolarity> predictSentiments(List<String> comments) throws SQLException;
}
