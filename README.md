# Object – Oriented Neural Network (in Java)
Does it make sense to use Neural Network written in Java using OOP paradigm?

Probably not, since it's slower and more complicated as Java is not supporting matrix based calculations out of the box.

**But here it is!** Fully working object-oriented Neural Network in Java which can be configured, trained and used however you like.

**Note**: This project was created just after my first introductory lecture on Neural Networks so it is a bit rough around the edges, but it works! 

## Getting started

### Prerequisites
At least [JDK 10](https://openjdk.java.net/).

Unit tests use [JUnit 5](https://junit.org/junit5/), [Mockito](https://site.mockito.org/) and [JMockit](https://jmockit.github.io/). They can be easily downloaded using gradle.

### Installing
Project can be built using [Gradle](https://gradle.org/).
Having Gradle installed just run `gradle clean build` in project's directory. All dependencies required for unit tests will be automatically downloaded.

Built library JAR file can be found in `build/libs/ObjectOrientedNeuralNetwork.jar`.

Alternatively you could just copy `src/main/java/neuralnetwork` package to your project, it will work too.

### Documentation

You can create JavaDoc by running `gradle javadoc`. Docs will become available in `build/docs/javadoc/index.html`. 


## How to use it?

### Creating the network
You can specify number of input and output neurons in constructor, as well as number of neurons in each hidden layer.
```java
int numberOfInputs = 400;
int numberOfOutputs = 10;
int[] numberOfNeuronsPerHiddenLayer = {50, 50}; // constructor uses varargs, so you can use multiple arguments
NeuralNetwork network = new NeuralNetwork(numberOfInputs, numberOfOutputs, numberOfNeuronsPerHiddenLayer);
```

### Using the network
Network result can be calculated using `NeuralNetwork.calculateResponse(List<Double> inputs)` method.

It's taking `List<Double>` of inputs as argument and returns `List<Double>` containing responses from each output neuron.

### Training the network
Network can be trained using `NeuralNetwork.train(List<Double> inputs, List<Double> expectedValues)` method.

Used arguments are `List<Double>` containing inputs for the network and `List<Double>` with expected values for output neurons.

Method returns `List<Double>` with calculated errors for each output neuron.

### Saving the network
Everything concerning the network implements [Serializable interface](https://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html),
so entire network can be easily saved to file and later imported using [Object Streams](https://docs.oracle.com/javase/tutorial/essential/io/objectstreams.html). 


## Author
* **Paweł Rutkowski** - *the entire thing* - [LinkedIn](https://linkedin.com/in/pawe%C5%82-rutkowski-611401124)