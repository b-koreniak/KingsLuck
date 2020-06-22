package koreniak.kingsluck.core.message;

import koreniak.kingsluck.core.Attribute;
import koreniak.kingsluck.core.Race;
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

import javax.websocket.DecodeException;
import javax.websocket.EncodeException;

public class MessageEncoderDecoderTest {
    private static final Logger LOGGER = LogManager.getLogger("MessageEncoderDecoderTest");

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            LOGGER.info(description);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            LOGGER.error(description, e);
        }
    };

    private MessageEncoder encoder = new MessageEncoder();
    private MessageDecoder decoder = new MessageDecoder();

    @Test
    public void canEncodeAndDecodeTextMessage() throws EncodeException, DecodeException {
        Message message = new Message("text", MessageType.DEFAULT);

        String encodedMessage = encoder.encode(message);
        Message decodedMessage = decoder.decode(encodedMessage);

        Assert.assertEquals(message.getObject(), decodedMessage.getObject());
        Assert.assertEquals(message.getType(), decodedMessage.getType());
    }

    @Test
    public void canEncodeAndDecodeAttribute() throws EncodeException, DecodeException {
        Attribute attribute = new Attribute(2);

        Message message = new Message(attribute, MessageType.DEFAULT);

        String encodedMessage = encoder.encode(message);
        Message decodedMessage = decoder.decode(encodedMessage);

        Attribute decodedAttribute = (Attribute) decodedMessage.getObject();

        Assert.assertEquals(attribute.getCurrentValue(), decodedAttribute.getCurrentValue());
    }

    @Test
    public void canEncodeAndDecodeUnit() throws EncodeException, DecodeException {
        Unit unit = new Unit();
        unit.setEfficiency(new Attribute(4));
        unit.setRace(Race.HUMAN);
        unit.setUnitType(UnitType.SWORDSMAN);

        Message message = new Message(unit, MessageType.DEFAULT);

        String encodedMessage = encoder.encode(message);
        Message decodedMessage = decoder.decode(encodedMessage);

        Unit decodedUnit = (Unit) decodedMessage.getObject();

        Assert.assertEquals(unit.getRace(), decodedUnit.getRace());
        Assert.assertEquals(unit.getUnitType(), decodedUnit.getUnitType());
        Assert.assertEquals(unit.getEfficiency().getCurrentValue(), decodedUnit.getEfficiency().getCurrentValue());
    }

    @Test
    public void canEncodeAndDecodeLeader() throws EncodeException, DecodeException {
        Leader leader = new Leader();
        leader.setRace(Race.HUMAN);

        Unit unit = new Unit();
        unit.setEfficiency(new Attribute(4));
        unit.setRace(Race.HUMAN);
        unit.setUnitType(UnitType.SWORDSMAN);

        leader.getDeckUnits().add(unit);

        Message message = new Message(leader, MessageType.DEFAULT);

        String encodedMessage = encoder.encode(message);
        Message decodedMessage = decoder.decode(encodedMessage);

        Leader decodedLeader = (Leader) decodedMessage.getObject();

        Assert.assertEquals(leader.getRace(), decodedLeader.getRace());

        Unit decodedUnit = leader.getDeckUnits().peek();

        Assert.assertEquals(unit.getRace(), decodedUnit.getRace());
        Assert.assertEquals(unit.getUnitType(), decodedUnit.getUnitType());
        Assert.assertEquals(unit.getEfficiency().getCurrentValue(), decodedUnit.getEfficiency().getCurrentValue());
    }
}
