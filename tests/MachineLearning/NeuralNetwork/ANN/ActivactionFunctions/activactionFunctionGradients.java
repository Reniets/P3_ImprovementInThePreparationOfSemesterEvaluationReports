package MachineLearning.NeuralNetwork.ANN.ActivactionFunctions;

import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class activactionFunctionGradients {

    // Tests:
    @Test
    public void hardSigmoidGrad(){
        ActivationFunction AF = new HardSigmoidActivation();

        Function<Double, Double> acti = AF::function;
        Function<Double, Double> prime = AF::functionPrime;

        assertTrue(this.checkNumGradient(acti, prime, AF.getMatrixPref()));
    }

    @Test
    public void identityGrad(){
        ActivationFunction AF = new IdentityActivaction();

        Function<Double, Double> acti = AF::function;
        Function<Double, Double> prime = AF::functionPrime;

        assertTrue(this.checkNumGradient(acti, prime, AF.getMatrixPref()));
    }

    @Test
    public void PReLUGrad(){
        ActivationFunction AF = new PReLUActivation();

        Function<Double, Double> acti = AF::function;
        Function<Double, Double> prime = AF::functionPrime;

        assertTrue(this.checkNumGradient(acti, prime, AF.getMatrixPref()));
    }

    @Test
    public void ReLUGrad(){
        ActivationFunction AF = new ReLUActivation();

        Function<Double, Double> acti = AF::function;
        Function<Double, Double> prime = AF::functionPrime;

        assertTrue(this.checkNumGradient(acti, prime, AF.getMatrixPref()));
    }

    @Test
    public void sigmoidGrad(){
        ActivationFunction AF = new SigmoidActivation();

        Function<Double, Double> acti = AF::function;
        Function<Double, Double> prime = AF::functionPrime;

        assertTrue(this.checkNumGradient(acti, prime, AF.getMatrixPref()));
    }

    @Test
    public void tanhGrad(){
        ActivationFunction AF = new TanhActivation();

        Function<Double, Double> acti = AF::function;
        Function<Double, Double> prime = AF::functionPrime;

        assertTrue(this.checkNumGradient(acti, prime, AF.getMatrixPref()));
    }

    private boolean checkNumGradient(Function<Double, Double> acti, Function<Double, Double> prime, MatrixPref pref){
        int steps = 1000;
        int start = -3;
        int end = 3;
        int range = end - start;

        double stepSize = 10e-6;
        double acceptableDiff = 10e-4;

        for (int i = 1; i < steps; i++) {
            double val = ((double) i / (double) steps) * range + start;

            // 0 is often an edge case, and often depends on the actual activation function.
            if (val == 0) {
                continue;
            }

            double prev = acti.apply(val - stepSize);
            double curr = acti.apply(val - stepSize);
            double next = acti.apply(val + stepSize);

            double numGrad = (next - prev) / (2 * stepSize);

            double actiGradInput = pref.equals(MatrixPref.NET) ? val : curr;
            double actiGrad = prime.apply(actiGradInput);

            double diff = Math.abs(numGrad - actiGrad);

            boolean accepted = acceptableDiff >= diff;

            if (!accepted) {
                return false;
            }
        }
        return true;
    }

}