package koreniak.kingsluck.client.desktop;

import koreniak.kingsluck.client.ClientFactory;
import koreniak.kingsluck.client.ClientI;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.observer.Observer;

public class StartMenuController implements Observer<Message> {
    private ClientI client = ClientFactory.getInstance().getDefaultClient();

    @Override
    public void update(Message observable) {

    }
}
