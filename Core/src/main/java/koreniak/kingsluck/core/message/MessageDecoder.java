package koreniak.kingsluck.core.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    private ObjectMapper mapper = new ObjectMapper();

    public MessageDecoder() {
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
