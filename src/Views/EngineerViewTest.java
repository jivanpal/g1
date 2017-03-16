package Views;

import GameLogic.Map;
import GameLogic.Ship;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by james on 15/03/17.
 */
public class EngineerViewTest {

    Ship s;
    Map m;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParseNumber() {
        assertTrue("1".equals(EngineerView.parseNumber("1:abc")));
        assertTrue("2".equals(EngineerView.parseNumber("2:abc")));
        assertFalse("2".equals(EngineerView.parseNumber("1:abc")));
        assertTrue("1".equals(EngineerView.parseNumber("1:a")));
        assertTrue("".equals(EngineerView.parseNumber("")));
    }

    @Test
    public void testParseSequence() {
        assertTrue("abc".equals(EngineerView.parseSequence("1:abc")));
        assertTrue("abc".equals(EngineerView.parseSequence("2:abc")));

        assertFalse("abcd".equals(EngineerView.parseSequence("1:abc")));

        assertTrue("a".equals(EngineerView.parseSequence("3:a")));

        assertTrue("".equals(EngineerView.parseSequence("")));
    }

    @Test
    public void testFindPlayerShip() {

    }
}