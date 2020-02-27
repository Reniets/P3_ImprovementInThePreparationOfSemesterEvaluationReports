package Predictors;

import Enums.SentimentPolarity;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Vectors.Vector;
import MachineLearning.NeuralNetwork.ANN.Layers.SoftmaxLayer;
import Preprocessing.Preprocessor;
import dbComponent.data.Keyword;
import dbComponent.database.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictKeyword implements PredictionMethod {

    private Preprocessor preprocessor;
    private List<Keyword> keywords;
    private Map<SentimentPolarity, Double> accumWeights;

    public PredictKeyword(DB database) throws SQLException, ClassNotFoundException {
        this.preprocessor = new Preprocessor();
        this.keywords = database.SELECT_allKeywords();
    }

    // Returns a SentimentPolarity if it can determine one, otherwise null
    @Override
    public SentimentPolarity predictSentiment(String comment) {
        this.accumWeights(comment);

        // Find the sentiment with the largest weight and return it.
        SentimentPolarity classifiedSentiment = null;
        Double maxAccumWeight = Double.MIN_VALUE;

        // Find the sentiment with the highest accumulated TFIDF weight
        for (Map.Entry<SentimentPolarity, Double> sentimentWeight : accumWeights.entrySet()) {
            if (sentimentWeight.getValue() > maxAccumWeight) {
                classifiedSentiment = sentimentWeight.getKey();
                maxAccumWeight = sentimentWeight.getValue();
            }
        }

        return classifiedSentiment;
    }

    @Override
    public List<SentimentPolarity> predictSentiments(List<String> comments) {
        List<SentimentPolarity> sentiments = new ArrayList<>(comments.size());
        for (String comment : comments) {
            sentiments.add(predictSentiment(comment));
        }

        return sentiments;
    }

    public Matrix predictSentimentWithSoftMax(String comment) {
        this.accumWeights(comment);

        // Make matrixbuilder and add cols
        MatrixBuilder weightMatrixBuilder = new MatrixBuilder(1, 2, true);

        int colIndex = 0;

        for (SentimentPolarity sentiment : SentimentPolarity.values()) {
            weightMatrixBuilder.setColumn(colIndex++, this.accumWeights.get(sentiment));
        }

        Matrix weightMatrix = weightMatrixBuilder.build();

        // Run softmax on these
        SoftmaxLayer softmaxLayer = new SoftmaxLayer(2);
        softmaxLayer.updateLayer(weightMatrix);

        return softmaxLayer.getOutMatrix();
    }

    public Matrix predictSentimentWithSoftMax(List<String> comments) {
        MatrixBuilder resultMatrixBuilder = new MatrixBuilder(comments.size(), 2, true);

        int counter = 0;
        for (String comment : comments) {
            Matrix prediction = this.predictSentimentWithSoftMax(comment);
            Vector predcitionVector = prediction.getRowVector(0);

            resultMatrixBuilder.setRow(counter++, predcitionVector);
        }

        return resultMatrixBuilder.build();
    }

    private void accumWeights(String comment) {
        List<String> processedComment = this.preprocessor.process(comment);
        this.accumWeights = new HashMap<>();

        // Initialize sentiment weights
        for (SentimentPolarity sentiment : SentimentPolarity.values()) {
            accumWeights.put(sentiment, 0.0);
        }

        // Accumulate weights for each term in comment
        for (String term : processedComment) {
            Keyword keywordTerm = new Keyword(term);
            Keyword keyword;

            // Accumulate weights in map for each sentiment if keyword exists in database
            if (keywords.contains(keywordTerm)) {
                keyword = keywords.get(keywords.indexOf(keywordTerm));

                for (SentimentPolarity sentiment : SentimentPolarity.values()) {
                    accumWeights.replace(sentiment, accumWeights.get(sentiment) + keyword.getSentimentWeightMap().get(sentiment));
                }
            }
        }
    }
}

