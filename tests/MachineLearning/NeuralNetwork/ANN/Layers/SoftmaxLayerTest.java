package MachineLearning.NeuralNetwork.ANN.Layers;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.Trainer.Costs.CostFunction;
import MachineLearning.NeuralNetwork.Trainer.Costs.MSECost;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoftmaxLayerTest {

    // Tests:
    @Test
    void backpropagate01() {
        CostFunction mse = new MSECost();
        Layer softmax = new SoftmaxLayer(2);

        Matrix net = new MatrixBuilder(new double[][]{
                {0.2, 0.5}
        }, true).build();

        softmax.updateLayer(net);
        Matrix out = softmax.getOutMatrix();

        Matrix target = new MatrixBuilder(new double[][]{
                {0, 1}
        }, true).build();

        Matrix error = mse.costPrime(target, out);

        Matrix deri = softmax.backpropagate(null, error);

        Matrix expected = new MatrixBuilder()
                .addRow(0.2080621278, -0.2080621278)    // Derived numbers from Maple
                .build();

        expected.compLoop((row, col, value) -> assertEquals(value, deri.getEntry(row, col), 10e-5));
    }

    @RepeatedTest(5)
    void backpropagate02(RepetitionInfo repetitionInfo) {
        int cols = repetitionInfo.getCurrentRepetition();
        double epsilon = 10e-5;
        CostFunction mse = new MSECost();
        Layer softMax = new SoftmaxLayer(cols);

        Matrix targets = new MatrixBuilder().setEntry(0, cols - 1, 1).build();

        Matrix inputs = Matrices.randomMatrix(1, cols);

        for (int i = 0; i < cols; i++) {
            Matrix inputs_neg = new MatrixBuilder(inputs, true).additiontoEntry(0, i, -epsilon).build();
            Matrix inputs_pos = new MatrixBuilder(inputs, true).additiontoEntry(0, i, epsilon).build();

            // Negative error
            softMax.updateLayer(inputs_neg);
            double error_neg = Matrices.sumRows(mse.cost(targets, softMax.getOutMatrix())).getEntry(0, 0);

            // Positive error
            softMax.updateLayer(inputs_pos);
            double error_pos = Matrices.sumRows(mse.cost(targets, softMax.getOutMatrix())).getEntry(0, 0);

            // Gradients
            softMax.updateLayer(inputs);
            double numGrad = (error_pos - error_neg) / (2 * epsilon);
            double deri = softMax.backpropagate(null, mse.costPrime(targets, softMax.getOutMatrix())).getEntry(0, i);

            assertEquals(numGrad, deri, epsilon);
        }
    }

    @Test
    void hashCodeTest() {
        SoftmaxLayer softmaxLayerA = new SoftmaxLayer(2);
        SoftmaxLayer softmaxLayerB = new SoftmaxLayer(2);
        assertEquals(softmaxLayerA.hashCode(), softmaxLayerB.hashCode());
    }
}