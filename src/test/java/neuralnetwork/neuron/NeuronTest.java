package neuralnetwork.neuron;

import fakes.FakeMap;
import fakes.FakeMathOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NeuronTest {

    private static final double RESPONSE = 2;
    private static final double BIAS = 1;
    private static final double ERROR = 2;
    private Neuron neuron;
    private List<ResponseProvider> previousNeurons = List.of(mock(ResponseProvider.class),
            mock(ResponseProvider.class));
    private List<Neuron> nextNeurons = List.of(mock(Neuron.class), mock(Neuron.class));
    private final double expectedResponse = BIAS + (previousNeurons.size() * RESPONSE);

    @BeforeEach void prepareNeuron() {
        new FakeMathOperations();
        neuron = new Neuron();
        previousNeurons.forEach(mock -> when(mock.getResponse()).thenReturn(RESPONSE));
        nextNeurons.forEach(mock -> when(mock.getScaledError(any())).thenReturn(ERROR));
        neuron.configurePreviousNeurons(previousNeurons);
        neuron.configureNextNeurons(nextNeurons);
    }

    @Test void testCalculateResponse() {
        neuron.calculateResponse();
        final double response = neuron.getResponse();
        assertEquals(expectedResponse, response);
    }

    @Test void testCalculateError() {
        final double expectedError = (ERROR * nextNeurons.size()) * expectedResponse * (1 - expectedResponse);
        neuron.calculateResponse();
        neuron.calculateError();
        final double calculatedError = neuron.getError();
        assertEquals(expectedError, calculatedError);
    }

    @Test void testAdjustWeights() {
        final FakeMap<ResponseProvider, Double> map = new FakeMap<>();
        neuron.adjustWeights(0);
        assertEquals(previousNeurons.size(), map.timeReplaceCalled());
    }

    @Test void testScaledError() {
        neuron.setError(ERROR);
        final double scaledError = neuron.getScaledError(previousNeurons.stream().findFirst().orElseThrow());
        assertEquals(ERROR, scaledError);
    }

}
