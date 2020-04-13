package fakes;

import mockit.Mock;
import mockit.MockUp;

import java.util.HashMap;

public class FakeMap<K, V> extends MockUp<HashMap<K, V>> {

    private int timesReplaceCalled = 0;

    @Mock public V replace(final K key, final V value) {
        ++timesReplaceCalled;
        return null;
    }

    public double timeReplaceCalled() {
        return timesReplaceCalled;
    }

}
