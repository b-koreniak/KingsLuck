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

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    private ObjectMapper mapper = new ObjectMapper();

    public MessageDecoder() {
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
    public Message decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, Message.class);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
