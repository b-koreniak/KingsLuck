package koreniak.kingsluck.core.observable;

import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ObservableArrayTest {
    @Test
    public void iterator() {
        ObservableArray<Unit> observableArray = new ObservableArray<>(3, 3, Unit.class);

        Unit unitSwordsman = new Unit();
        unitSwordsman.setUnitType(UnitType.SWORDSMAN);

        Unit unitArcher = new Unit();
        unitArcher.setUnitType(UnitType.ARCHER);

        Unit unitCavalry = new Unit();
        unitCavalry.setUnitType(UnitType.CAVALRY);

        observableArray.add(2, 1, unitArcher);
        observableArray.add(0, 0, unitSwordsman);
        observableArray.add(1, 1, unitCavalry);

        Iterator<Unit> iterator = observableArray.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(unitSwordsman.getUnitType(), iterator.next().getUnitType());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(unitCavalry.getUnitType(), iterator.next().getUnitType());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(unitArcher.getUnitType(), iterator.next().getUnitType());

        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("NoSuchElementException was not thrown");
        } catch (NoSuchElementException ignored) {

        }
    }
}
