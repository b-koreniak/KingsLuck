package koreniak.kingsluck.core.net;

import koreniak.kingsluck.core.message.Message;

import javax.websocket.EncodeException;
import java.io.IOException;

public interface SessionI {
    void sendMessage(Message message) throws IOException, EncodeException;

    void addProperty(Object key, Object value);

    Object getProperty(Object key);

    void removeProperty(Object key);

    boolean isConnected();

    void close() throws IOException;
}
