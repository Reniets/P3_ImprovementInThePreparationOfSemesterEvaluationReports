package MachineLearning.NeuralNetwork.Trainer.Costs;

import LinearAlgebra.Execution.Parallel;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;

public interface CostFunction {

    // The cost function, used to calculate error.
    default Matrix cost(Matrix target, Matrix prediction) {
        final int rows = target.getRows();
        final int cols = target.getColumns();

        //Matrix outMatrix = new DenseMatrix(rows, cols);
        MatrixBuilder outMatrix = new MatrixBuilder(rows, cols, true);
        //Comment
        Parallel.forIteration(rows, (row) -> {
            for (int col = 0; col < cols; col++) {
                double targetValue = target.getEntry(row, col);//target.toArray()[row][col];
                double predictedValue = prediction.getEntry(row, col);//prediction.toArray()[row][col];

                outMatrix.setEntry(row, col, this.function(targetValue, predictedValue));
            }
        });

        return outMatrix.build();
    }

    // The cost function, used to calculate error.
    double function(double target, double prediction);

    // The cost prime funciton, used in backpropagation.
    default Matrix costPrime(Matrix target, Matrix prediction) {
        final int rows = target.getRows();
        final int cols = target.getColumns();

        MatrixBuilder outMatrix = new MatrixBuilder(rows, cols, true);//new DenseMatrix(rows, cols);

        //for (int row = 0; row < rows; row++) {
        Parallel.forIteration(rows, (row) -> {
            for (int col = 0; col < cols; col++) {
                double targetValue = target.getEntry(row, col);//target.toArray()[row][col];
                double predictedValue = target.getEntry(row, col);//target.toArray()[row][col];

                outMatrix.setEntry(row, col, this.functionPrime(targetValue, predictedValue));//outMatrix.toArray()[row][col] = this.functionPrime(targetValue, predictedValue);
            }
        });

        return outMatrix.build();
    }

    // The cost prime funciton, used in backpropagation.
    double functionPrime(double target, double prediction);
}
