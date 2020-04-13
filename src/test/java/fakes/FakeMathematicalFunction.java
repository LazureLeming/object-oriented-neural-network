package fakes;

import mockit.Mock;
import mockit.MockUp;
import neuralnetwork.helpers.MathematicalFunction;

public class FakeMathematicalFunction extends MockUp<MathematicalFunction> {

    @Mock double sigmoid(final double input) {
        return input;
    }

    @Mock double randomValueBetween(final double origin, final double bound) {
        return 1D;
    }

}
