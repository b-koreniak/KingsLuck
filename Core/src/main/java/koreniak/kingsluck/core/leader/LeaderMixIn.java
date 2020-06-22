package koreniak.kingsluck.core.leader;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(converter = LeaderConverter.class)
public class LeaderMixIn {
}
