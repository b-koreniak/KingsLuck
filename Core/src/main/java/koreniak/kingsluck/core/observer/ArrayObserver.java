package koreniak.kingsluck.core.observer;

import koreniak.kingsluck.core.observable.Observable;

public interface ArrayObserver<T extends Observable> {
    void updateOnAdd(T observable);
    void updateOnRemove(T observable);
    void updateOnChange(T observable);
}
