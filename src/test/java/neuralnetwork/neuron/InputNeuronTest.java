package neuralnetwork.neuron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputNeuronTest {

    @Test void testConfigureInputAndGetResponse() {
        final double expectedResponse = 123D;
        final InputNeuron inputNeuron = new InputNeuron();
        inputNeuron.configureInput(expectedResponse);
        final double neuronResponse = inputNeuron.getResponse();
        assertEquals(expectedResponse, neuronResponse);
    }

}
