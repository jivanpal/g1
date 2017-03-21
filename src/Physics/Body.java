package Physics;

import java.io.Serializable;
import GameLogic.Global;
import Geometry.*;

/**
 * A class of objects that describe physical bodies in the game environment.
 * In the scope of this document, XYZ refers to the default basis of a body
 * described by this class, and X'Y'Z' refers to the actual basis of an instance
 * of this class. Given the body has basis X'Y'Z', then, by convention, the
 * barycenter of the body is O', and the body is oriented so that its right-vector
 * is I', its front-vector is J', and its normal- or up-vector is K'.
 * 
 * @author jivan
 */
public class Body implements Cloneable, Serializable {
/// FIELDS
    public static int nextID = 0;
    
    private int ID;
    private boolean destroyed = false;
    
    /**
     * Default mass is 1 kg.
     */
    private double m = 1;
    
    /**
     * Default body radius is 10 m.
     */
    private double r = 10;
    
    /**
     * Default position is at the origin. Default orientation is with
     * I as right-vector, J as front-vector, and K as normal vector.
     */
    private Basis b = new Basis(Vector.ZERO, Rotation.NONE);
    
    /**
     * Default linear velocity is 0.
     */
    private Vector v = Vector.ZERO;
    
    /**
     * Default angular velocity is 0.
     */
    private Vector w = Vector.ZERO;
    
/// CONSTRUCTORS
    
    /**
     * Create a body with given parameters.
     * @param   mass            The body's mass, in kilograms.
     * @param   radius          The body's radius, in meters.
     * @param   position        The body's displacement from the origin, in meters.
     * @param   orientation     The body's orientation, given as a rotation from the default orientation.
     * @param   velocity        The body's velocity, in meters per second.
     * @param   angularVelocity The body's right-handed angular velocity, in radians per second.
     */
    public Body (
        double      mass,
        double      radius,
        Vector      position,
        Rotation    orientation,
        Vector      velocity,
        Vector      angularVelocity
    ) {
        ID = nextID++;
        setMass(mass);
        setRadius(radius);
        setPosition(position);
        setOrientation(orientation);
        setVelocity(velocity);
        setAngularVelocity(angularVelocity);
    }

    /**
     * Create a stationary body with given parameters.
     * @param   mass        The body's mass, in kilograms.
     * @param   radius      The body' radius, in meters.
     * @param   position    The body's displacement from the origin, in meters.
     * @param   orientation The body's orientation, given as a rotation from the default
     *              orientation, which is on the x-y plane, pointing along the y-axis.
     */
    public Body(double mass, double radius, Vector position, Rotation orientation) {
        ID = nextID++;
        setMass(mass);
        setRadius(radius);
        setPosition(position);
        setOrientation(orientation);
    }
    
    /**
     * Create a stationary body with given parameters.
     * @param   mass        The body's mass, in kilograms.
     * @param   radius      The body' radius, in meters.
     * @param   position    The body's displacement from the origin, in meters.
     */
    public Body(double mass, double radius, Vector position) {
        ID = nextID++;
        setMass(m);
        setRadius(radius);
        setPosition(position);
    }
    
    /**
     * Create a stationary body at the origin with given parameters.
     * @param   mass    The body's mass, in kilograms.
     * @param   radius  The body' radius, in meters.
     */
    public Body(double mass, double radius) {
        ID = nextID++;
        setMass(mass);
        setRadius(radius);
    }

    /**
     * Create a stationary body of radius 10 m at the origin with given mass.
     * @param   mass    The body's mass, in kilograms.
     */
    public Body(double mass) {
        ID = nextID++;
        setMass(mass);
    }
    
    /**
     * Create a stationary body of mass 1 kg and radius 10 m at the origin.
     */
    public Body() {
        ID = nextID++;
    }

/// INSTANCE METHODS
    
    public Object clone() throws CloneNotSupportedException{
        Body b = (Body)super.clone();
        b.ID = nextID++;
        return (Object)b;
    }
    
// Getters
    
    /**
     * Get the unique ID of this instance.
     */
    public int getID() {
        return ID;
    }
    
    /**
     * Find out whether this instance still exists in the game.
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Get the mass of this instance.
     */
    public double getMass() {
        return m;
    }
    
    /**
     * Get the radius of this instance, in meters.
     */
    public double getRadius() {
        return r;
    }

    /**
     * Get the position vector of this instance in the basis XYZ, in meters.
     */
    public Vector getPosition() {
        return b.getO();
    }

    /**
     * Get the orientation of this instance with respect to XYZ.
     */
    public Rotation getOrientation() {
        return b.getR();
    }

    /**
     * Get the velocity of this instance in the the basis XYZ, in meters per second.
     */
    public Vector getVelocity() {
        return v;
    }

    /**
     * Get this instance's angular velocity in the basis XYZ, in radians per second.
     * @return this instance's angular velocity, described by a vector
     * whose direction describes the axis of rotation, and whose magnitude
     * describes the angular speed about that axis, in radians per second,
     * as per the right-hand rule.
     */
    public Vector getAngularVelocity() {
        return w;
    }
    
    /**
     * Get the linear momentum of this instance in the basis XYZ, in kg m / s.
     */
    public Vector getMomentum() {
        return v.scale(m);
    }
    
// Other Useful Getters
    
    /**
     * Get the basis X'Y'Z', 
     */
    public Basis getBasis() {
        return b;
    }
    
    /**
     * Get this instance's right vector; get I' in the basis XYZ.
     */
    public Vector getRightVector() {
        return b.getI();
    }
    
    /**
     * Get this instance's front vector; get J' in the basis XYZ.
     */
    public Vector getFrontVector() {
        return b.getJ();
    }
    
    /**
     * Get this instance's up vector; get K' in the basis XYZ.
     */
    public Vector getUpVector() {
        return b.getK();
    }
    
    /**
     * Get this instance's left vector; get -I' in the basis XYZ.
     */
    public Vector getLeftVector() {
        return getRightVector().negate();
    }
    
    /**
     * Get this instance's back vector; get -J' in the basis XYZ.
     */
    public Vector getRearVector() {
        return getFrontVector().negate();
    }
    
    /**
     * Get this instance's down vector; get -K' in the basis XYZ.
     */
    public Vector getDownVector() {
        return getUpVector().negate();
    }

// Setters

    /**
     * Mark this object as having been destroyed / not existing any more.
     * This is used by the game map to keep track of which game objects
     * need to be removed from the map due to being destroyed in-game.
     */
    public void destroy() {
        destroyed = true;
    }
    
    /**
     * Set the mass of this instance.
     * @param mass the new mass, in kg. The mass may be negative, in which
     *      case any forces exerted on the body will affect it negatively
     *      (that is, if the mass is -<i>m</i>, and a force <i>F</i> is
     *      exerted on it, it will have the same effect as a force -<i>F</i>
     *      exerted on the body at the same point of action if it had mass
     *      <i>m</i>). In this body's gravitation field, bodies of like-signed
     *      mass are attracted to it, and bodies of oppositely signed mass are
     *      repelled by it.
     */
    public void setMass(double newMass) {
        m = newMass;
    }
    
    /**
     * Set the radius of this instance.
     * @param newRadius the new radius, in meters.
     */
    public void setRadius(double newRadius) {
        r = newRadius;
    }

    /**
     * Set the position of this instance.
     * @param newPosition the new position vector in the basis XYZ, in meters.
     */
    public void setPosition(Vector newPosition) {
        b.setO(newPosition);
    }

    /**
     * Set the orientation of this instance.
     * @param newOrientation the new orientation, described as a rotation from the default orientation.
     */
    public void setOrientation(Rotation newOrientation) {
        b.setR(newOrientation);
    }
    
    /**
     * Set the position and orientation of this instance to those of a given basis.
     * @param newBasis the new basis for this instance.
     */
    public void setBasis(Basis newBasis) {
        b = newBasis;
    }

    /**
     * Set the velocity of this instance.
     * @param newVelocity the new velocity vector in the basis XYZ, in meters per second.
     */
    public void setVelocity(Vector newVelocity) {
        v = newVelocity;
    }

    /**
     * Set the angular velocity of this instance.
     * @param newAngularVelocity the new angular velocity, given as a
     *      vector describing whose direction describes the axis of rotation,
     *      and whose magnitude describes the angular speed about that axis
     *      in radians per second, as per the right-hand rule.
     */
    public void setAngularVelocity(Vector newAngularVelocity) {
        w = newAngularVelocity;
    }
    
// General alterers

    /**
     * Alter this instance's mass by a given amount.
     * @param delta the amount to alter the mass by, in kg. Positive
     *      values increase the mass, and negative values decrease it.
     */
    public void alterMass(double delta) {
        m += delta;
    }
    
    /**
     * Alter this instance's radius by a given amount.
     * @param delta the amount to alter the radius by, in meters. Positive
     *      values increase the radius, and negative values decrease it.
     */
    public void alterRadius(double delta) {
        r += delta;
    }
    
// Alterers with respect to XYZ

    /**
     * Alter this instance's position by a given vector in the basis XYZ, in meters.
     * @param delta the vector to move this instance by, in meters, in the basis XYZ.
     */
    public void alterPosition(Vector delta) {
        b.setO(b.getO().plus(delta));
    }

    /**
     * Rotate this instance by a given rotation with respect to XYZ.
     * @param delta the rotation to apply to this instance with respect to XYZ.
     */
    public void alterOrientation(Rotation delta) {
        b.setR(b.getR().then(delta));
    }
    
    /**
     * Alter this instance's velocity by a given vector in the basis XYZ, in meters.
     * @param delta the vector to alter this instance's velocity by, expressed in
     *      the basis XYZ in meters.
     */
    public void alterVelocity(Vector delta) {
        v = v.plus(delta);
//        System.err.println("Body #"+ID+": ∆v = "+delta+".");
    }

    /**
     * Alter this instance's angular velocity vector by a given vector in the basis XYZ, in
     * radians per second.
     * @param delta the vector to alter this instance's angular velocity vector by,
     *      expressed in the basis XYZ, in radians per second.
     */
    public void alterAngularVelocity(Vector delta) {
        w = w.plus(delta);
//        System.err.println("Body #"+ID+": ∆w = "+delta+".");
    }
    
// Alterers with respect to X'Y'Z'
    
    /**
     * Alter this instance's position by a given vector in the basis X'Y'Z', in meters.
     * @param delta the vector to move this instance by, in meters, in the basis X'Y'Z'.
     */
    public void alterPositionLocally(Vector delta) {
        alterPosition(b.globaliseDirection(delta));
    }
    
    /**
     * Rotate this instance by a given rotation with respect to X'Y'Z'.
     * @param delta the rotation to apply to this instance with respect to X'Y'Z'.
     */
    public void alterOrientationLocally(Rotation delta) {
        alterOrientation(new Rotation(b.globaliseDirection(delta.getAxisAngle())));
    }
    
    /**
     * Alter this instance's velocity by a given vector in the basis X'Y'Z', in meters.
     * @param delta the vector to alter this instance's velocity by, expressed in
     *      the basis X'Y'Z' in meters.
     */
    public void alterVelocityLocally(Vector delta) {
        alterVelocity(b.globaliseDirection(delta));
    }
    
    /**
     * Alter this instance's angular velocity vector by a given vector in the basis X'Y'Z', in
     * radians per second.
     * @param delta the vector to alter this instance's angular velocity vector by,
     *      expressed in the basis X'Y'Z', in radians per second.
     */
    public void alterAngularVelocityLocally(Vector delta) {
        alterAngularVelocity(b.globaliseDirection(delta));
    }
    
    /**
     * Move this instance by a given vector in the basis X'Y'Z'. Same as `alterPositionLocally(delta)`.
     * @param delta the vector to move this instance by, in meters, in the basis X'Y'Z'.
     */
    public void move(Vector delta) {
        alterPositionLocally(delta);
    }
    
    /**
     * Rotate this instance by a given rotation with respect to X'Y'Z'. Same as `alterOrientationLocally(delta)`.
     * @param delta the rotation to apply to this instance with respect to X'Y'Z'.
     */
    public void rotate(Rotation delta) {
        alterOrientationLocally(delta);
    }
    
// Change of basis
    
    /**
     * Given a reference body, alter this body's parameters so that it apparently
     * moves according to its current parameters from the reference frame of the
     * reference body.
     * @param origin the body that this instance will be set to originate from; the
     *      body whose basis to treat as the global basis.
     */
    public void setOriginBody(Body origin) {
        // Change description of velocities from
        // global reference frame to local reference frame,
        // as the local velocities should remain unchanged.
        v = b.localiseDirection(v);
        w = b.localiseDirection(w);
        
        // Change position and orientation
        b = origin.getBasis().then(b);
        
        //   Change description of velocities back from
        // local reference frame to global reference frame,
        // as this is what the implementation requires.
        //   Also add the velocities of the origin-body to
        // this instance, so that this instance moves relative
        // to the origin-body as intended.
        v = b.globaliseDirection(v).plus(origin.getVelocity());
        w = b.globaliseDirection(w).plus(origin.getAngularVelocity());
    }
    
// Old force calculators
    
//    /**
//     * Get the force that would cause this body to "rebound" in accordance with a
//     * given coefficient of restitution.
//     * @param lineOfImpact the line of action of the force; the common normal
//     *      between the colliding bodies touching surfaces.
//     * @param restitution the coefficient of restitution for this collision.
//     * @return the corresponding force vector in the basis XYZ, in newtons.
//     */
//    public Vector reboundForce(Vector lineOfImpact, double restitution) {
//        /* What the method does:
//        Vector affectedVelocityComponent = v.proj(lineOfImpact);
//        Vector velocityDelta    = affectedVelocityComponent.negate().scale(restitution);
//        Vector momentumDelta    = velocityDelta.scale(m);
//        Vector requiredImpulse  = momentumDelta;                                // Impulse = Force * time
//        Vector requiredForce    = requiredImpulse.scale(Global.REFRESH_RATE);   // therefore F = I / t
//        return requiredForce;
//        */
//        
//        // One liner that does the same thing:
//        return v.proj(lineOfImpact).scale(-m * Global.REFRESH_RATE * (1 + restitution));
//    }
//    
//    /**
//     * Get the force that would case this body to "rebound" perfectly elastically.
//     * Same as `reboundForce(lineOfImpact, 1)`.
//     * @param   lineOfImpact    The line of action of the force; the common normal
//     *              between the colliding bodies touching surfaces.
//     * @return
//     */
//    public Vector reboundForce(Vector lineOfImpact) {
//        return v.proj(lineOfImpact).scale(-m * Global.REFRESH_RATE);
//    }
    
// Force application
    
    /**
     * Exert a force <i>F</i> on this body at a given displacement <i>R</i> from its
     * barycenter, as described by vectors with respect to the basis XYZ, thus changing
     * this instance's parameters appropriately.
     *
     * @param   f   The vector describing <i>F</i> in the basis XYZ, in newtons.
     * @param   r   The vector describing <i>R</i> in the basis XYZ, in meters.
     */
    public void exertForce(Vector f, Vector r) {
        // ∆v = F ∆t / m
        alterVelocity(f.scale(Global.REFRESH_PERIOD / m));

        // ∆w = (r x F) ∆t / m r^2
        alterAngularVelocity(r.cross(f).scale(Global.REFRESH_PERIOD / (m * r.length() * r.length())));
    }
    
// Evolution

    /**
     * Update the body's position and orientation.
     */
    public void update() {
        // ∆s = v ∆t
        alterPosition(v.scale(Global.REFRESH_PERIOD));

        // (∆ orient) is proportional to w and t
        alterOrientation(new Rotation(w.scale(Global.REFRESH_PERIOD)));
    }
}
