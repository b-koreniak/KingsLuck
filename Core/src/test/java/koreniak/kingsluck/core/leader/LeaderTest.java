package koreniak.kingsluck.core.leader;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LeaderTest {
    private Leader leader;

    private Unit unitSwordsman;
    private Unit unitSpearman;
    private Unit unitCavalry;

    @Before
    public void init() {
        leader = new Leader();

        unitSwordsman = new Unit();
        unitSwordsman.setUnitType(UnitType.SWORDSMAN);
        unitSwordsman.setEfficiency(new Attribute(4));

        unitSpearman = new Unit();
        unitSpearman.setEfficiency(new Attribute(3));
        unitSpearman.setUnitType(UnitType.SPEARMAN);

        unitCavalry = new Unit();
        unitCavalry.setEfficiency(new Attribute(7));
        unitCavalry.setUnitType(UnitType.CAVALRY);
    }

    @Test
    public void updateOnAdd() {
        leader.updateOnAdd(unitSwordsman);

        Assert.assertEquals(unitSwordsman.getEfficiency().getCurrentValue(), leader.getEfficiency().getCurrentValue());
    }

    @Test
    public void updateOnRemove() {
        leader.setEfficiency(new Attribute(5));

        leader.updateOnRemove(unitSwordsman);

        Assert.assertEquals(1, leader.getEfficiency().getCurrentValue());
    }

    @Test
    public void updateOnChange() {
        leader.updateOnChange(unitSwordsman);

        Assert.assertEquals(unitSwordsman.getEfficiency().getCurrentValue(), leader.getEfficiency().getCurrentValue());
    }

    @Test
    public void addUnitOnFiled() {
        leader.getField().add(0, 1, unitSpearman);
        Assert.assertEquals(unitSpearman.getEfficiency().getCurrentValue(), leader.getEfficiency().getCurrentValue());

        leader.getField().add(1, 1, unitCavalry);
        int effSum = unitSpearman.getEfficiency().getCurrentValue() + unitCavalry.getEfficiency().getCurrentValue();
        Assert.assertEquals(effSum, leader.getEfficiency().getCurrentValue());
    }

    @Test
    public void removeUnitFromField() {
        leader.getField().add(0, 1, unitSpearman);
        leader.getField().add(1, 1, unitCavalry);

        leader.getField().remove(0, 1);
        Assert.assertEquals(unitCavalry.getEfficiency().getCurrentValue(), leader.getEfficiency().getCurrentValue());

        leader.getField().remove(1, 1);
        Assert.assertEquals(0, leader.getEfficiency().getCurrentValue());
    }

    @Test
    public void modifyUnitOnField() {
        leader.getField().add(0, 1, unitSpearman);
        leader.getField().add(1, 1, unitCavalry);

        int effSum = unitSpearman.getEfficiency().getCurrentValue() + unitCavalry.getEfficiency().getCurrentValue();

        unitSpearman.getEfficiency().dec(2);
        Assert.assertEquals(effSum - 2, leader.getEfficiency().getCurrentValue());

        effSum -= 2;

        unitCavalry.getEfficiency().inc(3);
        Assert.assertEquals(effSum + 3, leader.getEfficiency().getCurrentValue());
    }
}
