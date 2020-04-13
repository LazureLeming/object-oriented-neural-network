package neuralnetwork.neuron;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class OutputNeuronTest {

    private OutputNeuron outputNeuron;

    @BeforeEach void prepareOutputNeuron() {
        outputNeuron = spy(OutputNeuron.class);
    }

    @Test void testCalculateError() {
        final double response = 10;
        when(outputNeuron.getResponse()).thenReturn(response);
        final double expectedValue = 1;
        outputNeuron.setExpectedResult(expectedValue);
        outputNeuron.calculateError();
        final double calculatedError = outputNeuron.getError();
        assertEquals(expectedValue - response, calculatedError);
    }

    @Test void testGetAbsoluteError() {
        final double negativeValue = -1;
        when(outputNeuron.getError()).thenReturn(negativeValue);
        final double absoluteError = outputNeuron.getAbsoluteError();
        assertEquals(-negativeValue, absoluteError);
    }

}
