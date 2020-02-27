package Predictors;

import Enums.SentimentPolarity;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Vectors.Vector;
import MachineLearning.NeuralNetwork.ANN.ANN;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.SequentialData;
import Preprocessing.EmbeddingProcessor;
import Preprocessing.Preprocessor;
import dbComponent.database.DB;
import dbComponent.enums.NetworkType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PredictLSTM implements PredictionMethod {
    private ANN network;
    private Map<String, Vector> embeddingMap;
    private Preprocessor preprocessor = new Preprocessor();

    private static final int MAX_WORDS = 40;

    public PredictLSTM(DB database) throws SQLException, ClassNotFoundException {
        this.network = database.SELECT_neuralNetwork(NetworkType.SENTIMENT);
        this.embeddingMap = database.SELECT_allWordEmbeddings();
    }

    @Override
    public SentimentPolarity predictSentiment(String comment) {
        SequentialData sequentialData = this.commentToSequentialData(comment);
        return this.predictSentiments(sequentialData).get(0);
    }

    @Override
    public List<SentimentPolarity> predictSentiments(List<String> comments) {
        SequentialData sequentialData = this.commentsToSequentialData(comments);
        return this.predictSentiments(sequentialData);
    }

    private List<SentimentPolarity> predictSentiments(SequentialData sequentialData) {
        Matrix predictions = this.predictRaw(sequentialData);
        return convertPredictionsToSentiments(predictions);
    }

    Matrix predictRaw(List<String> comments) {
        SequentialData sequentialData = this.commentsToSequentialData(comments);
        return predictRaw(sequentialData);
    }

    Matrix predictRaw(SequentialData sequentialData) {
        return this.network.evaluateInputs(sequentialData);
    }

    static List<SentimentPolarity> convertPredictionsToSentiments(Matrix predictions) {
        List<SentimentPolarity> sentiments = new ArrayList<>(predictions.getRows());

        for (int row = 0; row < predictions.getRows(); row++) {
            Vector rowVector = predictions.getRowVector(row);
            int maxPos = rowVector.getMaxValuePos();

            SentimentPolarity sentiment = SentimentPolarity.fromValue(maxPos);

            sentiments.add(sentiment);
        }

        return sentiments;
    }

    private SequentialData commentsToSequentialData(List<String> comments) {
        List<List<String>> words = new ArrayList<>(comments.size());

        for (String comment : comments) {
            words.add(preprocessor.cleanText(comment));
        }

        return EmbeddingProcessor.createSequentialData(this.embeddingMap, words, MAX_WORDS);
    }

    private SequentialData commentToSequentialData(String comment) {
        return commentsToSequentialData(Collections.singletonList(comment));
    }
}
