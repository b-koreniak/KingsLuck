package koreniak.kingsluck.core;

import koreniak.kingsluck.core.observable.Observable;
import koreniak.kingsluck.core.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Attribute implements Observable<Attribute> {
    private int minValue;
    private int maxValue;

    private int baseValue;

    private int currentValue;

    public Attribute() {
        maxValue = Integer.MAX_VALUE;
    }

    public Attribute(int baseValue) {
        this();
        this.baseValue = baseValue;
        currentValue = baseValue;
    }

    private void inc() {
        if (currentValue < maxValue) {
            currentValue++;
        }
    }

    private void dec() {
        if (currentValue > minValue) {
            currentValue--;
        }
    }

    public void inc(int value) {
        for (int i = 0; i < value; i++) {
            inc();
        }

        notifyObservers();
    }

    public void dec(int value) {
        for (int i = 0; i < value; i++) {
            dec();
        }

        notifyObservers();
    }

    public void modify(int modifier) {
        if (modifier > 0) {
            inc(modifier);
        } else {
            dec(Math.abs(modifier));
        }

        notifyObservers();
    }

    private List<Observer<Attribute>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<Attribute> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Attribute> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Attribute> observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void notifyObservers(Attribute object) {

    }

    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    public String toString() {
        return String.valueOf(currentValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return currentValue == attribute.currentValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentValue);
    }
}
