package neuralnetwork;

import neuralnetwork.helpers.*;

import java.io.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;

/**
 * Helper class for training and using specific Neural Network.
 *
 * @author Paweł Rutkowski S18277
 * @see NeuralNetwork
 */
public class NeuralNetworkTrainer implements Serializable {

    private static final long serialVersionUID = -355239184450356980L;
    private final Map<List<Double>, List<Double>> trainingMap;
    private final Map<List<Double>, List<Double>> testingMap;
    private NeuralNetwork neuralNetwork;
    private NeuralNetwork savedNeuralNetwork;
    private int numberOfIterations;
    private double learningRate;

    /**
     * Constructor. Saves all relevant information required for network testing.
     * Most values can be also modified afterwards using setter methods.
     *
     * @param neuralNetwork      Neural Network to test.
     * @param trainingMap        map containing input vectors and expected values
     *                           used for training.
     * @param testingMap         map containing input vectors and expected values
     *                           used for testing.
     * @param numberOfIterations number of training iterations.
     * @param learningRate       learning rate used in testing.
     */
    public NeuralNetworkTrainer(final NeuralNetwork neuralNetwork, final Map<List<Double>, List<Double>> trainingMap,
            final Map<List<Double>, List<Double>> testingMap, final int numberOfIterations, final double learningRate) {
        this.neuralNetwork = neuralNetwork;
        this.trainingMap = trainingMap;
        this.testingMap = testingMap;
        this.numberOfIterations = numberOfIterations;
        this.learningRate = learningRate;
        this.savedNeuralNetwork = deepCopySerializableObject(neuralNetwork);
    }

    /**
     * Method reading Neural Network object from file with provided filename.
     *
     * @param filename name of file storing Neural Network
     * @throws IOException            I/O error occurred when opening/reading file
     *                                with provided filename.
     * @throws ClassNotFoundException Class of a serialized object cannot be found.
     */
    public void readNeuralNetworkFromFile(final String filename) throws IOException, ClassNotFoundException {
        final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename));
        this.neuralNetwork = (NeuralNetwork) objectInputStream.readObject();
        objectInputStream.close();
    }

    /**
     * Method saving used Neural Network to file with provided filename.
     *
     * @param filename name of file to store Neural Network in.
     * @throws IOException I/O error occurred when opening/reading file with
     *                     provided filename.
     */
    public void saveNeuralNetworkToFile(final String filename) throws IOException {
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename));
        objectOutputStream.writeObject(neuralNetwork);
        objectOutputStream.close();
    }

    /**
     * Setter for number of learning iterations.
     *
     * @param numberOfIterations number of learning iterations.
     */
    public void setNumberOfIterations(final int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    /**
     * Setter for learning rate.
     *
     * @param learningRate learning rate to use in training.
     */
    public void setLearningRate(final double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Method testing Neural Network on provided testing map.
     *
     * @return list of entries containing incorrectly classifying responses as keyes
     *         with expected values as values
     */
    public List<Entry<List<Double>, Integer>> test() {
        return testingMap.entrySet().stream().map(entry -> {
            final List<Double> calculatedResponse = neuralNetwork.calculateResponse(entry.getKey());
            final int calculatedValue = valueFromVector(calculatedResponse);
            final int expectedValue = valueFromVector(entry.getValue());
            if (calculatedValue == expectedValue) {
                return null;
            } else {
                return new SimpleImmutableEntry<>(calculatedResponse, expectedValue);
            }
        }).filter(Objects::nonNull).collect(toList());
    }

    /**
     * Method used for training provided Neural Network for specified number of
     * iterations with specified learning rate. Uses provided training map.
     *
     * @return list of root mean square errors (RMSE) from all iterations.
     */
    public List<Double> train() {
        return trainAndReturnRmseStream().peek(rmse -> System.out.println()).collect(toList());
    }

    /**
     * Method used for training provided Neural Network for specified number of
     * iterations with specified learning rate. Uses provided training map. On each
     * iteration tests network on provided testing set. Useful for plotting RMSE and
     * accuracy per learning iteration.
     */
    public void trainAndTestOnEachIteration() {
        trainAndReturnRmseStream().forEach(i -> System.out.println(
                String.format("\t\tACCURACY=%.5f", (testingMapSize() - test().size()) / (double) testingMapSize())));
    }

    private Stream<Double> trainAndReturnRmseStream() {
        final List<List<Double>> inputs = new ArrayList<>(trainingMap.keySet());
        return IntStream.rangeClosed(1, numberOfIterations)
                .peek(i -> System.out.print(String.format("ITERATION=%d\t\tLEARNING-RATE=%f", i, learningRate)))
                .mapToObj(i -> inputs.stream()
                        .mapToDouble(input -> neuralNetwork.train(input, trainingMap.get(input), learningRate).stream()
                                .mapToDouble(MathOperations::squareValue).sum())
                        .sum() / 2D)
                .peek(i -> shuffle(inputs)).peek(rmse -> System.out.print(String.format("\t\tRMSE=%.3f", rmse)));
    }

    /**
     * Method returning testing map size. Can be useful for calculating network
     * accuracy as ratio.
     *
     * @return testing map size.
     */
    public int testingMapSize() {
        return testingMap.size();
    }

    /**
     * Saves current state of Neural Network for backup. Network is automatically
     * backed up in constructor and in setter.
     */
    public void saveNeuralNetwork() {
        savedNeuralNetwork = deepCopySerializableObject(neuralNetwork);
    }

    /**
     * Restores previously saved Neural Network.
     */
    public void restoreNeuralNetwork() {
        neuralNetwork = deepCopySerializableObject(savedNeuralNetwork);
    }

    /**
     * Setter for Neural Network. Creates backup of this new network.
     *
     * @param neuralNetwork Neural Network to test.
     */
    public void setNeuralNetwork(final NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        savedNeuralNetwork = deepCopySerializableObject(neuralNetwork);
    }

    /**
     * Getter for tested Neural Network.
     *
     * @return tested Neural Network.
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    private static int valueFromVector(final List<Double> vector) {
        return vector.indexOf(Collections.max(vector));
    }

    private static <T extends Serializable> T deepCopySerializableObject(final T source) {
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(source);
            objectOutputStream.flush();
            objectOutputStream.close();
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    byteArrayOutputStream.toByteArray());
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInputStream.readObject();
        } catch (final IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
                if (objectInputStream != null)
                    objectInputStream.close();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
