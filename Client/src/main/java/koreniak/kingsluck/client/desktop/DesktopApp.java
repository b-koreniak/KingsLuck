package koreniak.kingsluck.client.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import koreniak.kingsluck.client.desktop.screen.ParentScreen;
import koreniak.kingsluck.client.desktop.screen.ScreenType;

public class DesktopApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ParentScreen parentScreen = new ParentScreen();

        parentScreen.loadScreen(ScreenType.START);

        Scene scene = new Scene(parentScreen);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
