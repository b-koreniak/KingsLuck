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

    public GameManager(Leader activeLeader, Leader inactiveLeader) {
        this(6, 2, activeLeader, inactiveLeader);
    }

    public GameManager(int maxTurns, int roundsToWin, Leader activeLeader, Leader inactiveLeader) {
        this.maxTurns = maxTurns;
        this.roundsToWin = roundsToWin;

        this.activeLeader = activeLeader;
        this.inactiveLeader = inactiveLeader;

        int uniqueId = 0;

        Collections.shuffle(activeLeader.getDeckUnits());
        Collections.shuffle(inactiveLeader.getDeckUnits());

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

    public Unit duelUsingDeckUnit(int targetRow, int targetColumn) {
        return duel(targetRow, targetColumn, activeLeader.getDeckUnits().poll());
    }

    public Unit duel(int targetRow, int targetColumn, Unit attacker) {
        Unit target = inactiveLeader.getField().get(targetRow, targetColumn);

        Unit looser = DuelManager.duel(attacker, target);

        if (looser != null) {
            if (looser.equals(target)) {
                activeLeader.getDeckUnits().add(attacker);

                inactiveLeader.getField().remove(targetRow, targetColumn);
                inactiveLeader.getTrashUnits().add(target);
            }

            if (looser.equals(attacker)) {
                activeLeader.getTrashUnits().add(attacker);
            }
        }

        return looser;
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

        return unit;
    }

    public void fireAbility(Ability ability, Unit target) {

    }

    public void fireAbility(Ability ability, Leader target) {

    }

    public void skipRound() {
        activeLeader.setSkippedRound(true);
        activeLeader.setActionable(false);
    }

    public void transferTurn() {
        turns++;
        activeLeader.setTurnsPlayed(activeLeader.getTurnsPlayed() + 1);

        if (!inactiveLeader.isSkippedRound()) {
            activeLeader.setActionable(false);
            inactiveLeader.setActionable(true);

            Leader tempLeader = activeLeader;
            activeLeader = inactiveLeader;
            inactiveLeader = tempLeader;
        }
    }

    public boolean isEndOfRound() {
        if (activeLeader.getTurnsPlayed() == maxTurns && inactiveLeader.getTurnsPlayed() == maxTurns) {
            return true;
        }

        if (activeLeader.getTurnsPlayed() == maxTurns && inactiveLeader.isSkippedRound()) {
            return true;
        }

        if (inactiveLeader.getTurnsPlayed() == maxTurns && activeLeader.isSkippedRound()) {
            return true;
        }

        if (inactiveLeader.isSkippedRound() && activeLeader.isSkippedRound()) {
            return true;
        }

        return false;
    }

    public Leader getRoundWinner() {
        if (activeLeader.getEfficiency().getCurrentValue() > inactiveLeader.getEfficiency().getCurrentValue()) {
            return activeLeader;
        }

        if (activeLeader.getEfficiency().getCurrentValue() < inactiveLeader.getEfficiency().getCurrentValue()) {
            return inactiveLeader;
        }

        return null;
    }

    public void resetLeaders() {
        activeLeader.setTurnsPlayed(0);
        inactiveLeader.setTurnsPlayed(0);

        for (Unit unit : activeLeader.getField()) {
            activeLeader.getTrashUnits().add(unit);
        }
        activeLeader.getField().clear();

        for (Unit unit : inactiveLeader.getField()) {
            inactiveLeader.getTrashUnits().add(unit);
        }
        inactiveLeader.getField().clear();
    }

    public GameState getGameState() {
        if (activeLeader.getRoundsWon() == roundsToWin && inactiveLeader.getRoundsWon() == roundsToWin) {
            return new GameState(GameState.State.DRAW);
        }

        if (activeLeader.getRoundsWon() == roundsToWin) {
            return new GameState(GameState.State.VICTORY, activeLeader, inactiveLeader);
        }

        if (inactiveLeader.getRoundsWon() == roundsToWin) {
            return new GameState(GameState.State.VICTORY, inactiveLeader, activeLeader);
        }

        return new GameState(GameState.State.CONTINUES);
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
