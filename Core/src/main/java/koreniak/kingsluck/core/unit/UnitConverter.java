package koreniak.kingsluck.core.unit;

import com.fasterxml.jackson.databind.util.StdConverter;

public class UnitConverter extends StdConverter<Unit, Unit> {
    @Override
    public Unit convert(Unit value) {
        value.getEfficiency().addObserver(value);

        return value;
    }
}
