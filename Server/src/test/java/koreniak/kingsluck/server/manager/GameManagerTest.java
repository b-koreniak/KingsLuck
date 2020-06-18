package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.unit.Unit;
import koreniak.kingsluck.core.unit.UnitType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Random;

public class GameManagerTest {
    private static final Logger LOGGER = LogManager.getLogger(GameManager.class);

    private static GameManager gameManager;

    private static Leader leader1;
    private static Leader leader2;

    private static long seedForDrawLeader1 = 3264234698L;
    private static long seedForDrawLeader2 = 1288219823L;

    @BeforeClass
    public static void init() {
        BeanFactory beanFactory = new GenericXmlApplicationContext("game/game-bean.xml");

        leader1 = beanFactory.getBean("humanCommander", Leader.class);
        leader2 = beanFactory.getBean("humanCommander", Leader.class);

        gameManager = new GameManager(4, 1, leader1, leader2, seedForDrawLeader1, seedForDrawLeader2);
    }

    @After
    public void log() {
        int turn = gameManager.getTurns();

        StringBuilder builder = new StringBuilder();

        builder.append("TURN " + turn + " RESULT");
        builder.append(System.lineSeparator());

        appendLeaderData(leader1, turn, builder);
        appendLeaderData(leader2, turn, builder);

        LOGGER.debug(builder.toString());
    }

    private void appendLeaderData(Leader leader, int turn, StringBuilder builder) {
        builder.append("Leader uniqueId: " + leader.getUniqueId());
        builder.append(System.lineSeparator());

        builder.append("Total efficiency: " + leader.getEfficiency());
        builder.append(System.lineSeparator());

        builder.append("Field:");
        builder.append(System.lineSeparator());

        for (int i = 0; i < leader.getField().getRows(); i++) {
            for (int j = 0; j < leader.getField().getColumns(); j++) {
                builder.append(leader.getField().get(i, j));
                builder.append("\t");
            }
            builder.append(System.lineSeparator());
        }

        builder.append(System.lineSeparator());
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

    @Test
    public void testTurn1() {
        putDeckUnitOnField();
        assertLeadersState(leader2, leader1);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn2() {
        putDeckUnitOnField();
        assertLeadersState(leader1, leader2);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn3() {
        putDeckUnitOnField();
        assertLeadersState(leader2, leader1);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn4() {
        putDeckUnitOnField();
        assertLeadersState(leader1, leader2);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn5() {
        putDeckUnitOnField();
        assertLeadersState(leader2, leader1);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn6() {
        putDeckUnitOnField();
        assertLeadersState(leader1, leader2);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn7() {
        putDeckUnitOnField();
        assertLeadersState(leader2, leader1);
        assertLeadersEfficiency(leader1, leader2);
    }

    @Test
    public void testTurn8() {
        putDeckUnitOnField();
        assertLeadersState(leader1, leader2);
        assertLeadersEfficiency(leader1, leader2);

        Assert.assertEquals(GameState.State.DRAW, gameManager.getGameState().getState());
    }

    private void assertLeadersState(Leader expectedActiveLeader, Leader expectedInactiveLeader) {
        Assert.assertEquals(expectedActiveLeader, gameManager.getActiveLeader());
        Assert.assertEquals(expectedInactiveLeader, gameManager.getInactiveLeader());
    }

    private void assertLeadersEfficiency(Leader leader1, Leader leader2) {
        Assert.assertEquals(getEfficiencySum(leader1), leader1.getEfficiency().getCurrentValue());
        Assert.assertEquals(getEfficiencySum(leader2), leader2.getEfficiency().getCurrentValue());
    }

    private int getEfficiencySum(Leader leader) {
        int sum = 0;
        Unit[][] units = leader.getField().getArray();

        for (int i = 0; i < units.length; i++) {
            for (int j = 0; j < units[i].length; j++) {
                if (units[i][j] != null) {
                    sum += units[i][j].getEfficiency().getCurrentValue();
                }
            }
        }

        return sum;
    }

    private void putDeckUnitOnField() {
        Random random;
        if (leader1.equals(gameManager.getActiveLeader())) {
            random = new Random(seedForDrawLeader1);
        } else {
            random = new Random(seedForDrawLeader2);
        }

        int maxRowAmount = gameManager.getActiveLeader().getField().getRows();
        int maxColumnAmount = gameManager.getActiveLeader().getField().getColumns();

        int randomRow = random.nextInt(maxRowAmount);
        int randomColumn = random.nextInt(maxColumnAmount);

        while (gameManager.getActiveLeader().getField().get(randomRow, randomColumn) != null) {
            randomRow = random.nextInt(maxRowAmount);
            randomColumn = random.nextInt(maxColumnAmount);
        }

        Unit unit = gameManager.putDeckUnitOnField(randomRow, randomColumn);
    }
}
