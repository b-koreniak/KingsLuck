package koreniak.kingsluck.core.observable;

import koreniak.kingsluck.core.observer.Observer;

public interface Observable<T> {
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);

    void notifyObservers();
    void notifyObservers(T object);
}
