package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

public class ActivationFunctions {

    // Private constructor to avoid initilization.
    private ActivationFunctions() {
    }

    public static ActivationFunction getByID(int ID) {
        switch (ID) {
            case 0:
                return new SigmoidActivation();
            case 1:
                return new TanhActivation();
            case 2:
                return new ReLUActivation();
            case 3:
                return new IdentityActivaction();
            case 4:
                return new PReLUActivation();
            case 5:
                return new HardSigmoidActivation();

            // Break is not required since we call return, in every case.
            default:
                throw new IllegalArgumentException("No activation function with this ID");
        }
    }

}
