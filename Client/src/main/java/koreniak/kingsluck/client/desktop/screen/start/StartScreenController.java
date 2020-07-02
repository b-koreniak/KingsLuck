package koreniak.kingsluck.client.desktop.screen.start;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;
import koreniak.kingsluck.client.ClientFactory;
import koreniak.kingsluck.client.ClientI;
import koreniak.kingsluck.client.desktop.screen.ParentScreen;
import koreniak.kingsluck.client.desktop.screen.ScreenController;
import koreniak.kingsluck.client.desktop.screen.ScreenType;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.observer.Observer;

public class StartScreenController implements Observer<Message>, ScreenController {
    private ClientI client = ClientFactory.getInstance().getDefaultClient();

    private ParentScreen parentScreen;

    @FXML
    private Button buttonFastGame;
    @FXML
    private Button buttonExit;

    @FXML
    public void initialize() {
        buttonFastGame.setOnAction(event -> {
            client.connect(new Message(MessageType.CONNECTION_REQUEST));

            parentScreen.setDisable(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15000), onFinishedEvent -> {
                parentScreen.setDisable(false);
            }));
            timeline.play();
        });

        buttonExit.setOnAction(event -> {

        });
    }

    @Override
    public void update(Message observable) {
        switch (observable.getType()) {
            case CONNECTION_ESTABLISHED: {
                Platform.runLater(() -> {
                    parentScreen.setDisable(false);
                    parentScreen.loadScreen(ScreenType.WAIT);
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
