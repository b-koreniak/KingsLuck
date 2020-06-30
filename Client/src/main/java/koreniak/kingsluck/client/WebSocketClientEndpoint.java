package koreniak.kingsluck.client;

import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageDecoder;
import koreniak.kingsluck.core.message.MessageEncoder;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.net.WebSocketSessionImpl;
import koreniak.kingsluck.core.observer.Observer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ClientEndpoint(encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WebSocketClientEndpoint implements ClientI {
    private static final Logger LOGGER = LogManager.getLogger("WebSocketClientEndpoint");

    private String playerNickname;
    private SessionI session;

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        notifyObservers(message);
    }

    @OnClose
    public void onClose(CloseReason closeReason, Session session) {
        notifyObservers(new Message(MessageType.CONNECTION_LOST));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        notifyObservers(new Message(MessageType.CONNECTION_LOST));
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public SessionI getSession() {
        return session;
    }

    @Override
    public void connect(Message connectionMessage) {
        ClientManager clientManager = new ClientManager();

        try {
            session = new WebSocketSessionImpl(clientManager.connectToServer(this, new URI("")));
            sendMessage(connectionMessage);
        } catch (DeploymentException | IOException | URISyntaxException e) {
            LOGGER.error("connect exception", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.error("disconnect exception", e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            session.sendMessage(message);
        } catch (IOException | EncodeException e) {
            LOGGER.error("sendMessage exception", e);
        }
    }

    private List<Observer<Message>> observers = new CopyOnWriteArrayList<>();

    @Override
    public void addObserver(Observer<Message> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Message> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
    }

    @Override
    public void notifyObservers(Message object) {
        for (Observer<Message> observer : observers) {
            observer.update(object);
        }
    }
}
