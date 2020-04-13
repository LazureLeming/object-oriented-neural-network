package fakes;

import java.util.function.Consumer;

public class FakeConsumer<T> implements Consumer<T> {

    private int timesAcceptCalled = 0;

    @Override
    public synchronized void accept(T t) {
        ++timesAcceptCalled;
    }

    public int timesAcceptCalled() {
        return timesAcceptCalled;
    }

}
