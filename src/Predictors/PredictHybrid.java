package Predictors;

import Enums.SentimentPolarity;
import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import MachineLearning.NeuralNetwork.ANN.ANN;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.DefaultData;
import dbComponent.database.DB;
import dbComponent.enums.NetworkType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PredictHybrid implements PredictionMethod {
    private ANN hybridNetwork;
    private PredictLSTM LSTM;
    private PredictKeyword keyword;

    public PredictHybrid(DB db) throws SQLException, IOException, ClassNotFoundException {
        this.hybridNetwork = db.SELECT_neuralNetwork(NetworkType.HYBRID);
        this.LSTM = new PredictLSTM(db);
        this.keyword = new PredictKeyword(db);
    }

    @Override
    public SentimentPolarity predictSentiment(String comment) {
        return this.predictSentiments(Collections.singletonList(comment)).get(0);
    }

    @Override
    public List<SentimentPolarity> predictSentiments(List<String> comments) {
        // Get predicitons from both methods
        Matrix predicitonLSTM = this.LSTM.predictRaw(comments);
        Matrix predictionKeyword = this.keyword.predictSentimentWithSoftMax(comments);

        // Concat predicitons
        Matrix concatPredicitons = Matrices.concatRows(predicitonLSTM, predictionKeyword);

        // Create hybrid predictions
        Matrix hybridPrediciton = this.hybridNetwork.evaluateInputs(new DefaultData(concatPredicitons));

        // Convert to sentiment list and return it.
        return PredictLSTM.convertPredictionsToSentiments(hybridPrediciton);
    }
}
