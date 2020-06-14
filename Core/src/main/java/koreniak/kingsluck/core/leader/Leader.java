package koreniak.kingsluck.core.leader;

import koreniak.kingsluck.core.Ability;
import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.GameObject;
import koreniak.kingsluck.core.Race;
import koreniak.kingsluck.core.observable.ObservableArray;
import koreniak.kingsluck.core.observer.ArrayObserver;
import koreniak.kingsluck.core.unit.Unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Leader extends GameObject implements ArrayObserver<Unit> {
    private static final int FIELD_ROWS = 2;
    private static final int FIELD_COLUMNS = 3;

    private Attribute efficiency = new Attribute();

    private Race race;
    private LeaderType leaderType;

    private ObservableArray<Unit> field = new ObservableArray<>(FIELD_ROWS, FIELD_COLUMNS, Unit.class);

    private List<Unit> handUnits = new ArrayList<>();
    private LinkedList<Unit> deckUnits = new LinkedList<>();

    private List<Ability> handAbilities = new ArrayList<>();

    private boolean isActionable;
    private boolean isSkippedRound;

    private int turnsPlayed;
    private int roundsWon;

    public Leader() {
        field.addObserver(this);
    }

    @Override
    public void updateOnAdd(Unit unit) {
        efficiency.modify(unit.getEfficiency().getCurrentValue());
    }

    @Override
    public void updateOnRemove(Unit unit) {
        if (unit.getEfficiency().getCurrentValue() > 0) {
            efficiency.modify(unit.getEfficiency().getCurrentValue() * -1);
        } else {
            efficiency.modify(unit.getEfficiency().getCurrentValue());
        }
    }

    @Override
    public void updateOnChange(Unit unit) {
        efficiency.modify(unit.getEfficiency().getCurrentValue());
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public LeaderType getLeaderType() {
        return leaderType;
    }

    public void setLeaderType(LeaderType leaderType) {
        this.leaderType = leaderType;
    }

    public LinkedList<Unit> getDeckUnits() {
        return deckUnits;
    }

    public void setDeckUnits(LinkedList<Unit> deckUnits) {
        this.deckUnits = deckUnits;
    }

    public List<Ability> getHandAbilities() {
        return handAbilities;
    }

    public List<Unit> getHandUnits() {
        return handUnits;
    }

    public ObservableArray<Unit> getField() {
        return field;
    }

    public int getTurnsPlayed() {
        return turnsPlayed;
    }

    public void setTurnsPlayed(int turnsPlayed) {
        this.turnsPlayed = turnsPlayed;
    }

    public boolean isSkippedRound() {
        return isSkippedRound;
    }

    public void setSkippedRound(boolean skippedRound) {
        isSkippedRound = skippedRound;
    }

    public boolean isActionable() {
        return isActionable;
    }

    public void setActionable(boolean actionable) {
        isActionable = actionable;
    }

    public Attribute getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Attribute efficiency) {
        this.efficiency = efficiency;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public void setRoundsWon(int roundsWon) {
        this.roundsWon = roundsWon;
    }
}
