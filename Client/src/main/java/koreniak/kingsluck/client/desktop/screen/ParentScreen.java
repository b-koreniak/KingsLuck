package koreniak.kingsluck.client.desktop.screen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ParentScreen extends StackPane {
    public void loadScreen(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent parent = loader.load();

        ScreenController screenController = loader.getController();
        screenController.setParentScreen(this);

        getChildren().clear();
        getChildren().add(parent);
    }

    public void loadScreen(ScreenType type) throws IOException {
        loadScreen(ScreenFactory.getScreenResource(type));
    }
}
