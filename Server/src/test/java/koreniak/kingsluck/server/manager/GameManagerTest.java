package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class GameManagerTest {
    private static final Logger LOGGER = LogManager.getLogger("GameManagerTest");

    Leader playerLeader;
    Leader enemyLeader;

    GameManager gameManager;

    @Before
    public void init() {
        playerLeader = new Leader();
        enemyLeader = new Leader();

        gameManager = new GameManager(3, 1,
                playerLeader, enemyLeader
        );
    }

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
    public void transferTurn() {
        gameManager.transferTurn();

        Assert.assertEquals(1, gameManager.getTurns());
        Assert.assertEquals(1, playerLeader.getTurnsPlayed());

        Assert.assertEquals(playerLeader, gameManager.getInactiveLeader());
        Assert.assertEquals(enemyLeader, gameManager.getActiveLeader());
    }

    @Test
    public void skipRound() {
        gameManager.skipRound();

        Assert.assertTrue(playerLeader.isSkippedRound());
        Assert.assertFalse(playerLeader.isActionable());
    }

    @Test
    public void putDeckUnitOnField() {
        Unit unitToPut = new Unit();
        unitToPut.setUnitType(UnitType.ARCHER);
        unitToPut.setEfficiency(new Attribute(3));

        playerLeader.getDeckUnits().addFirst(unitToPut);

        gameManager.putDeckUnitOnField(0, 1);

        Assert.assertEquals(unitToPut, playerLeader.getField().get(0, 1));
    }

    @Test
    public void resetLeaders() {
        Unit playerUnit = new Unit();
        playerUnit.setUnitType(UnitType.CAVALRY);
        playerUnit.setEfficiency(new Attribute(7));

        playerLeader.getDeckUnits().addFirst(playerUnit);

        Unit enemyUnit = new Unit();
        enemyUnit.setUnitType(UnitType.SWORDSMAN);
        enemyUnit.setEfficiency(new Attribute(4));

        enemyLeader.getDeckUnits().addFirst(enemyUnit);

        gameManager = new GameManager(3, 1,
                playerLeader, enemyLeader
        );

        gameManager.putDeckUnitOnField(0, 0);
        gameManager.transferTurn();

        gameManager.putDeckUnitOnField(1, 0);

        gameManager.resetLeaders();

        Assert.assertEquals(0, playerLeader.getTurnsPlayed());
        Assert.assertEquals(0, enemyLeader.getTurnsPlayed());

        Assert.assertTrue(playerLeader.getField().isEmpty());
        Assert.assertTrue(enemyLeader.getField().isEmpty());

        Assert.assertEquals(playerUnit, playerLeader.getTrashUnits().get(0));
        Assert.assertEquals(enemyUnit, enemyLeader.getTrashUnits().get(0));
    }

    @Test
    public void getGameState() {
        gameManager.getActiveLeader().setRoundsWon(1);

        GameState gameState = gameManager.getGameState();

        Assert.assertEquals(GameState.State.VICTORY, gameState.getState());
        Assert.assertEquals(gameManager.getActiveLeader(), gameState.getWinner());
    }

    @Test
    public void duelUsingDeckUnit() {
        Unit playerUnit = new Unit();
        playerUnit.setUnitType(UnitType.CAVALRY);
        playerUnit.setEfficiency(new Attribute(7));

        playerLeader.getDeckUnits().addFirst(playerUnit);

        Unit enemyUnit = new Unit();
        enemyUnit.setUnitType(UnitType.SWORDSMAN);
        enemyUnit.setEfficiency(new Attribute(4));

        enemyLeader.getDeckUnits().addFirst(enemyUnit);

        gameManager = new GameManager(3, 1,
                playerLeader, enemyLeader
        );

        gameManager.putDeckUnitOnField(0, 2);
        gameManager.transferTurn();

        Unit looser = gameManager.duelUsingDeckUnit(0, 2);

        Assert.assertEquals(0, enemyUnit.getEfficiency().getCurrentValue());
        Assert.assertEquals(2, playerUnit.getEfficiency().getCurrentValue());
        Assert.assertEquals(enemyUnit, looser);

        Assert.assertEquals(playerUnit, playerLeader.getField().get(0, 2));
        Assert.assertEquals(enemyUnit, enemyLeader.getTrashUnits().get(0));
    }
}
