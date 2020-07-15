package neuralnetwork.helpers;

import java.util.concurrent.ThreadLocalRandom;

public class MathOperations {

    public static double sigmoid(final double input) {
        return 1 / (Math.exp(-input) + 1);
    }

    public static double sigmoidDerivative(final double input) {
        return sigmoid(input) * (1D - sigmoid(input));
    }

    public static double randomValueBetween(final double origin, final double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static double squareValue(final double valueToSquare) {
        return valueToSquare * valueToSquare;
    }

}
