package MachineLearning.NeuralNetwork.ANN;

import DataStructures.Bytes;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.SigmoidActivation;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.TanhActivation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ANNImport_export {

    // Tests that a network can be exported and imported and be equal
    @Test
    void import_export_ANN() {
        ANN network = new ANNBuilder(7)
                .addDenseLayer(10, new SigmoidActivation())
                .addLstmLayer(3, 2)
                .addDenseLayer(7, new TanhActivation())
                .addSoftmaxLayer(7)
                .build();

        List<Byte> byteRepresentation = network.getByteRepresentation();

        ANN networkImport = new ANN(1).ANNFromByteRepresentation(Bytes.listToByteBuffer(byteRepresentation));

        assertEquals(network, networkImport);
    }
}