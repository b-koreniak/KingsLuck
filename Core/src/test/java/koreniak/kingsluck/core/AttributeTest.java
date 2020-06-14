package koreniak.kingsluck.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
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
        Assert.assertEquals(4, attribute.getCurrentValue());

        attribute.modify(-4);
        Assert.assertEquals(0, attribute.getCurrentValue());
    }
}
