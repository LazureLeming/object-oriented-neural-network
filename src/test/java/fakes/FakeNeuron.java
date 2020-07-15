package fakes;

import mockit.Mock;
import mockit.MockUp;
import neuralnetwork.neuron.Neuron;

public class FakeNeuron extends MockUp<Neuron> {

    private int timesCalculateResponseCalled = 0;
    private int timesCalculateErrorCalled = 0;
    private int timesAdjustWeightsCalled = 0;
    private double learningRate = 0;

    @Mock public synchronized void calculateResponse() {
        ++timesCalculateResponseCalled;
    }

    @Mock public synchronized void calculateError() {
        ++timesCalculateErrorCalled;
    }

    @Mock public synchronized void adjustWeights(final double learningRate) {
        ++timesAdjustWeightsCalled;
        this.learningRate = learningRate;
    }

    public int timesCalculateResponseCalled() {
        return timesCalculateResponseCalled;
    }

    public int timesCalculateErrorCalled() {
        return timesCalculateErrorCalled;
    }

    public int timesAdjustWeightsCalled() {
        return timesAdjustWeightsCalled;
    }

    public double learningRate() {
        return learningRate;
    }

}
