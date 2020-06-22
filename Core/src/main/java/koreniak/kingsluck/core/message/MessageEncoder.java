package koreniak.kingsluck.core.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.AttributeMixIn;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.leader.LeaderMixIn;
import koreniak.kingsluck.core.observable.ObservableArray;
import koreniak.kingsluck.core.observable.ObservableArrayMixIn;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitMixIn;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    private ObjectMapper mapper = new ObjectMapper();

    public MessageEncoder() {
        mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.addMixIn(ObservableArray.class, ObservableArrayMixIn.class);
        mapper.addMixIn(Attribute.class, AttributeMixIn.class);
        mapper.addMixIn(Unit.class, UnitMixIn.class);
        mapper.addMixIn(Leader.class, LeaderMixIn.class);
        mapper.addMixIn(Message.class, MessageMixIn.class);
    }

    @Override
    public String encode(Message object) throws EncodeException {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EncodeException(object, e.getMessage(), e);
        }
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
