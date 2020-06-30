package koreniak.kingsluck.server;

import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageDecoder;
import koreniak.kingsluck.core.message.MessageEncoder;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.net.WebSocketSessionImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/server", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WebSocketServerEndpoint {
    private static final Logger LOGGER = LogManager.getLogger("WebSocketServerEndpoint");

    private static final Map<String, SessionI> sessionMap = new ConcurrentHashMap<>();

    private static final Server server = new Server();

    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), new WebSocketSessionImpl(session));
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        try {
            server.handleMessage(sessionMap.get(session.getId()), message);
        } catch (IOException | EncodeException e) {
            LOGGER.error("onMessage handling exception", e);
        }
    }

    @OnClose
    public void onClose(CloseReason closeReason, Session session) {
        try {
            server.handleSessionClose(sessionMap.get(session.getId()));
        } catch (IOException | EncodeException e) {
            LOGGER.error("onClose handling exception", e);
        }

        sessionMap.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            server.handleSessionClose(sessionMap.get(session.getId()));
        } catch (IOException | EncodeException e) {
            LOGGER.error("onError handling exception", e);
        }

        LOGGER.error("onError exception", throwable);

        sessionMap.remove(session.getId());
    }
}
