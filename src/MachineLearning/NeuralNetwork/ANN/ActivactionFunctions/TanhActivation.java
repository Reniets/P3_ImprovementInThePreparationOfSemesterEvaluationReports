package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

import java.util.Objects;

public class TanhActivation implements ActivationFunction {
    private final int ID = 1;
    private final MatrixPref matrixPref = MatrixPref.OUT;

    @Override
    public double function(double x) {
        return Math.tanh(x);
    }

    @Override
    public double functionPrime(double x) {
        return 1 - Math.pow(x, 2);
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
        TanhActivation that = (TanhActivation) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ID);
    }
}
