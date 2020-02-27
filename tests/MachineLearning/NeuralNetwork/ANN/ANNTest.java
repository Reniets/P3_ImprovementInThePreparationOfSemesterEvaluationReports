package MachineLearning.NeuralNetwork.ANN;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.SigmoidActivation;
import MachineLearning.NeuralNetwork.Trainer.Costs.MSECost;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.DefaultData;
import MachineLearning.NeuralNetwork.Trainer.Trainer;
import MachineLearning.NeuralNetwork.Trainer.TrainingMethods.MiniBatch;
import MachineLearning.NeuralNetwork.Trainer.TrainingMethods.TrainingMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ANNTest {

    // Test Constants
    private static final Matrix INPUT = new MatrixBuilder()
            .addRow(0, 0)
            .addRow(0, 1)
            .addRow(1, 0)
            .addRow(1, 1)
            .build();

    private static final Matrix AND_GATE = new MatrixBuilder()
            .addRow(0)
            .addRow(0)
            .addRow(0)
            .addRow(1)
            .build();

    private static final Matrix OR_GATE = new MatrixBuilder()
            .addRow(0)
            .addRow(1)
            .addRow(1)
            .addRow(1)
            .build();

    private static final Matrix XOR_GATE = new MatrixBuilder()
            .addRow(0)
            .addRow(1)
            .addRow(1)
            .addRow(0)
            .build();

    // Tests:
    @Test
    void isItLearning_AndGate() {
        trainAndVerifyGate(AND_GATE);
    }

    @Test
    void isItLearning_OrGate() {
        trainAndVerifyGate(OR_GATE);
    }

    @Test
    void isItLearning_xorGate() {
        trainAndVerifyGate(XOR_GATE);
    }

    private void trainAndVerifyGate(Matrix gate) {
        ANN network = new ANN(2, 1, 1, 2, new SigmoidActivation());
        TrainingMethod miniBatch = new MiniBatch(1, 2, new MSECost());
        Trainer trainer = new Trainer(network, miniBatch);

        trainer.startTraining(new DefaultData(INPUT), new DefaultData(gate), 7500);
        Matrix predictions = network.evaluateInputs(new DefaultData(INPUT));

        gate.compLoop((row, col, value) -> assertEquals(value, predictions.getEntry(row, col), 0.1));
    }

}