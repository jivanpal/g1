package JUnit;

import GameLogic.Bullet;
import GameLogic.Ship;
import GameLogic.Weapon;
import Geometry.Vector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by James on 21/03/17.
 */
public class WeaponTest {
    Weapon weaponWithAmmo;
    Weapon weaponWithoutAmmo;
    Bullet bullet;
    Ship ship;

    @Before
    public void setUp() {
        ship = new Ship("test1", "test2");
        bullet = new Bullet(10, 10, 10, 10, 10, new Vector(10, 10, 10), new Vector(10, 10, 10));
        weaponWithAmmo = new Weapon(ship, bullet, 100, 2, 10, 10, 10);
        weaponWithoutAmmo = new Weapon(ship, bullet, 100, 0, 10, 10, 0);
    }

    @After
    public void tearDown() {
        ship = null;
        bullet = null;
        weaponWithAmmo = null;
    }

    @Test
    public void testIncreaseAmmo() {
        int current = weaponWithAmmo.getAmmoLevel();
        weaponWithAmmo.increaseAmmo();
        int newAmmo = weaponWithAmmo.getAmmoLevel();
        assertTrue(current + 1 == newAmmo);
    }

    @Test
    public void testCanFire() {
        assertTrue("Weapon can fire - has ammo and no cooldown", weaponWithAmmo.canFire());
        assertFalse("Weapon cannot fire - has no ammo", weaponWithoutAmmo.canFire());

        weaponWithAmmo.fire();
        assertFalse("Weapon cannot fire - on cooldown", weaponWithAmmo.canFire());
        weaponWithAmmo.increaseAmmo();
    }

    @Test
    public void testFire() {
        assertTrue("Cannot fire - no ammo. Returns null.", weaponWithoutAmmo.fire() == null);

        int currentAmmo = weaponWithAmmo.getAmmoLevel();
        Bullet b = weaponWithAmmo.fire();
        int newAmmo = weaponWithAmmo.getAmmoLevel();

        assertTrue("Ammo lowered", newAmmo < currentAmmo);
        assertFalse("Weapon with ammo cannot fire", weaponWithAmmo.canFire());
    }
}
