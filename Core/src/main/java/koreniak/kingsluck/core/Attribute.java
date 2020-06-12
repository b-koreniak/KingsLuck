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

    private void incAttribute(int value) {
        for (int i = 0; i < value; i++) {
            inc();
        }
    }

    private void decAttribute(int value) {
        for (int i = 0; i < value; i++) {
            dec();
        }
    }

    public void modifyAttribute(int modifier) {
        if (modifier > 0) {
            incAttribute(modifier);
        } else {
            decAttribute(modifier);
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
