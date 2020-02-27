package MachineLearning.NeuralNetwork.ANN.Layers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LstmLayerTest {

    // Test Fields:
    private static LstmLayer lstmLayer;

    // Test Constants
    private static final int UNROLL_LENGTH = 10;
    private static final int NODES_IN_PREV_LAYER = 10;
    private static final int NODES_IN_THIS_LAYER = 10;

    @BeforeAll
    static void beforeAll() {
        lstmLayer = new LstmLayer(UNROLL_LENGTH, NODES_IN_PREV_LAYER, NODES_IN_THIS_LAYER);
    }

    // Tests:
    @Test
    void updateLayer() {

    }

    @Test
    void clear() {

    }

    @Test
    void backpropagate() {
    }

    @Test
    void updateWeights() {
    }

    @Test
    void getOutMatrix() {

    }

    @Test
    void getNetMatrix() {
    }

    @Test
    void getNodesInLayer() {
        assertEquals(NODES_IN_THIS_LAYER, lstmLayer.getNodesInLayer());
    }

    @Test
    void saveCurrentBackpropagate01() {
    }

    @Test
    void getByteRepresentation() {
    }

    @Test
    void bytesToLayer() {
    }

    @Test
    void equals() {
    }

    @Test
    void hashCodeTest() {
    }
}