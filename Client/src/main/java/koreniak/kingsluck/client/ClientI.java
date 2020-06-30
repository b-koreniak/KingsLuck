package koreniak.kingsluck.client;

import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.observable.Observable;

public interface ClientI extends Observable<Message> {
    String getPlayerNickname();

    SessionI getSession();

    void connect(Message connectionMessage);
    void disconnect();

    void sendMessage(Message message);
}
