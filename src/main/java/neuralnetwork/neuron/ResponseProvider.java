package neuralnetwork.neuron;

/**
 * Interface providing accessing neuron response functionality. Used for getting
 * responsed from neurons in previous layers of the network.
 */
public interface ResponseProvider {

    /**
     * Get response of neuron.
     *
     * @return neuron response.
     */
    double getResponse();

}
