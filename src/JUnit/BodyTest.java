package JUnit;

import Geometry.Quaternion;
import Geometry.Rotation;
import Geometry.Vector;
import Physics.Body;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunListener;

import static org.junit.Assert.*;

/**
 * Created by James on 22/03/17.
 * Unit tests for the Body.java class.
 */
public class BodyTest {

    Body b1; // 10kg, 10m radius, at origin, no rotation, no velocity, no angular velocity
    Body b2; // 10kg, 10m radius, at (10, 10, 10), rotation Quaternion I, velocity of (1, 1, 1), angular velocity of (1, 1, 1).
    Body clone;

    @Before
    public void setUp() {
        b1 = new Body(10, 10, new Vector(0, 0, 0), Rotation.NONE, new Vector(0, 0, 0), new Vector(0, 0, 0));
        b2 = new Body(10, 10, new Vector(10, 10, 10), new Rotation(Quaternion.I), new Vector(1, 1, 1), new Vector(1, 1, 1));
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testIncrementingID() {
        assertTrue((b1.getID() + 1) == b2.getID());

        try {
            clone = (Body) b1.clone();
        } catch (CloneNotSupportedException e) {
            fail();
        }

        assertTrue(clone.getID() > b1.getID());
    }

    @Test
    public void testGetPosition() {
        Body b = new Body(10, 10, new Vector(0, 0, 0));
        assertEquals(b.getPosition(), new Vector(0, 0, 0));
    }

    @Test
    public void testAlterPosition() {
        Body b = new Body(10, 10, new Vector(0, 0, 0));

        Vector oldPos = b.getPosition();
        b.alterPosition(new Vector(0, 0, 0));
        assertEquals(oldPos, b.getPosition());

        oldPos = b.getPosition();
        b.alterPosition(new Vector(10, 0, 0));
        assertNotEquals(oldPos, b.getPosition());
        assertEquals(b.getPosition(), new Vector(10, 0, 0));

        oldPos = b.getPosition();
        b.alterPosition(new Vector(10, 10, 10));
        assertNotEquals(oldPos, b.getPosition());
        assertEquals(b.getPosition(), new Vector(20, 10, 10));
    }

    @Test
    public void testAlterOrientation() {
        assertFalse(b1.getOrientation().equals(b2.getOrientation()));
        b1.alterOrientation(b2.getOrientation());
        assertEquals(b1.getOrientation(), b2.getOrientation());
        b1.alterOrientation(new Rotation(Quaternion.I).inverse());
        assertNotEquals(b1.getOrientation(), b2.getOrientation());
    }

    @Test
    public void testAlterVelocity() {

    }

    @Test
    public void alterAngularVelocity() {

    }

    @Test
    public void alterPositionLocally() {

    }

    @Test
    public void alterOrientationLocally() {

    }

    @Test
    public void alterVelocityLocally() {

    }

    @Test
    public void alterAngularVelocityLocally() {

    }

    @Test
    public void testMove() {

    }


}
