package koreniak.kingsluck.core.observable;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ObservableArrayConverter<T extends Observable<T>> extends StdConverter<ObservableArray<T>, ObservableArray<T>> {
    @Override
    public ObservableArray<T> convert(ObservableArray<T> value) {
        for (T t : value) {
            t.addObserver(value);
        }

        return value;
    }
}
