package koreniak.kingsluck.client.desktop.screen.battlefield;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import koreniak.kingsluck.client.ClientFactory;
import koreniak.kingsluck.client.ClientI;
import koreniak.kingsluck.client.desktop.screen.ParentScreen;
import koreniak.kingsluck.client.desktop.screen.ScreenController;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.observer.Observer;
import koreniak.kingsluck.core.unit.Unit;

import java.util.Queue;

public class BattlefieldScreenController implements Observer<Message>, ScreenController {
    private ClientI client = ClientFactory.getInstance().getDefaultClient();

    private ParentScreen parentScreen;

    private Leader playerLeader;
    private Leader opponentLeader;

    private Table playerTable;
    private Table opponentTable;

    @FXML
    private ToggleButton buttonTopDeckUnit;

    @FXML
    private GridPane gridPanePlayerField;
    @FXML
    private Label labelPlayerDeckUnitsAmount;
    @FXML
    private Label labelPlayerRoundsWon;
    @FXML
    private Label labelPlayerStatus;
    @FXML
    private Label labelPlayerTurnsPlayed;
    @FXML
    private Label labelPlayerTotalScore;

    @FXML
    private GridPane gridPaneOpponentField;
    @FXML
    private Label labelOpponentDeckUnitsAmount;
    @FXML
    private Label labelOpponentRoundsWon;
    @FXML
    private Label labelOpponentStatus;
    @FXML
    private Label labelOpponentTurnsPlayed;
    @FXML
    private Label labelOpponentTotalScore;

    public BattlefieldScreenController() {
    }

    public BattlefieldScreenController(Leader playerLeader, Leader opponentLeader) {
        this.playerLeader = playerLeader;
        this.opponentLeader = opponentLeader;
    }

    @FXML
    public void initialize() {
        playerTable.leader = playerLeader;
        playerTable.gridPaneField = gridPanePlayerField;
        playerTable.labelDeckUnitsAmount = labelPlayerDeckUnitsAmount;
        playerTable.labelRoundsWon = labelPlayerRoundsWon;
        playerTable.labelStatus = labelPlayerStatus;
        playerTable.labelTurnsPlayed = labelPlayerTurnsPlayed;
        playerTable.labelTotalScore = labelPlayerTotalScore;

        opponentTable.leader = opponentLeader;
        opponentTable.gridPaneField = gridPaneOpponentField;
        opponentTable.labelDeckUnitsAmount = labelOpponentDeckUnitsAmount;
        opponentTable.labelRoundsWon = labelOpponentRoundsWon;
        opponentTable.labelStatus = labelOpponentStatus;
        opponentTable.labelTurnsPlayed = labelOpponentTurnsPlayed;
        opponentTable.labelTotalScore = labelOpponentTotalScore;

        if (playerLeader.getDeckUnits().peek() != null) {
            buttonTopDeckUnit.setText(playerLeader.getDeckUnits().peek().toString());
        } else {
            buttonTopDeckUnit.setText("No units");
            buttonTopDeckUnit.setDisable(true);
        }

        for (Node child : gridPanePlayerField.getChildren()) {
            child.setOnMouseClicked(event -> {
                if (playerLeader.isActionable()) {
                    int row = GridPane.getRowIndex(child);
                    int column = GridPane.getColumnIndex(child);

                    if (buttonTopDeckUnit.isSelected() && playerLeader.getField().get(row, column) == null) {
                        Unit unit = playerLeader.getDeckUnits().peek();

                        Object[] objects = {row, column, unit};

                        client.sendMessage(new Message(objects, MessageType.PUT_UNIT_ON_FIELD_REQUEST));
                        parentScreen.setDisable(true);
                    }
                }
            });
        }
    }

    @Override
    public void update(Message message) {
        switch (message.getType()) {
            case MESSAGE_QUEUE: {
                Queue<Message> messages = (Queue<Message>) message.getObject();

                while (!messages.isEmpty()) {
                    handleMessage(messages.poll());
                }
                break;
            }
        }
    }

    public void handleMessage(Message message) {
        switch (message.getType()) {
            case PUT_UNIT_ON_FIELD_RESPONSE: {
                Object[] objects = (Object[]) message.getObject();

                Leader leader = (Leader) objects[0];
                int row = (int) objects[1];
                int column = (int) objects[2];

                Unit unit = (Unit) objects[3];

                if (playerLeader.getUniqueId() == leader.getUniqueId()) {
                    playerTable.putUnitOnField(row, column, unit);
                } else if (opponentLeader.getUniqueId() == leader.getUniqueId()) {
                    opponentTable.putUnitOnField(row, column, unit);
                }

                if (playerLeader.getDeckUnits().peek() != null) {
                    buttonTopDeckUnit.setText(playerLeader.getDeckUnits().peek().toString());
                } else {
                    buttonTopDeckUnit.setText("No units");
                    buttonTopDeckUnit.setDisable(true);
                }

                playerTable.refresh();
                opponentTable.refresh();

                parentScreen.setDisable(false);

                break;
            }
        }
    }

    @Override
    public void setParentScreen(ParentScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    private class Table {
        private Leader leader;

        private GridPane gridPaneField;

        private Label labelDeckUnitsAmount;

        private Label labelRoundsWon;
        private Label labelStatus;
        private Label labelTurnsPlayed;
        private Label labelTotalScore;

        public void refresh() {
            for (Unit unit : leader.getField()) {

            }

            labelDeckUnitsAmount.setText(String.valueOf(leader.getDeckUnits().size()));
            labelRoundsWon.setText(String.valueOf(leader.getRoundsWon()));

            if (leader.isActionable()) {
                labelStatus.setText("Actionable");
            } else {
                labelStatus.setText("NotActionable");
            }

            if (leader.isSkippedRound()) {
                labelStatus.setText("SkippedRound");
            }

            labelTurnsPlayed.setText(String.valueOf(leader.getTurnsPlayed()));
            labelTotalScore.setText(String.valueOf(leader.getEfficiency().getCurrentValue()));
        }

        public void putUnitOnField(int row, int column, Unit unit) {
            Label label = new Label(unit.toString());
            label.setWrapText(true);

            gridPaneField.add(label, column, row);
        }
    }
}
