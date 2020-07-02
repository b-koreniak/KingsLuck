package koreniak.kingsluck.client.desktop.screen;

public class ScreenFactory {
    public static String getScreenResource(ScreenType screenType) {
        switch (screenType) {
            case START: {
                return "/koreniak/kingsluck/client/desktop/screen/start/startscreen.fxml";
            }
            case WAIT: {
                return "/koreniak/kingsluck/client/desktop/screen/wait/waitscreen.fxml";
            }
            case BATTLEFIELD: {
                return "/koreniak/kingsluck/client/desktop/screen/battlefield/batllefieldscreen.fxml";
            }
            default: {
                return "/koreniak/kingsluck/client/desktop/screen/start/startscreen.fxml";
            }
        }
    }
}
