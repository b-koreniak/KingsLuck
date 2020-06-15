package koreniak.kingsluck.core.unit;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.GameObject;
import koreniak.kingsluck.core.Race;
import koreniak.kingsluck.core.observable.Observable;
import koreniak.kingsluck.core.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Unit extends GameObject implements Observable<Unit>, Observer<Attribute> {
    private Attribute efficiency;

    private Race race;
    private UnitType unitType;

    @Override
    public void update(Attribute observable) {
        notifyObservers();
    }

    private List<Observer<Unit>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<Unit> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Unit> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Unit> observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void notifyObservers(Unit object) {

    }

    @Override
    public String toString() {
        return "Unit{" +
                "efficiency=" + efficiency +
                ", race=" + race +
                ", unitType=" + unitType + '}';
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Attribute getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Attribute efficiency) {
        this.efficiency = efficiency;

        this.efficiency.addObserver(this);
    }
}
