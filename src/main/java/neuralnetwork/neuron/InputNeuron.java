package neuralnetwork.neuron;

import java.io.Serializable;

/**
 * Pseudo-neuron acting as input to the network. Keeps only one input which is
 * passed without any alteration to further layers.
 */
public class InputNeuron implements ResponseProvider, Serializable {

    private static final long serialVersionUID = 5984890068623287736L;
    private double input;

    /**
     * Configures specific network input value.
     *
     * @param input value for this specific network input
     */
    public void configureInput(final double input) {
        this.input = input;
    }

    /**
     * Returns pseudo-response by passing input to further network layers.
     *
     * @return one of the inputs without any alterations
     */
    @Override
    public double getResponse() {
        return input;
    }

}
