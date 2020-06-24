package koreniak.kingsluck.server;

import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageDecoder;
import koreniak.kingsluck.core.message.MessageEncoder;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.net.WebSocketSessionImpl;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/server", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WebSocketServerEndpoint {
    private static final Map<String, SessionI> sessionMap = new ConcurrentHashMap<>();

    private Server server = new Server();

    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), new WebSocketSessionImpl(session));
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        server.handleMessage(sessionMap.get(session.getId()), message);
    }

    @OnClose
    public void onClose(CloseReason closeReason, Session session) {
        server.handleSessionClose(sessionMap.get(session.getId()));
        sessionMap.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        server.handleSessionClose(sessionMap.get(session.getId()));
        sessionMap.remove(session.getId());
    }
}
