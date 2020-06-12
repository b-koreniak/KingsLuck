package koreniak.kingsluck.core.observer;

import koreniak.kingsluck.core.observable.Observable;

public interface Observer<T> {
    void update(T observable);
}
