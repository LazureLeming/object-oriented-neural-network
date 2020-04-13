package fakes;

import mockit.Mock;
import mockit.MockUp;
import neuralnetwork.neuron.OutputNeuron;

public class FakeOutputNeuron extends MockUp<OutputNeuron> {

    private int timesCalculateErrorCalled = 0;

    @Mock public synchronized void calculateError() {
        ++timesCalculateErrorCalled;
    }

    public int timesCalculateErrorCalled() {
        return timesCalculateErrorCalled;
    }

}
