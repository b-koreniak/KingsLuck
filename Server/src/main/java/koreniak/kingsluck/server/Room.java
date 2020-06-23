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
    private Map.Entry<Leader, SessionI> leaderSessionFirst;
    private Map.Entry<Leader, SessionI> leaderSessionSecond;

    private GameManager gameManager;

    private Queue<Message> messageQueue;

    public Room() {
        messageQueue = new ArrayDeque<>();
    }

    public void addLeader(Leader leader, SessionI sessionI) {
        if (leaderSessionFirst == null) {
            leaderSessionFirst = new AbstractMap.SimpleEntry<>(leader, sessionI);
        } else if (leaderSessionSecond == null) {
            leaderSessionSecond = new AbstractMap.SimpleEntry<>(leader, sessionI);
        }
    }

    public boolean isFilled() {
        return leaderSessionFirst != null && leaderSessionSecond != null;
    }

    public void startGame() {
        gameManager = new GameManager(leaderSessionFirst.getKey(), leaderSessionSecond.getKey());

        addLeadersToMessageQueue();
        messageQueue.add(new Message(MessageType.START_GAME));
        sendMessageQueue();
    }

    public void putUnitOnField(int row, int column, Unit unit) {
        gameManager.putUnitOnField(row, column, unit);
        gameManager.transferTurn();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            handleGameState();
        }

        addLeadersToMessageQueue();
        List<Object> objects = new ArrayList<>();
        objects.add(row);
        objects.add(column);
        objects.add(unit);
        messageQueue.add(new Message(objects, MessageType.PUT_UNIT_ON_FIELD));

        sendMessageQueue();
    }

    public void duelEnemyUnit(int targetRow, int targetColumn, Unit attacker) {
        gameManager.duel(targetRow, targetColumn, attacker);
        gameManager.transferTurn();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            handleGameState();
        }

        addLeadersToMessageQueue();
        List<Object> objects = new ArrayList<>();
        objects.add(targetRow);
        objects.add(targetColumn);
        objects.add(attacker);
        messageQueue.add(new Message(objects, MessageType.DUEL_ENEMY_UNIT));

        sendMessageQueue();
    }

    public void skipRound() {
        gameManager.skipRound();
        gameManager.transferTurn();

        if (gameManager.isEndOfRound()) {
            setRoundsWon();
            handleGameState();
        }

        addLeadersToMessageQueue();
        sendMessageQueue();
    }

    public void sendMessageQueue() {
        try {
            leaderSessionFirst.getValue().sendMessage(new Message(messageQueue, MessageType.MESSAGE_QUEUE));
            leaderSessionSecond.getValue().sendMessage(new Message(messageQueue, MessageType.MESSAGE_QUEUE));
        } catch (IOException | EncodeException ignored) {
            // TODO: 6/23/2020 add exception handling
        }
    }

    private void addLeadersToMessageQueue() {
        messageQueue.add(new Message(leaderSessionFirst.getKey(), MessageType.LEADER_INFO));
        messageQueue.add(new Message(leaderSessionSecond.getKey(), MessageType.LEADER_INFO));
    }

    private void handleGameState() {
        switch (gameManager.getGameState().getState()) {
            case CONTINUES: {
                Leader winner = gameManager.getRoundWinner();

                if (winner != null) {
                    messageQueue.add(new Message(winner, MessageType.ROUND_VICTORY));
                } else {
                    messageQueue.add(new Message(MessageType.ROUND_DRAW));
                }
                break;
            }
            case VICTORY: {
                messageQueue.add(new Message(gameManager.getGameState().getWinner(), MessageType.GAME_VICTORY));
                break;
            }
            case DRAW: {
                messageQueue.add(new Message(MessageType.GAME_DRAW));
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

    public Map.Entry<Leader, SessionI> getLeaderSessionFirst() {
        return leaderSessionFirst;
    }

    public Map.Entry<Leader, SessionI> getLeaderSessionSecond() {
        return leaderSessionSecond;
    }
}
