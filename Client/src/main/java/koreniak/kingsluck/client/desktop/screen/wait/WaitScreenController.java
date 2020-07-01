package koreniak.kingsluck.client.desktop.screen.wait;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import koreniak.kingsluck.client.ClientFactory;
import koreniak.kingsluck.client.ClientI;
import koreniak.kingsluck.client.desktop.screen.ParentScreen;
import koreniak.kingsluck.client.desktop.screen.ScreenController;
import koreniak.kingsluck.client.desktop.screen.ScreenType;
import koreniak.kingsluck.client.desktop.screen.battlefield.BattlefieldScreenController;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.observer.Observer;

public class WaitScreenController implements Observer<Message>, ScreenController {
    private ClientI client = ClientFactory.getInstance().getDefaultClient();

    private ParentScreen parentScreen;

    @FXML
    private Button buttonCancel;

    @FXML
    public void initialize() {
        buttonCancel.setOnAction(event -> {

        });

        client.sendMessage(new Message(MessageType.START_GAME_REQUEST));
    }

    @Override
    public void update(Message observable) {
        switch (observable.getType()) {
            case START_GAME_RESPONSE: {
                Object[] leaders = (Object[]) observable.getObject();

                Leader playerLeader = (Leader) leaders[0];
                Leader opponentLeader = (Leader) leaders[1];

                Platform.runLater(() -> {
                    parentScreen.loadScreen(ScreenType.BATTLEFIELD, new BattlefieldScreenController(playerLeader, opponentLeader));
                });
                break;
            }
        }
    }

    @Override
    public void setParentScreen(ParentScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
}
