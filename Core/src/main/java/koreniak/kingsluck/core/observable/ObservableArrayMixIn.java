package koreniak.kingsluck.core.observable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties("arrayObserverList")
@JsonDeserialize(converter = ObservableArrayConverter.class)
public class ObservableArrayMixIn {
}
