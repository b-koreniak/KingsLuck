package koreniak.kingsluck.server;

import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.server.manager.GameManager;
import koreniak.kingsluck.server.manager.GameState;

public class Room {
    private GameManager gameManager;

    public Room(Leader activeLeader, Leader inactiveLeader) {
        gameManager = new GameManager(activeLeader, inactiveLeader);
    }

    public void putUnitOnField(int row, int column, Unit unit) {
        gameManager.putUnitOnField(row, column, unit);
        gameManager.transferTurn();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            handleGameState();
        }
    }

    public void duelEnemyUnit(int targetRow, int targetColumn, Unit attacker) {
        gameManager.duel(targetRow, targetColumn, attacker);
        gameManager.transferTurn();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            handleGameState();
        }
    }

    public void skipRound() {
        gameManager.skipRound();
        gameManager.transferTurn();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            handleGameState();
        }
    }

    private void setRoundsWon() {
        Leader winner = gameManager.getRoundWinner();

        if (winner != null) {
            winner.setRoundsWon(winner.getRoundsWon() + 1);
        } else {
            gameManager.getActiveLeader().setRoundsWon(gameManager.getActiveLeader().getRoundsWon() + 1);
            gameManager.getInactiveLeader().setRoundsWon(gameManager.getInactiveLeader().getRoundsWon() + 1);
        }
    }

    private void handleGameState() {
        GameState gameState = gameManager.getGameState();

        switch (gameState.getState()) {
            case CONTINUES: {
                break;
            }
            case VICTORY: {
                break;
            }
            case DRAW: {
                break;
            }
        }
    }
}
