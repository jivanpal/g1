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

import java.util.Random;

/**
 * Created by James on 22/03/17.
 * Unit tests for the Body.java class.
 */
public class BodyTest {

    Body b1; // 10kg, 10m radius, at origin, no rotation, no velocity, no angular velocity
    Body b2; // 10kg, 10m radius, at (10, 10, 10), rotation Quaternion I, velocity of (1, 1, 1), angular velocity of (1, 1, 1).
    Body clone;

    Random r = new Random();
    static final double COORD_RANGE = 5;
    private Vector randomVector() {
        return new Vector(r.nextDouble()-0.5, r.nextDouble()-0.5, r.nextDouble()-0.5).scale(COORD_RANGE);
    }
    
    private Body randomBody() {
        return new Body(
            r.nextDouble(),
            r.nextDouble(),
            randomVector(),
            new Rotation(randomVector()),
            randomVector(),
            randomVector()
        );
    }
    
    @Before
    public void setUp() {
        b1 = new Body(10, 10, new Vector(0, 0, 0), Rotation.NONE, new Vector(0, 0, 0), new Vector(0, 0, 0));
        b2 = new Body(10, 10, new Vector(10, 10, 10), new Rotation(Quaternion.I), new Vector(1, 1, 1), new Vector(1, 1, 1));
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testUniqueID() {
        Body[] body = new Body[10];
        
        for (int i = 0; i < body.length / 2; i++) {
            body[2*i] = randomBody();
        }
        for (int i = 0; i < body.length / 2; i++) {
            body[2*i+1] = (Body)(body[2*i].clone());
        }
        for (int i = 0; i < body.length; i++) {
            for (int j = 0; j < i; j++) {
                assertNotEquals(body[i].getID(), body[j].getID());
            }
        }
    }

    @Test
    public void testGetPosition() {
        Vector v = randomVector();
        Body b = new Body(r.nextDouble(), r.nextDouble(), v);
        assertEquals(b.getPosition(), v);
    }

    @Test
    public void testAlterPosition() {
        Body b = randomBody();

        Vector[] pos = new Vector[5];
        pos[0] = b.getPosition();
        
        Vector[] vec = new Vector[pos.length];
        vec[0] = Vector.ZERO;
        
        // Several times, move the ship by some vector
        // and store its subsequent position.
        for (int i = 1; i < pos.length; i++) {
            vec[i] = randomVector();
            b.alterPosition(vec[i]);
            pos[i] = b.getPosition();
        }
        
        // Check whether the movement was done correctly
        for (int i = 1; i < pos.length; i++) {
            assertEquals( pos[i], pos[i-1].plus(vec[i]) );
        }
    }

    @Test
    public void testAlterOrientation() {
        Rotation r1 = new Rotation(randomVector());
        Rotation r2 = new Rotation(randomVector());
        Body b = randomBody(); b.setOrientation(r1);
        
        b.alterOrientation(r2);
        assertEquals(b.getOrientation(), r1.then(r2));
    }

    @Test
    public void testAlterVelocity() {
        Vector v = randomVector();
        Vector w = randomVector();
        Body b = randomBody();
        
        b.setVelocity(v);;
        b.alterVelocity(w);
        assertEquals(b.getVelocity(), v.plus(w));
    }

    @Test
    public void alterAngularVelocity() {
        Vector v = randomVector();
        Vector w = randomVector();
        Body b = randomBody();
        b.setAngularVelocity(v);
        
        b.alterAngularVelocity(w);
        assertEquals(b.getAngularVelocity(), v.plus(w));
    }

    @Test
    public void alterPositionLocally() {
        Vector p = randomVector();
        Vector q = randomVector();
        Body b = new Body(
            r.nextDouble(),
            r.nextDouble(),
            p,
            new Rotation(randomVector())
        );
        
        Vector qGlobal = b.getBasis().globaliseDirection(q);
        b.alterPositionLocally(q);
        
        assertEquals(b.getPosition(), p.plus(qGlobal));
        
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
