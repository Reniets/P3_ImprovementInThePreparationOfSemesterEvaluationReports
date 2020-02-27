package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

import java.util.Objects;

public class HardSigmoidActivation implements ActivationFunction {
    private final int ID = 5;
    private final MatrixPref matrixPref = MatrixPref.NET;

    @Override
    public double function(double x) {
        return Math.min(1, Math.max(0, 0.2 * x + 0.5));
    }

    @Override
    public double functionPrime(double x) {
        return (-2.5 < x && x < 2.5) ? 0.2 : 0;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public MatrixPref getMatrixPref() {
        return this.matrixPref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HardSigmoidActivation that = (HardSigmoidActivation) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
