package koreniak.kingsluck.server;

import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.server.manager.GameManager;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.*;

public class Room {
    private Map.Entry<String, SessionI> playerSessionFirst;
    private Map.Entry<String, SessionI> playerSessionSecond;

    private GameManager gameManager;

    public void addPlayer(String playerNickname, SessionI sessionI) {
        if (playerSessionFirst == null) {
            playerSessionFirst = new AbstractMap.SimpleEntry<>(playerNickname, sessionI);
        } else if (playerSessionSecond == null) {
            playerSessionSecond = new AbstractMap.SimpleEntry<>(playerNickname, sessionI);
        }
    }

    public boolean isFilled() {
        return playerSessionFirst != null && playerSessionSecond != null;
    }

    public void initializeGameManager(Leader activeLeader, Leader inactiveLeader) {
        gameManager = new GameManager(activeLeader, inactiveLeader);
    }

    public Message getPutUnitOnFieldMessage(int row, int column, Unit unit) {
        gameManager.putUnitOnField(row, column, unit);
        gameManager.transferTurn();

        Queue<Message> messages = new ArrayDeque<>();

        Object[] objects = new Object[3];
        objects[0] = row;
        objects[1] = column;
        objects[2] = unit;
        messages.add(new Message(objects, MessageType.PUT_UNIT_ON_FIELD));

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            addGameStateMessage(messages);
        }

        messages.add(new Message(gameManager.getActiveLeader(), MessageType.LEADER_INFO));
        messages.add(new Message(gameManager.getInactiveLeader(), MessageType.LEADER_INFO));

        return new Message(messages, MessageType.MESSAGE_QUEUE);
    }

    public Message getDuelEnemyUnitMessage(int targetRow, int targetColumn, Unit attacker) {
        gameManager.duel(targetRow, targetColumn, attacker);
        gameManager.transferTurn();

        Queue<Message> messages = new ArrayDeque<>();

        Object[] objects = new Object[3];
        objects[0] = targetRow;
        objects[1] = targetColumn;
        objects[2] = attacker;
        messages.add(new Message(objects, MessageType.DUEL_ENEMY_UNIT));

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            addGameStateMessage(messages);
        }

        messages.add(new Message(gameManager.getActiveLeader(), MessageType.LEADER_INFO));
        messages.add(new Message(gameManager.getInactiveLeader(), MessageType.LEADER_INFO));

        return new Message(messages, MessageType.MESSAGE_QUEUE);
    }

    public Message getSkipRoundMessage() {
        gameManager.skipRound();
        gameManager.transferTurn();

        Queue<Message> messages = new ArrayDeque<>();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            addGameStateMessage(messages);
        }

        messages.add(new Message(gameManager.getActiveLeader(), MessageType.LEADER_INFO));
        messages.add(new Message(gameManager.getInactiveLeader(), MessageType.LEADER_INFO));

        return new Message(messages, MessageType.MESSAGE_QUEUE);
    }

    public void sendBroadcastMessage(Message message) throws IOException, EncodeException {
        playerSessionFirst.getValue().sendMessage(message);
        playerSessionSecond.getValue().sendMessage(message);
    }

    private void addGameStateMessage(Queue<Message> messages) {
        switch (gameManager.getGameState().getState()) {
            case VICTORY: {
                messages.add(new Message(gameManager.getGameState().getWinner(), MessageType.GAME_VICTORY));
                break;
            }
            case DRAW: {
                messages.add(new Message(MessageType.GAME_DRAW));
                break;
            }
            case CONTINUES: {
                Leader roundWinner = gameManager.getRoundWinner();

                if (roundWinner != null) {
                    messages.add(new Message(roundWinner, MessageType.ROUND_VICTORY));
                } else {
                    messages.add(new Message(MessageType.ROUND_DRAW));
                }
                break;
            }
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

    public Map.Entry<String, SessionI> getPlayerSessionFirst() {
        return playerSessionFirst;
    }

    public Map.Entry<String, SessionI> getPlayerSessionSecond() {
        return playerSessionSecond;
    }
}
