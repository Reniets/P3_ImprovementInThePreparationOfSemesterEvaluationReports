package MachineLearning.NeuralNetwork.Trainer.TrainingMethods;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import MachineLearning.NeuralNetwork.ANN.ANN;
import MachineLearning.NeuralNetwork.Trainer.Costs.CostFunction;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.ANNData;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.DefaultData;

public abstract class TrainingMethod {
    CostFunction CF;

    public TrainingMethod(CostFunction CF) {
        this.CF = CF;
    }

    final double calcError(Matrix target, Matrix prediction) {
        Matrix errorMatrix = CF.cost(target, prediction);

        return Matrices.sumMatrix(errorMatrix) / target.getRows();
    }

    // Trains 1 step, and returns the avg error
    public abstract double train(ANN network, ANNData allInputData, DefaultData allOutputData);
}
