package neuralnetwork.helpers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;

public class ListOperations {

    public static <T> List<T> createListOfObjects(final int numberOfElements, final Supplier<T> objectSupplier) {
        return Stream.generate(objectSupplier).limit(numberOfElements).collect(toList());
    }

    public static <T> void runNeuralOperationConcurrently(final List<T> objects, final Consumer<T> operation) {
        objects.parallelStream().map(neuron -> runAsync(() -> operation.accept(neuron)))
                .forEach(CompletableFuture::join);
    }

}
