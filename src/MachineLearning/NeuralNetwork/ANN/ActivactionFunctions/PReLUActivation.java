package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

import java.util.Objects;

public class PReLUActivation implements ActivationFunction {
    private final double alpha = 0.01;
    private final int ID = 4;
    private final MatrixPref matrixPref = MatrixPref.NET;

    @Override
    public double function(double x) {
        return (x >= 0) ? x : x * alpha;
    }

    @Override
    public double functionPrime(double x) {
        return (x >= 0) ? 1 : alpha;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public MatrixPref getMatrixPref() {
        return matrixPref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PReLUActivation that = (PReLUActivation) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ID);
    }
}
