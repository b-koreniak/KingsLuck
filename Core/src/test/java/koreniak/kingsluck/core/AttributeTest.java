package koreniak.kingsluck.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


public class AttributeTest {
    private static final Logger LOGGER = LogManager.getLogger("AttributeTest");

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

    private Attribute attribute;

    @Before
    public void init() {
        attribute = new Attribute(7);
    }

    @Test
    public void inc() {
        attribute.inc(4);

        Assert.assertEquals(11, attribute.getCurrentValue());
    }

    @Test
    public void dec() {
        attribute.dec(3);
        Assert.assertEquals(4, attribute.getCurrentValue());

        attribute.dec(4);
        Assert.assertEquals(0, attribute.getCurrentValue());
    }

    @Test
    public void modify() {
        attribute.modify(4);
        Assert.assertEquals(11, attribute.getCurrentValue());

        attribute.modify(-3);
        Assert.assertEquals(8, attribute.getCurrentValue());

        attribute.modify(-4);
        Assert.assertEquals(4, attribute.getCurrentValue());
    }
}
