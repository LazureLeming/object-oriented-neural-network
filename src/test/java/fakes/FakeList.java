package fakes;

import mockit.Mock;
import mockit.MockUp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FakeList<T> extends MockUp<ArrayList<T>> {

    private int timesForEachCalled = 0;

    @Mock public void forEach(Consumer<? super T> action) {
        timesForEachCalled++;
    }

    public int timesForEachCalled() {
        return timesForEachCalled;
    }
 }
