package MachineLearning.NeuralNetwork.ANN;

import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.SigmoidActivation;
import MachineLearning.NeuralNetwork.ANN.Layers.Layer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ANNBuilderTest {

    private ANN networkA;
    private ANN networkB;
    private ANNBuilder annBuilder;

    private static final int SIZE = 5;

    @BeforeEach
    void beforeEach() {
        this.networkA = new ANN(SIZE, SIZE, 0, 0, new SigmoidActivation());
        this.networkB = new ANN(SIZE, SIZE, 0, 0, new SigmoidActivation());
        this.annBuilder = new ANNBuilder(SIZE);
    }

    @Test
    void addDenseLayer() {
    }

    @Test
    void addSoftmaxLayer() {
    }

    @Test
    void addLstmLayer() {
    }

    @Test
    void concatNetwork() {
        ANN resultANN = annBuilder
                .concatNetwork(networkA)
                .concatNetwork(networkB)
                .build();

        int sizeA = networkA.getLayers().size();
        Layer layerA = networkA.getLayers().get(0);

        Layer layerB = networkB.getLayers().get(0);

        assertEquals(layerA, resultANN.getLayers().get(0));
        assertEquals(layerB, resultANN.getLayers().get(sizeA));
    }

    @Test
    void build() {
    }
}