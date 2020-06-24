package koreniak.kingsluck.core.net;

import koreniak.kingsluck.core.message.Message;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public class WebSocketSessionImpl implements SessionI {
    private Session session;

    public WebSocketSessionImpl(Session session) {
        this.session = session;

        addProperty(SessionPropertyType.SESSION_ID, session.getId());
    }

    @Override
    public void sendMessage(Message message) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(message);
    }

    @Override
    public void addProperty(Object key, Object value) {
        session.getUserProperties().put(String.valueOf(key), value);
    }

    @Override
    public Object getProperty(Object key) {
        return session.getUserProperties().get(key);
    }

    @Override
    public void removeProperty(Object key) {
        session.getUserProperties().remove(key);
    }

    @Override
    public boolean isConnected() {
        return session.isOpen();
    }

    @Override
    public void close() throws IOException {
        session.close();
    }
}
