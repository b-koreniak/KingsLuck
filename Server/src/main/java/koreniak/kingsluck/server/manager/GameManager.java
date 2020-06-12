package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.Ability;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.exception.GameException;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.observable.Observable;
import koreniak.kingsluck.core.observer.Observer;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager implements Observable<Message> {
    private AbilityManager abilityManager;

    private Leader activeLeader;
    private Leader inactiveLeader;

    private int turns;

    private int maxTurns;
    private int roundsToWin;

    private GameState gameState;

    public GameManager(int maxTurns, int roundsToWin, Leader activeLeader, Leader inactiveLeader, long seedForLeader1, long seedForLeader2) {
        this.maxTurns = maxTurns;
        this.roundsToWin = roundsToWin;

        this.activeLeader = activeLeader;
        this.inactiveLeader = inactiveLeader;

        gameState = new GameState(GameState.State.CONTINUES);

        int uniqueId = 0;

        Collections.shuffle(activeLeader.getDeckUnits(), new Random(seedForLeader1));
        Collections.shuffle(inactiveLeader.getDeckUnits(), new Random(seedForLeader2));

        activeLeader.setUniqueId(uniqueId);
        inactiveLeader.setUniqueId(++uniqueId);

        for (Unit deckUnit : activeLeader.getDeckUnits()) {
            deckUnit.setUniqueId(++uniqueId);
        }
        for (Unit handUnit : activeLeader.getHandUnits()) {
            handUnit.setUniqueId(++uniqueId);
        }
        for (Ability handAbility : activeLeader.getHandAbilities()) {
            handAbility.setUniqueId(++uniqueId);
        }

        for (Unit deckUnit : inactiveLeader.getDeckUnits()) {
            deckUnit.setUniqueId(++uniqueId);
        }
        for (Unit handUnit : inactiveLeader.getHandUnits()) {
            handUnit.setUniqueId(++uniqueId);
        }
        for (Ability handAbility : inactiveLeader.getHandAbilities()) {
            handAbility.setUniqueId(++uniqueId);
        }
    }

    public Unit putDeckUnitOnField(int row, int column) {
        return putUnitOnField(row, column, activeLeader.getDeckUnits().poll());
    }

    public Unit putUnitOnField(int row, int column, Unit unit) {
        if (activeLeader.getField().get(row, column) != null) {
            throw new GameException(MessageFormat.format(ResourceBundle
                    .getBundle("StringBundle", Locale.getDefault())
                    .getString("message.cell.occupied"), row, column));
        }

        activeLeader.getField().add(row, column, unit);

        transferTurn();

        return unit;
    }

    public void fireAbility(Ability ability, Unit target) {

        transferTurn();
    }

    public void fireAbility(Ability ability, Leader target) {

        transferTurn();
    }

    public void skipRound() {
        activeLeader.setSkippedRound(true);
        activeLeader.setActionable(false);
        transferTurn();
    }

    private void transferTurn() {
        turns++;
        activeLeader.setTurnsPlayed(activeLeader.getTurnsPlayed() + 1);

        if ((activeLeader.getTurnsPlayed() == maxTurns && inactiveLeader.getTurnsPlayed() == maxTurns)
                || (activeLeader.isSkippedRound() && inactiveLeader.isSkippedRound())
                || (activeLeader.getTurnsPlayed() == maxTurns && inactiveLeader.isSkippedRound())
                || (activeLeader.isSkippedRound() && inactiveLeader.getTurnsPlayed() == maxTurns)) {
            endRound();
        }

        if (!inactiveLeader.isSkippedRound()) {
            activeLeader.setActionable(false);
            inactiveLeader.setActionable(true);

            Leader tempLeader = activeLeader;
            activeLeader = inactiveLeader;
            inactiveLeader = tempLeader;
        }

        setGameState();
        notifyObservers(new Message(getGameState(), MessageType.GAME_STATE));
    }

    private void endRound() {
        if (activeLeader.getEfficiency().getCurrentValue() > inactiveLeader.getEfficiency().getCurrentValue()) {
            activeLeader.setRoundsWon(activeLeader.getRoundsWon() + 1);
        } else if (activeLeader.getEfficiency().getCurrentValue() < inactiveLeader.getEfficiency().getCurrentValue()) {
            inactiveLeader.setRoundsWon(inactiveLeader.getRoundsWon() + 1);
        } else {
            activeLeader.setRoundsWon(activeLeader.getRoundsWon() + 1);
            inactiveLeader.setRoundsWon(inactiveLeader.getRoundsWon() + 1);
        }

        activeLeader.setTurnsPlayed(0);
        inactiveLeader.setTurnsPlayed(0);
    }

    private List<Observer<Message>> observers = new CopyOnWriteArrayList<>();

    @Override
    public void addObserver(Observer<Message> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Message> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        for (Observer<Message> observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public void notifyObservers() {

    }

    public GameState getGameState() {
        return gameState;
    }

    private void setGameState() {
        if (activeLeader.getRoundsWon() == roundsToWin && inactiveLeader.getRoundsWon() == roundsToWin) {
            gameState = new GameState(GameState.State.DRAW);
        } else if (activeLeader.getRoundsWon() == roundsToWin) {
            gameState = new GameState(GameState.State.VICTORY, activeLeader, inactiveLeader);
        } else if (inactiveLeader.getRoundsWon() == roundsToWin) {
            gameState = new GameState(GameState.State.VICTORY, inactiveLeader, activeLeader);
        } else {
            gameState = new GameState(GameState.State.CONTINUES);
        }
    }

    public int getTurns() {
        return turns;
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public int getRoundsToWin() {
        return roundsToWin;
    }

    public Leader getActiveLeader() {
        return activeLeader;
    }

    public Leader getInactiveLeader() {
        return inactiveLeader;
    }
}
