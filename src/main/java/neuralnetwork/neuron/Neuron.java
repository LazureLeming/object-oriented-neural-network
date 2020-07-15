package neuralnetwork.neuron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import neuralnetwork.helpers.MathOperations;

/**
 * Basic neuron in neural network, used in hidden layers. <br>
 * <br>
 * Keeps information about previous neurons with weights assigned to their
 * connections. Keeps information about next neurons for backpropagating errors.
 *
 * @author Paweł Rutkowski S18277
 * @see nai.neuralnetwork.NeuralNetwork
 */
public class Neuron implements ResponseProvider, Serializable {

    private static final long serialVersionUID = -6752725139822282832L;
    private static final double INITIAL_WEIGHT_ORIGIN = -0.1;
    private static final double INITIAL_WEIGHT_BOUND = 0.1;
    private final Map<ResponseProvider, Double> previousNeurons = new LinkedHashMap<>();
    private final List<Neuron> nextNeurons = new ArrayList<>();
    private double bias = MathOperations.randomValueBetween(INITIAL_WEIGHT_ORIGIN, INITIAL_WEIGHT_BOUND);
    private transient double response = 0D;
    private transient double error = 0D;

    /**
     * Configures map of previous errors based on passed list of neurons setting
     * their initial weights to random value between values defined by
     * {@link Neuron#INITIAL_WEIGHT_BOUND} and {@link Neuron#INITIAL_WEIGHT_ORIGIN}.
     *
     * @param neurons list of neurons in previous layer of the network
     */
    public void configurePreviousNeurons(final List<ResponseProvider> neurons) {
        neurons.forEach(
                neuron -> previousNeurons.put(neuron, MathOperations.randomValueBetween(INITIAL_WEIGHT_ORIGIN, INITIAL_WEIGHT_BOUND)));
    }

    /**
     * Configures list of neurons in next layer of the network.
     *
     * @param neurons list of neurons in next layer of the network
     */
    public void configureNextNeurons(final List<Neuron> neurons) {
        nextNeurons.clear();
        nextNeurons.addAll(neurons);
    }

    /**
     * Calculates this neuron's response based on responses from neurons in previous
     * layer, weights associated to them, bias and sigmoid activation function.
     * Calculated response is assigned to {@link Neuron#response} field.
     */
    public void calculateResponse() {
        final double response = previousNeurons.entrySet().stream()
                .map(entry -> entry.getKey().getResponse() * entry.getValue()).mapToDouble(Double::valueOf).sum();
        this.response = MathOperations.sigmoid(response + bias);
    }

    /**
     * Returns calculated response. Calculating the response is happening in
     * different method for easier calculations on multiple threads.
     *
     * @return calculated response.
     */
    @Override
    public double getResponse() {
        return response;
    }

    /**
     * Calculates this neuron's error based on errors from neurons in next layer and
     * weights associated with their connections.
     */
    public void calculateError() {
        final double errorFromNextLayer = nextNeurons.stream().map(neuron -> neuron.getScaledError(this))
                .mapToDouble(Double::valueOf).sum();
        this.error = errorFromNextLayer * response * (1D - response);
    }

    /**
     * Adjusts weights for each connection based on calculated error, previous
     * neuron response and learning rate.
     *
     * @param learningRate learning rate used for training.
     */
    public void adjustWeights(final double learningRate) {
        previousNeurons.forEach((neuron, weight) -> previousNeurons.replace(neuron,
                weight + (error * neuron.getResponse() * learningRate)));
        bias += error * learningRate;
    }

    /**
     * Method returning calculated error.
     *
     * @return calculated error.
     */
    public double getError() {
        return error;
    }

    /**
     * Method calculating scaled error for given Neuron. Calculated by multiplying
     * weight connecting given neuron and error.
     *
     * @param neuron neuron for which weight should be used in calculations
     * @return scaled error for given neuron.
     */
    double getScaledError(final ResponseProvider neuron) {
        return previousNeurons.get(neuron) * error;
    }

    /**
     * Error setter.
     *
     * @param error error value to set as error field.
     */
    void setError(final double error) {
        this.error = error;
    }

}
