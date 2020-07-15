package neuralnetwork.helpers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

import static neuralnetwork.helpers.MathOperations.randomValueBetween;
import static neuralnetwork.helpers.MathOperations.sigmoid;
import static neuralnetwork.helpers.MathOperations.squareValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathOperationsTest {

    @Test void testSigmoid() {
        final double resultAccuracy = 0.0001;
        assertEquals(0.7310, sigmoid(1), resultAccuracy);
        assertEquals(0.9933, sigmoid(5), resultAccuracy);
        assertEquals(0.9999, sigmoid(10), resultAccuracy);
        assertEquals(0.0474, sigmoid(-3), resultAccuracy);
        assertEquals(0.5, sigmoid(0));
    }

    @Test void testRandomValueBetween() {
        final Collection<Double> randomValues = new ArrayList<>();
        final double lowerBound = 5;
        final double upperBound = 15;
        IntStream.range(0, 100).forEach(number -> randomValues.add(randomValueBetween(lowerBound, upperBound)));
        assertTrue(Collections.min(randomValues) >= lowerBound);
        assertTrue(Collections.max(randomValues) <= upperBound);
    }

    @Test void testSquareValue() {
        final double resultAccuracy = 0.0000001;
        assertEquals(4, squareValue(2), resultAccuracy);
        assertEquals(9, squareValue(3), resultAccuracy);
        assertEquals(16, squareValue(4), resultAccuracy);
        assertEquals(11.4921, squareValue(3.39), resultAccuracy);
        assertEquals(123.965956, squareValue(11.134), resultAccuracy);
        assertEquals(7.38905609893, squareValue(Math.E), resultAccuracy);
    }

}
