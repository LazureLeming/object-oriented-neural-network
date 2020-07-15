package fakes;

import mockit.Mock;
import mockit.MockUp;
import neuralnetwork.helpers.MathOperations;

public class FakeMathOperations extends MockUp<MathOperations> {

    @Mock double sigmoid(final double input) {
        return input;
    }

    @Mock double randomValueBetween(final double origin, final double bound) {
        return 1D;
    }

}
