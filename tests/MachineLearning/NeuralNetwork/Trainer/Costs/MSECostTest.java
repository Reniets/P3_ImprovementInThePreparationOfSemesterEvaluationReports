package MachineLearning.NeuralNetwork.Trainer.Costs;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MSECostTest {

    // Test Field:
    private MSECost mse;

    @BeforeEach
    void beforeEach() {
        this.mse = new MSECost();
    }

    // Tests:
    @Test
    void function() {
        double result = mse.function(0.5, 0.7);
        double expected = 0.5 * Math.pow(0.5 - 0.7, 2);
        assertEquals(expected, result);
    }

    @Test
    void costPrime() {
        Matrix matrixA = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        Matrix matrixB = new MatrixBuilder()
                .addRow(2, 4, 6)
                .addRow(8, 10, 12)
                .addRow(14, 16, 18)
                .build();


        Matrix result = mse.costPrime(matrixA, matrixB);
        Matrix expected = matrixA;

        for (int i = 0; i < result.getRows(); i++) {
            assertArrayEquals(expected.toArray()[i], result.toArray()[i]);
        }
    }

    @Test
    void functionPrime() {
        double result = mse.functionPrime(0.5, 0.7);
        double expected = 0.7 - 0.5;
        assertEquals(expected, result);
    }
}