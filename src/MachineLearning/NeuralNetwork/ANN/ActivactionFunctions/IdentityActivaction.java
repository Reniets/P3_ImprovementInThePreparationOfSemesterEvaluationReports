package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

public class IdentityActivaction implements ActivationFunction {
    private final int ID = 3;
    private final MatrixPref matrixPref = MatrixPref.OUT;

    @Override
    public Matrix activation(Matrix matrix) {
        return matrix;
    }

    @Override
    public double function(double x) {
        return x;
    }

    @Override
    public Matrix activationPrime(Matrix matrix) {
        return MatrixBuilder.buildConstant(matrix.getRows(), matrix.getColumns(), 1);//new DenseMatrix(matrix.getRows(), matrix.getColumns(), 1); // The derivative of the identity activation is always 1
    }

    @Override
    public double functionPrime(double x) {
        return 1;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public MatrixPref getMatrixPref() {
        return matrixPref;
    }
}
