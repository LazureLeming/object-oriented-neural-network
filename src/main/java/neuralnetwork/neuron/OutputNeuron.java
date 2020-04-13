package neuralnetwork.neuron;

/**
 * Neuron residing on the output layer of the network. Keeps information about
 * expected response of this specific neuron. Calculates network error
 * differently than neurons in hidden layers.
 */
public class OutputNeuron extends Neuron {

    private static final long serialVersionUID = -8056174036956143699L;
    private transient double expectedResponse;

    /**
     * Sets expected response of this neuron.
     *
     * @param expectedResponse expected response of this neuron
     */
    public void setExpectedResult(final double expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    /**
     * Calculates and sets error based of expected response and achieved one instead
     * of errors from next neurons since there are no any.
     */
    @Override
    public void calculateError() {
        setError(expectedResponse - getResponse());
    }

    /**
     * Calculate absolute value of this neuron achieved error and returns it.
     *
     * @return absolute value of this neuron error.
     */
    public double getAbsoluteError() {
        return Math.abs(getError());
    }

}
