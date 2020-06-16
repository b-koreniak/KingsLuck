package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitType;
import org.junit.Assert;
import org.junit.Test;

public class DuelManagerTest {
    @Test
    public void duel() {
        Unit attacker = new Unit();
        attacker.setUnitType(UnitType.SWORDSMAN);
        attacker.setEfficiency(new Attribute(4));

        Unit target = new Unit();
        target.setUnitType(UnitType.SPEARMAN);
        target.setEfficiency(new Attribute(7));

        Unit deadUnit = DuelManager.duel(attacker, target);

        Assert.assertEquals(0, attacker.getEfficiency().getCurrentValue());
        Assert.assertEquals(2, target.getEfficiency().getCurrentValue());

        Assert.assertEquals(attacker, deadUnit);
    }

    @Test
    public void attack() {
        Unit attacker = new Unit();
        attacker.setUnitType(UnitType.SWORDSMAN);
        attacker.setEfficiency(new Attribute(4));

        Unit target = new Unit();
        target.setUnitType(UnitType.SPEARMAN);
        target.setEfficiency(new Attribute(7));

        Unit deadUnit = DuelManager.attack(attacker, target);

        Assert.assertEquals(4, attacker.getEfficiency().getCurrentValue());
        Assert.assertEquals(3, target.getEfficiency().getCurrentValue());
    }
}
