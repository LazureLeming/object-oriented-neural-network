package neuralnetwork.helpers;

import java.util.concurrent.ThreadLocalRandom;

public class MathematicalFunction {

    public static double sigmoid(final double input) {
        return 1 / (Math.exp(-input) + 1);
    }

    public static double randomValueBetween(final double origin, final double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

}
