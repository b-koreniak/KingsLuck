package koreniak.kingsluck.client.desktop.screen.battlefield;

import koreniak.kingsluck.client.ClientFactory;
import koreniak.kingsluck.client.ClientI;
import koreniak.kingsluck.client.desktop.screen.ParentScreen;
import koreniak.kingsluck.client.desktop.screen.ScreenController;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.observer.Observer;

public class BattlefieldScreenController implements Observer<Message>, ScreenController {
    private ClientI client = ClientFactory.getInstance().getDefaultClient();

    private ParentScreen parentScreen;

    @Override
    public void update(Message observable) {

    }

    @Override
    public void setParentScreen(ParentScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
}
