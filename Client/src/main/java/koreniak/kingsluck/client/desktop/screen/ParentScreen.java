package koreniak.kingsluck.client.desktop.screen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ParentScreen extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger("ParentScreen");

    public void loadScreen(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent parent = loader.load();

        ScreenController screenController = loader.getController();
        screenController.setParentScreen(this);

        getChildren().clear();
        getChildren().add(parent);
    }

    public void loadScreen(ScreenType type) {
        try {
            loadScreen(ScreenFactory.getScreenResource(type));
        } catch (IOException e) {
            LOGGER.error("loadScreen exception", e);
        }
    }
}
