package MachineLearning.NeuralNetwork.ANN.Layers;

import DataStructures.Bytes;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.SigmoidActivation;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.TanhActivation;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class layerByteRepresentation {

    @Test
    void getByteRepresentation01() {
        // Create layer 1
        DenseLayer layer1 = new DenseLayer(10,13, new SigmoidActivation());

        // Create layer 2 which is a duplicate of layer 1
        DenseLayer layer2 = new DenseLayer(10,13, new SigmoidActivation());

        layer2.setWeightMatrix(layer1.getWeightMatrix());
        layer2.setBiasWeightMatrix(layer1.getBiasWeightMatrix());

        // Assert
        assertEquals(layer1.getByteRepresentation(), layer2.getByteRepresentation());
    }

    @Test // (These is VERY LOW chance that this test will fail.)
    void getByteRepresentation02() {
        // Create layer 1
        Layer layer1 = new DenseLayer(10,13, new SigmoidActivation());

        // Create layer 2 which is a NOT duplicate of layer 1
        Layer layer2 = new DenseLayer(10,13, new SigmoidActivation());

        // Assert
        assertNotEquals(layer1.getByteRepresentation(), layer2.getByteRepresentation());
    }

    @Test
    void bytesToLayer01() {
        Layer layerOriginal = new DenseLayer(13, 10, new SigmoidActivation());

        ByteBuffer byteBuffer = Bytes.listToByteBuffer(layerOriginal.getByteRepresentation());

        Layer layerImport = Layer.createLayerFromBytes(byteBuffer);

        assertEquals(layerOriginal, layerImport);
    }

    @Test
    void bytesToLayer02() {
        Layer layerOriginal = new DenseLayer(10, 13, new TanhActivation());

        ByteBuffer byteBuffer = Bytes.listToByteBuffer(layerOriginal.getByteRepresentation());

        Layer layerImport = Layer.createLayerFromBytes(byteBuffer);

        assertEquals(layerOriginal, layerImport);
    }

    @Test
    void bytesToLayer03() {
        Layer layerOriginal = new LstmLayer(5, 7, 10);

        ByteBuffer byteBuffer = Bytes.listToByteBuffer(layerOriginal.getByteRepresentation());

        Layer layerImport = Layer.createLayerFromBytes(byteBuffer);

        assertEquals(layerOriginal, layerImport);
    }

    @Test
    void bytesToLayer04() {
        Layer layerOriginal = new SoftmaxLayer(10);

        ByteBuffer byteBuffer = Bytes.listToByteBuffer(layerOriginal.getByteRepresentation());

        Layer layerImport = Layer.createLayerFromBytes(byteBuffer);

        assertEquals(layerOriginal, layerImport);
    }
}