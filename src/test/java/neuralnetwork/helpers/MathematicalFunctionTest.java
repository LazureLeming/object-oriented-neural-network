package neuralnetwork.helpers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

import static neuralnetwork.helpers.MathematicalFunction.randomValueBetween;
import static neuralnetwork.helpers.MathematicalFunction.sigmoid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathematicalFunctionTest {

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
}
