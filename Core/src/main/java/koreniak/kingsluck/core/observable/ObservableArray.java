package koreniak.kingsluck.core.observable;

import koreniak.kingsluck.core.observer.ArrayObserver;
import koreniak.kingsluck.core.observer.Observer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ObservableArray<T extends Observable<T>> implements Observer<T> {
    private int rows;
    private int columns;

    private T[][] array;

    public ObservableArray(int rows, int columns, Class<T> tClass) {
        this.rows = rows;
        this.columns = columns;

        array = (T[][]) Array.newInstance(tClass, rows, columns);
    }

    public T get(int row, int column) {
        return array[row][column];
    }

    public void add(int row, int column, T object) {
        array[row][column] = object;
        object.addObserver(this);

        notifyOnAdd(object);
    }

    public void remove(int row, int column) {
        T object = array[row][column];
        object.removeObserver(this);

        array[row][column] = null;

        notifyOnRemove(object);
    }

    public boolean isFilled() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (array[i][j] == null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (array[i][j] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<ArrayObserver<T>> arrayObserverList = new ArrayList<>();

    public void addObserver(ArrayObserver<T> observer) {
        arrayObserverList.add(observer);
    }

    public void removeObserver(ArrayObserver<T> observer) {
        arrayObserverList.remove(observer);
    }

    public void notifyOnAdd(T object) {
        for (ArrayObserver<T> observer : arrayObserverList) {
            observer.updateOnAdd(object);
        }
    }

    public void notifyOnRemove(T object) {
        for (ArrayObserver<T> observer : arrayObserverList) {
            observer.updateOnRemove(object);
        }
    }

    public void notifyOnChange(T object) {
        for (ArrayObserver<T> observer : arrayObserverList) {
            observer.updateOnChange(object);
        }
    }

    @Override
    public void update(T observable) {
        notifyOnChange(observable);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public T[][] getArray() {
        return array;
    }
}
