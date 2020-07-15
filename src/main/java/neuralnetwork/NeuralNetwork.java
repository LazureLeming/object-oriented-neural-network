package neuralnetwork;

import neuralnetwork.neuron.InputNeuron;
import neuralnetwork.neuron.Neuron;
import neuralnetwork.neuron.OutputNeuron;
import neuralnetwork.neuron.ResponseProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Object-oriented Neural Network.
 *
 * @author Paweł Rutkowski S18277
 * @see Neuron
 * @see InputNeuron
 * @see OutputNeuron
 */
public class NeuralNetwork implements Serializable {

    private static final long serialVersionUID = -5423205492506068915L;
    private final List<List<Neuron>> neurons = new ArrayList<>();
    private final List<InputNeuron> inputLayer = new ArrayList<>();
    private final List<OutputNeuron> outputLayer = new ArrayList<>();

    /**
     * Only constructor. Creates and connects all neurons together.
     *
     * @param numberOfInputs                number of network inputs
     * @param numberOfOutputs               number of network outputs
     * @param numberOfNeuronsPerHiddenLayer array of numbers of neurons in each
     *                                      hidden layer
     */
    public NeuralNetwork(final int numberOfInputs, final int numberOfOutputs,
            final int... numberOfNeuronsPerHiddenLayer) {
        initializeInputLayer(numberOfInputs);
        initializeHiddenLayers(numberOfNeuronsPerHiddenLayer);
        initializeOutputLayer(numberOfOutputs);
        connectNeurons();
    }

    /**
     * Calculate network response based on given inputs. Inputs size has to match
     * network inputs size, otherwise {@link IllegalArgumentException} is thrown.
     * Calculations for each layer are done concurrently using
     * {@link CompletableFuture#runAsync(Runnable)} to speed-up processing time.
     *
     * @param inputs list consisting of input values as doubles (input vector)
     * @return list consisting of responses of each output layer neuron (output
     *         vector)
     */
    public List<Double> calculateResponse(final List<Double> inputs) {
        setInputLayerValues(inputs);
        neurons.forEach(neuralLayer -> neuralLayer.forEach(Neuron::calculateResponse));
        return outputLayer.stream().map(Neuron::getResponse).collect(toList());
    }

    /**
     * Method allowing neural network to be trained based on inputs, expected values
     * and passed learning rate of neurons. Inputs size has to match network inputs
     * size, otherwise {@link IllegalArgumentException} is thrown. Expected values
     * list size has to match network outputs size, otherwise
     * {@link IllegalArgumentException} is thrown.
     *
     * @param inputs         list consisting of input values as doubles (input
     *                       vector)
     * @param expectedValues list consisting of expected outputs for each output
     *                       neuron (expected response vector)
     * @param learningRate   rate at which neurons adjust their weights - before
     *                       adjustments error is multiplied by this value
     * @return list of errors from output layer
     */
    public List<Double> train(final List<Double> inputs, final List<Double> expectedValues, final double learningRate) {
        setExpectedResponses(expectedValues);
        calculateResponse(inputs);
        backpropagateErrorsAndAdjustWeights(learningRate);
        return outputLayer.stream().map(Neuron::getError).collect(Collectors.toList());
    }

    private void initializeInputLayer(final int numberOfInputs) {
        inputLayer.clear();
        inputLayer.addAll(createListOfObjects(numberOfInputs, InputNeuron::new));
    }

    private void initializeHiddenLayers(final int... numberOfNeuronsPerLayer) {
        Arrays.stream(numberOfNeuronsPerLayer)
                .forEach(numberOfNeurons -> neurons.add(createListOfObjects(numberOfNeurons, Neuron::new)));
    }

    private void initializeOutputLayer(final int numberOfOutputs) {
        outputLayer.clear();
        outputLayer.addAll(createListOfObjects(numberOfOutputs, OutputNeuron::new));
        neurons.add(outputLayer.stream().map(Neuron.class::cast).collect(toList()));
    }

    private void connectNeurons() {
        connectInputToHiddenLayer();
        connectHiddenAndOutputLayers();
    }

    private void connectInputToHiddenLayer() {
        final List<Neuron> firstNeuralLayer = neurons.stream().findFirst().orElseThrow();
        firstNeuralLayer.forEach(neuron -> neuron
                .configurePreviousNeurons(inputLayer.stream().map(ResponseProvider.class::cast).collect(toList())));
    }

    private void connectHiddenAndOutputLayers() {
        connectPreviousNeurons();
        connectNextNeurons();
    }

    private void connectPreviousNeurons() {
        for (int i = 1; i < neurons.size(); ++i) {
            final List<Neuron> neuralLayer = neurons.get(i);
            final List<ResponseProvider> previousLayer = neurons.get(i - 1).stream().map(ResponseProvider.class::cast)
                    .collect(toList());
            neuralLayer.forEach(neuron -> neuron.configurePreviousNeurons(previousLayer));
        }
    }

    private void connectNextNeurons() {
        for (int i = neurons.size() - 2; i >= 0; --i) {
            final List<Neuron> neuralLayer = neurons.get(i);
            final List<Neuron> previousLayer = neurons.get(i + 1);
            neuralLayer.forEach(neuron -> neuron.configureNextNeurons(previousLayer));
        }
    }

    private void setInputLayerValues(final List<Double> inputs) {
        if (inputLayer.size() != inputs.size()) {
            throw new IllegalArgumentException("Number of network inputs and passed number of inputs doesn't match!");
        }
        for (int i = 0; i < inputLayer.size(); ++i) {
            inputLayer.get(i).configureInput(inputs.get(i));
        }
    }

    private void setExpectedResponses(final List<Double> expectedValues) {
        if (outputLayer.size() != expectedValues.size()) {
            throw new IllegalArgumentException("Expected values size doesn't match output layer size");
        }
        for (int i = 0; i < expectedValues.size(); ++i) {
            outputLayer.get(i).setExpectedResult(expectedValues.get(i));
        }
    }

    private void backpropagateErrorsAndAdjustWeights(final double learningRate) {
        Collections.reverse(neurons);
        neurons.forEach(neuralLayer -> neuralLayer.forEach(Neuron::calculateError));
        neurons.forEach(neuralLayer -> neuralLayer.forEach(neuron -> neuron.adjustWeights(learningRate)));
        Collections.reverse(neurons);
    }

    private static <T> List<T> createListOfObjects(final int numberOfElements, final Supplier<T> objectSupplier) {
        return Stream.generate(objectSupplier).limit(numberOfElements).collect(toList());
    }

}
