package MachineLearning.NeuralNetwork.Trainer.Costs;

import LinearAlgebra.Types.Matrices.Matrix;

public class MSECost implements CostFunction {

    @Override
    public double function(double target, double prediction) {
        return 0.5 * Math.pow(target - prediction, 2);
    }

    @Override
    public Matrix costPrime(Matrix target, Matrix prediction) {
        return prediction.sub(target); // Simpler implementation than calling the prime function
    }

    @Override
    public double functionPrime(double target, double prediction) {
        return (prediction - target);
    }
}
