package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

public interface ActivationFunction {

    // Used for feed forward
    default Matrix activation(Matrix matrix) {
        int rows = matrix.getRows();
        int cols = matrix.getColumns();

        MatrixBuilder outMatrix = new MatrixBuilder(rows, cols, true);//.buildEmpty(rows,cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double value = matrix.getEntry(row, col);//double value = matrix.toArray()[row][col];

                outMatrix.setEntry(row, col, this.function(value));
            }
        }

        return outMatrix.build();
    }

    double function(double x);

    // Used for backpropagation.
    default Matrix activationPrime(Matrix matrix) {
        int rows = matrix.getRows();
        int cols = matrix.getColumns();

        MatrixBuilder outMatrix = new MatrixBuilder(rows, cols, true);//Matrix outMatrix = MatrixBuilder.buildEmpty(rows,cols);//new DenseMatrix(rows, cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double value = matrix.getEntry(row, col);// matrix.toArray()[row][col];

                outMatrix.setEntry(row, col, this.functionPrime(value));
            }
        }

        return outMatrix.build();
    }

    double functionPrime(double x);

    int getID();

    MatrixPref getMatrixPref();
}
