package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class GameManagerTest {
    private static final Logger LOGGER = LogManager.getLogger("GameManagerTest");

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            LOGGER.error(description, e);
        }

        @Override
        protected void succeeded(Description description) {
            LOGGER.info(description);
        }
    };

    @Test
    public void putUnitOnField() {
        Leader playerLeader = new Leader();
        Leader enemyLeader = new Leader();

        Unit unitToPut = new Unit();
        unitToPut.setUnitType(UnitType.ARCHER);
        unitToPut.setEfficiency(new Attribute(3));

        playerLeader.getDeckUnits().addFirst(unitToPut);

        GameManager gameManager = new GameManager(3, 1,
                playerLeader, enemyLeader,
                3264234698L, 1288219823L);

        gameManager.putDeckUnitOnField(0, 1);

        Assert.assertEquals(unitToPut, playerLeader.getField().get(0, 1));

        Assert.assertEquals(playerLeader, gameManager.getInactiveLeader());
        Assert.assertEquals(enemyLeader, gameManager.getActiveLeader());
    }

    @Test
    public void duel() {
        Leader playerLeader = new Leader();
        Leader enemyLeader = new Leader();

        Unit target = new Unit();
        target.setUnitType(UnitType.CAVALRY);
        target.setEfficiency(new Attribute(7));

        playerLeader.getDeckUnits().addFirst(target);

        Unit attacker = new Unit();
        attacker.setUnitType(UnitType.SWORDSMAN);
        attacker.setEfficiency(new Attribute(4));

        enemyLeader.getDeckUnits().addFirst(attacker);

        GameManager gameManager = new GameManager(3, 1,
                playerLeader, enemyLeader,
                3264234698L, 1288219823L);

        gameManager.putDeckUnitOnField(0, 2);

        Unit looser = gameManager.duelUsingDeckUnit(0, 2);

        Assert.assertEquals(0, attacker.getEfficiency().getCurrentValue());
        Assert.assertEquals(2, target.getEfficiency().getCurrentValue());
        Assert.assertEquals(attacker, looser);

        Assert.assertEquals(target, playerLeader.getField().get(0, 2));
        Assert.assertEquals(attacker, enemyLeader.getTrashUnits().get(0));
    }
}
