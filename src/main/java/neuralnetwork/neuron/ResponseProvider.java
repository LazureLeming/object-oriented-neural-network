package neuralnetwork.neuron;

/**
 * Interface providing accessing neuron response functionality. <br>
 * <br>
 * Used for getting responses from neurons in previous layers of the network.
 *
 * @author Paweł Rutkowski S18277
 */
public interface ResponseProvider {

    /**
     * Get response of neuron.
     *
     * @return neuron response.
     */
    double getResponse();

}
