package MachineLearning.NeuralNetwork.ANN.Layers;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Vectors.Vector;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.ActivationFunction;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.SigmoidActivation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DenseLayerTest {

    // Test Field:
    private ActivationFunction af;

    // Test Constant:
    private static final int TOTAL_REPS = 5;

    @BeforeEach
    void beforeEach() {
        af = new SigmoidActivation();
    }

    // Tests:
    // Weights all 0
    @RepeatedTest(TOTAL_REPS)
    void updateLayer01(RepetitionInfo repetitionInfo) {
        // Set the input and output sizes
        int inputSize = repetitionInfo.getCurrentRepetition();
        int outputSize = TOTAL_REPS - inputSize + 1;
        int totalInputs = 3;

        // Create the layer
        DenseLayer layer = new DenseLayer(inputSize, outputSize, this.af);
        layer.setWeightMatrix(MatrixBuilder.buildEmpty(inputSize, outputSize));
        layer.setBiasWeightMatrix(MatrixBuilder.buildEmpty(1, outputSize));

        // Create the inputs and expected outputs
        Matrix inputs = Matrices.randomMatrix(totalInputs, inputSize);
        Matrix expectedOutputs = MatrixBuilder.buildConstant(totalInputs, outputSize, 0.5);

        // Forward the inputs and get the actual inputs
        layer.updateLayer(inputs);
        Matrix actualOutput = layer.getOutMatrix();

        // Assert expected
        assertEquals(expectedOutputs, actualOutput);
    }

    // Weights all 1
    @RepeatedTest(TOTAL_REPS)
    void updateLayer02(RepetitionInfo repetitionInfo) {
        // Set the input and output sizes
        int inputSize = repetitionInfo.getCurrentRepetition();
        int outputSize = TOTAL_REPS - inputSize + 1;
        int totalInputs = 3;

        // Create the layer
        ActivationFunction sigmoid = this.af;
        DenseLayer layer = new DenseLayer(inputSize, outputSize, sigmoid);
        layer.setWeightMatrix(MatrixBuilder.buildConstant(inputSize, outputSize, 1));
        layer.setBiasWeightMatrix(MatrixBuilder.buildEmpty(1, outputSize));

        // Create the inputs and expected outputs
        Matrix inputs = Matrices.randomMatrix(totalInputs, inputSize);
        Vector summedRows = sigmoid.activation(Matrices.sumRows(inputs)).getColumnVector(0);
        MatrixBuilder matrixBuilder = new MatrixBuilder();

        for (int i = 0; i < outputSize; i++) {
            matrixBuilder.addColumn(summedRows);
        }

        Matrix expectedOutputs = matrixBuilder.build();

        // Forward the inputs and get the actual inputs
        layer.updateLayer(inputs);
        Matrix actualOutput = layer.getOutMatrix();

        // Assert expected
        assertEquals(expectedOutputs, actualOutput);
    }

    // Positive Bias
    @RepeatedTest(TOTAL_REPS)
    void updateLayer03(RepetitionInfo repetitionInfo) {
        // Set the input and output sizes
        int inputSize = repetitionInfo.getCurrentRepetition();
        int outputSize = TOTAL_REPS - inputSize + 1;
        int totalInputs = 3;

        // Create the input matrix
        Matrix inputs = Matrices.randomMatrix(totalInputs, inputSize);

        // Create the layers
        DenseLayer layerNoBias = new DenseLayer(inputSize, outputSize, this.af);
        layerNoBias.setBiasWeightMatrix(MatrixBuilder.buildEmpty(inputSize, outputSize));   // Remove the bias
        layerNoBias.updateLayer(inputs);

        DenseLayer layerWithBias = new DenseLayer(inputSize, outputSize, this.af);
        layerWithBias.setWeightMatrix(layerNoBias.getWeightMatrix()); // Copy the weight matrix from the previous layer
        layerWithBias.setBiasWeightMatrix(Matrices.randomMatrix(1, outputSize, 0.10, 1.00)); // Make an all positive bias matrix
        layerWithBias.updateLayer(inputs);

        // Assert that the layer with bias allways returns higher values than the one without it.
        layerNoBias.getOutMatrix().compLoop((row, col, value) -> {
            double valueNoBias = value;
            double valueWithBias = layerWithBias.getOutMatrix().getEntry(row, col);

            assertTrue(valueNoBias < valueWithBias);
        });
    }

    // Negative bias
    @RepeatedTest(TOTAL_REPS)
    void updateLayer04(RepetitionInfo repetitionInfo) {
        // Set the input and output sizes
        int inputSize = repetitionInfo.getCurrentRepetition();
        int outputSize = TOTAL_REPS - inputSize + 1;
        int totalInputs = 3;

        // Create the input matrix
        Matrix inputs = Matrices.randomMatrix(totalInputs, inputSize);

        // Create the layers
        DenseLayer layerNoBias = new DenseLayer(inputSize, outputSize, this.af);
        layerNoBias.setBiasWeightMatrix(MatrixBuilder.buildEmpty(inputSize, outputSize));   // Remove the bias
        layerNoBias.updateLayer(inputs);

        DenseLayer layerWithBias = new DenseLayer(inputSize, outputSize, this.af);
        layerWithBias.setWeightMatrix(layerNoBias.getWeightMatrix()); // Copy the weight matrix from the previous layer
        layerWithBias.setBiasWeightMatrix(Matrices.randomMatrix(1, outputSize, -1.00, -0.10)); // Make an all negative bias matrix
        layerWithBias.updateLayer(inputs);

        // Assert that the layer with bias allways returns higher values than the one without it.
        layerNoBias.getOutMatrix().compLoop((row, col, value) -> {
            double valueNoBias = value;
            double valueWithBias = layerWithBias.getOutMatrix().getEntry(row, col);

            assertTrue(valueNoBias > valueWithBias);
        });
    }

    // Tests that it's possible to get the activation function
    @Test
    void getAF() {
        DenseLayer denseLayer = new DenseLayer(10, 10, this.af);
        assertEquals(this.af, denseLayer.getAF());
    }

    // Tests that the net matrix is in fact null when the layer is just instantiated
    @Test
    void getNetMatrixNull() {
        DenseLayer denseLayer = new DenseLayer(10, 10, this.af);
        assertNull(denseLayer.getNetMatrix());
    }

    @Test
    void equalsAndHashCode() {
        DenseLayer denseLayerA = new DenseLayer(10, 10, this.af);
        denseLayerA.setBiasWeightMatrix(Matrices.getIdentityMatrix(3));
        denseLayerA.setWeightMatrix(Matrices.getIdentityMatrix(4));
        DenseLayer denseLayerB = new DenseLayer(10, 10, this.af);
        denseLayerB.setBiasWeightMatrix(Matrices.getIdentityMatrix(3));
        denseLayerB.setWeightMatrix(Matrices.getIdentityMatrix(4));
        assertEquals(denseLayerA, denseLayerB);
        assertEquals(denseLayerA.hashCode(), denseLayerB.hashCode());
    }
}