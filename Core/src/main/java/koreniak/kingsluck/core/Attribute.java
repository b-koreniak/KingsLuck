package koreniak.kingsluck.core;

import java.util.Objects;

public class Attribute {
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
    }

    public void dec(int value) {
        for (int i = 0; i < value; i++) {
            dec();
        }
    }

    public void modify(int modifier) {
        if (modifier > 0) {
            inc(modifier);
        } else {
            dec(modifier);
        }
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
