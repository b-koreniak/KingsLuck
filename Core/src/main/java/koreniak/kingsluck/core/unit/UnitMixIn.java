package koreniak.kingsluck.core.unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties("observers")
@JsonDeserialize(converter = UnitConverter.class)
public class UnitMixIn {
}
