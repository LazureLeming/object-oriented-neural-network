package neuralnetwork.helpers;

import fakes.FakeConsumer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListOperationsTest {

    @Test void testCreateListOfObjects() {
        final int listSize = 10;
        final List<Character> list = ListOperations.createListOfObjects(listSize, () -> 'A');
        assertEquals(listSize, list.size());
        assertTrue(list.stream().allMatch(character -> character.equals('A')));
    }

    @Test void testRunNeuralOperationConcurrently() {
        final FakeConsumer<Integer> consumer = new FakeConsumer<>();
        final List<Integer> list = List.of(1, 2, 3, 4, 5);
        ListOperations.runNeuralOperationConcurrently(list, consumer);
        assertEquals(list.size(), consumer.timesAcceptCalled());
    }

}
