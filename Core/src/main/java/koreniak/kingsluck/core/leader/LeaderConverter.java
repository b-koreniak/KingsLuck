package koreniak.kingsluck.core.leader;

import com.fasterxml.jackson.databind.util.StdConverter;

public class LeaderConverter extends StdConverter<Leader, Leader> {
    @Override
    public Leader convert(Leader value) {
        value.getField().addObserver(value);

        return value;
    }
}
