package neuralnetwork;

import fakes.FakeNeuron;
import fakes.FakeOutputNeuron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NeuralNetworkTest {

    private static final int NUMBER_OF_INPUTS = 10;
    private static final int NUMBER_OF_OUTPUTS = 3;
    private static final int[] NUMBER_OF_HIDDEN_NEURONS = { 5, 5 };
    private static final int NUMBER_OF_NEURONS = Arrays.stream(NUMBER_OF_HIDDEN_NEURONS).sum() + NUMBER_OF_OUTPUTS;
    private final List<Double> doubles = IntStream.range(0, NUMBER_OF_INPUTS).mapToObj(Double::valueOf)
            .collect(toList());
    private NeuralNetwork network;
    private FakeNeuron fakeNeuron;

    @BeforeEach void initializeNetwork() {
        fakeNeuron = new FakeNeuron();
        network = new NeuralNetwork(NUMBER_OF_INPUTS, NUMBER_OF_OUTPUTS, NUMBER_OF_HIDDEN_NEURONS);
    }

    @Test void testCalculateResponse() {
        network.calculateResponse(doubles);
        final int timesCalculateResponseCalled = fakeNeuron.timesCalculateResponseCalled();
        assertEquals(NUMBER_OF_NEURONS, timesCalculateResponseCalled);
    }

    @Test void testTrain() {
        final FakeOutputNeuron outputNeuron = new FakeOutputNeuron();
        network.train(doubles, IntStream.range(0, NUMBER_OF_OUTPUTS).mapToObj(Double::valueOf).collect(toList()));
        final int timesCalculateErrorCalledInHiddenLayer = fakeNeuron.timesCalculateErrorCalled();
        final int timesCalculateErrorCalledInOutputLayer = outputNeuron.timesCalculateErrorCalled();
        assertEquals(NUMBER_OF_NEURONS,
                timesCalculateErrorCalledInHiddenLayer + timesCalculateErrorCalledInOutputLayer);
        final int timesAdjustWeightCalled = fakeNeuron.timesAdjustWeightsCalled();
        assertEquals(NUMBER_OF_NEURONS, timesAdjustWeightCalled);
    }

    @Test void testCalculateResponseWithWrongSizeThrowsIllegalArgumentException() {
        final List<Double> incorrectInputSizeList = IntStream.range(0, NUMBER_OF_INPUTS - 1).mapToObj(Double::valueOf)
                .collect(toList());
        assertThrows(IllegalArgumentException.class, () -> network.calculateResponse(incorrectInputSizeList));
    }

    @Test void testTrainWithWrongSizeThrowsIllegalArgumentException() {
        final List<Double> incorrectInputSizeList = IntStream.range(0, NUMBER_OF_OUTPUTS - 1).mapToObj(Double::valueOf)
                .collect(toList());
        assertThrows(IllegalArgumentException.class, () -> network.train(doubles, incorrectInputSizeList));
    }

}
