package JUnit;

import GameLogic.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by James on 21/03/17.
 */
public class ResourceTest {

    private Resource resourceNone;
    private Resource resourceFull;
    private Resource resourceMedium;

    @Before
    public void setUp() {
        resourceNone = new Resource(100, 0);
        resourceFull = new Resource(10, 10);
        resourceMedium = new Resource(10, 5);
    }

    @After
    public void tearDown() {
        resourceNone = null;
        resourceFull = null;
    }

    @Test
    public void testGetFraction() {
        assertTrue(resourceNone.getFraction() == 0);
        assertTrue(resourceMedium.getFraction() == .5d);
        assertTrue(resourceFull.getFraction() == 1);
    }

    @Test
    public void testSet() {
        int prev = resourceFull.get();
        resourceFull.set(resourceFull.get() + 1);
        int curr = resourceFull.get();
        assertEquals(prev, curr);

        prev = resourceNone.get();
        resourceNone.set(-1);
        curr = resourceNone.get();
        assertEquals(prev, curr);

        resourceMedium.set(6);
        assertTrue(resourceMedium.get() == 6);
    }
}
