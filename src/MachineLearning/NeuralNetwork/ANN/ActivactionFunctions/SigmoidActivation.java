package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

import java.util.Objects;

public class SigmoidActivation implements ActivationFunction {
    private final int ID = 0;
    private final MatrixPref matrixPref = MatrixPref.OUT;

    @Override
    public double function(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double functionPrime(double x) {
        return x * (1 - x);
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
        SigmoidActivation that = (SigmoidActivation) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ID);
    }
}
