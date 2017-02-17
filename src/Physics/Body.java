package Physics;

import GameLogic.Global;
import Geometry.*;

public class Body implements Cloneable {
/// FIELDS
    private boolean destroyed = false;
    
    private double m = 1;       // Default mass is 1 kg.
    private double radius = 10; // Defualt radius is 10 m.
    
    private Vector s = Vector.ZERO;             // Default position is at the origin.
    private Rotation orient = Rotation.NONE;    // Default orientation is on the x-y plane,
                                                // pointing along the y-axis.

    private Vector v = Vector.ZERO;     // Default velocity is zero.
    private Vector omega = Vector.ZERO; // Default angular velocity is zero.
    
/// CONSTRUCTORS
    
    /**
     * Create a body with given parameters.
     * @param   mass            The body's mass, in kg.
     * @param   radius          The body' radius, in meters.
     * @param   position        The body's displacement from the origin, in meters.
     * @param   orientation     The body's orientation, given as a rotation from the default
     *              orientation, which is on the x-y plane, pointing along the y-axis.
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
        setMass(mass);
        setRadius(radius);
        setPosition(position);
        setOrientation(orientation);
        setVelocity(velocity);
        setAngularVelocity(angularVelocity);
    }

    /**
     * Create a stationary body with given parameters.
     * @param   mass        The body's mass, in kg.
     * @param   radius      The body' radius, in meters.
     * @param   position    The body's displacement from the origin, in meters.
     * @param   orientation The body's orientation, given as a rotation from the default
     *              orientation, which is on the x-y plane, pointing along the y-axis.
     */
    public Body(double mass, double radius, Vector position, Rotation orientation) {
        setMass(m);
        setRadius(radius);
        setPosition(position);
        setOrientation(orientation);
    }
    
    /**
     * Create a stationary body with given parameters.
     * @param   mass        The body's mass, in kg.
     * @param   radius      The body' radius, in meters.
     * @param   position    The body's displacement from the origin, in meters.
     */
    public Body(double mass, double radius, Vector position) {
        setMass(m);
        setRadius(radius);
        setPosition(position);
    }
    
    /**
     * Create a stationary body at the origin with given parameters.
     * @param   mass    The body's mass, in kg.
     * @param   radius  The body' radius, in meters.
     */
    public Body(double mass, double radius) {
        setMass(mass);
        setRadius(radius);
    }

    /**
     * Create a stationary body of radius 10 m at the origin with given mass.
     * @param   mass    The body's mass, in kg.
     */
    public Body(double mass) {
        setMass(mass);
    }
    
    /**
     * Create a stationary body of mass 1 kg and radius 10 m at the origin.
     */
    public Body() {
        // Do nothing. Creates an instance with the default field values.
    }

/// INSTANCE METHODS
    
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
    
// Getters
    
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
        return radius;
    }

    /**
     * Get the position vector of this instance, in meters.
     */
    public Vector getPosition() {
        return s;
    }

    /**
     * Get the orientation of this instance, as described by
     * a rotation with respect to the reference basis/orientation.
     */
    public Rotation getOrientation() {
        return orient;
    }

    /**
     * Get this instance's velocity, in meters per second.
     */
    public Vector getVelocity() {
        return v;
    }

    /**
     * Get this instance's angular velocity as vector describing whose
     * direction describes the axis of rotation, and whose magnitude
     * describes the right-handed angular speed about that axis in
     * radians per second.
     */
    public Vector getAngularVelocity() {
        return omega;
    }
    
    public Vector getMomentum() {
        return v.scale(m);
    }
    
// Other Useful Getters
    
    /**
     * Get the local basis of this instance, defined relative to the global basis.
     */
    public Basis getBasis() {
        return new Basis(s, orient);
    }
    
    /**
     * Get the unit vector pointing in the body's local rightward direction.
     */
    public Vector getRightVector() {
        return getBasis().getXAxis();
    }
    
    /**
     * Get the unit vector pointing in the body's local forward direction.
     */
    public Vector getForwardVector() {
        return getBasis().getYAxis();
    }
    
    /**
     * Get the unit vector pointing in the body's local upward direction.
     */
    public Vector getUpVector() {
        return getBasis().getZAxis();
    }
    
    /**
     * Get the unit vector pointing in the body's local leftward direction.
     */
    public Vector getLeftVector() {
        return getRightVector().negate();
    }
    
    /**
     * Get the unit vector pointing in the body's local backward direction.
     */
    public Vector getBackwardVector() {
        return getForwardVector().negate();
    }
    
    /**
     * Get the unit vector pointing in the body's local downward direction.
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
     * @param   mass    The new mass, in kg. The mass may be negative, in which
     *      case, any forces exerted on the body will affect it negatively
     *      (that is, if the mass is -<i>m</i>, and a force <i>F</i> is
     *      exerted on it, it will have the same effect as a force -<i>F</i>
     *      exerted on the body at the same point of action if it had mass
     *      <i>m</i>), and gravitational effects due to this body will be
     *      repulsive rather than attractive. 
     */
    public void setMass(double mass) {
        m = mass;
    }
    
    /**
     * Set the radius of this instance.
     * @param   radius  The new radius, in meters.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Set the position of this instance.
     * @param   position    The new position vector, in meters.
     */
    public void setPosition(Vector position) {
        s = position;
    }

    /**
     * Set the orientation of this instance.
     * @param   orientation The new orientation, described as a rotation
     *      from the reference orientation.
     */
    public void setOrientation(Rotation orientation) {
        orient = orientation;
    }

    /**
     * Set the velocity of this instance.
     * @param   velocity    The new velocity vector, in meters per second.
     */
    public void setVelocity(Vector velocity) {
        v = velocity;
    }

    /**
     * Set the angular velocity of this instance.
     * @param   angularVelocity The new angular velocity, given as a
     *      vector describing whose direction describes the axis of rotation,
     *      and whose magnitude describes the right-handed angular speed about
     *      that axis in radians per second.
     */
    public void setAngularVelocity(Vector angularVelocity) {
        omega = angularVelocity;
    }
    
// Alterers

    /**
     * Alter this instance's mass by a given amount.
     * @param   delta   The amount to alter the mass by. Positive values
     *              increase the mass, and negative values decrease it.
     */
    public void alterMass(double delta) {
        m += delta;
    }
    
    public void alterRadius(double delta) {
        radius += delta;
    }

    /**
     * Move the body by a given vector with respect to the global basis.
     */
    public void move(Vector delta) {
        s = s.plus(delta);
    }

    /**
     * Rotate the body by a given rotation defined with respect to the global basis.
     */
    public void rotate(Rotation delta) {
        orient = orient.then(delta);
    }
    
    public void alterVelocity(Vector delta) {
        v = v.plus(delta);
    }

    public void alterAngularVelocity(Vector delta) {
        omega = omega.plus(delta);
    }
    
    /**
     * Given a reference body, alter this body's parameters so that it apparently
     * moves according to its current parameters from the reference fram of the
     * reference body.
     * @param   reference   The reference body.
     */
    public void setReferenceBody(Body reference) {
        move(reference.getPosition());
        rotate(reference.getOrientation());
        setVelocity(reference.getOrientation().apply(v));
        setAngularVelocity(reference.getOrientation().apply(omega));
        alterVelocity(reference.getVelocity());
        alterAngularVelocity(reference.getAngularVelocity());
    }
    
    /**
     * Get the force that would cause this body to "rebound" in accordance with a
     * given coefficient of restitution.
     * @param   lineOfImpact    The line of action of the force; the common normal
     *              between the colliding bodies touching surfaces.
     * @param   restitution     The coefficient of restitution for this collision.
     * @return
     */
    public Vector reboundForce(Vector lineOfImpact, double restitution) {
        /* What the method does:
        Vector affectedVelocityComponent = v.proj(lineOfImpact);
        Vector velocityDelta    = affectedVelocityComponent.negate().scale(restitution);
        Vector momentumDelta    = velocityDelta.scale(m);
        Vector requiredImpulse  = momentumDelta;                                // Impulse = Force * time
        Vector requiredForce    = requiredImpulse.scale(Global.REFRESH_RATE);   // therefore F = I / t
        return requiredForce;
        */
        
        // One liner that does the same thing:
        return v.proj(lineOfImpact).scale(-m * Global.REFRESH_RATE * (1 + restitution));
    }
    
    /**
     * Get the force that would case this body to "rebound" perfectly elastically.
     * Same as `reboundForce(lineOfImpact, 1)`.
     * @param   lineOfImpact    The line of action of the force; the common normal
     *              between the colliding bodies touching surfaces.
     * @return
     */
    public Vector reboundForce(Vector lineOfImpact) {
        return v.proj(lineOfImpact).scale(-m * Global.REFRESH_RATE);
    }
    
// Evolution

    /**
     * Update the body's position and orientation.
     */
    public void update() {
        // ∆s = v ∆t
        move(v.scale(Global.REFRESH_PERIOD));

        // (∆ orient) is proportional to omega and t
        rotate(new Rotation(omega.scale(Global.REFRESH_PERIOD)));
    }

    /**
     * Exert a force of given magnitude on this body at a given displacement
     * from its barycenter. This serves to change the rate-related parameters
     * of the body.
     *
     * @param   f   The force vector to simulate exertion of, in newtons.
     * @param   r   The displacement of the the point of action of the force from
     *              the body's barycenter, in meters.
     */
    public void exertForce(Vector f, Vector r) {
        // ∆v = F ∆t / m
        alterVelocity(f.scale(Global.REFRESH_PERIOD / m));

        // ∆ omega = (r x F) ∆t / m r^2
        alterAngularVelocity(r.cross(f).scale(Global.REFRESH_PERIOD / (m * r.length() * r.length())));
    }
    
    /**
     * Determine whether this body is touching or colliding with another.
     * @param   b   The body the check whether this one is touching.
     * @return  whether the two bodies are touching, as either `true` or `false`.
     */
    public boolean isTouching(Body b) {
        return this.getPosition().minus(b.getPosition()).length() < this.getRadius() + b.getRadius();
    }
    
    /**
     * Exert a force on this body that is equivalent to the force it would experience
     * when colliding with a given body. This takes into account the body's position.
     * In practice, `rebound(<i>b</i>)` is invoked <b>if and only if</b>
     * `<i>this</i>.isTouching(<i>b</i>)`.
     * @param   b   The body to simulate rebounding this body off of. The body <i>b</i>
     *              is unaffected by invocation of this method. In order for this body
     *              (call it body <i>a</i>) and <i>b</i> to both experience a rebounding
     *              effect off of each other, both `<i>a</i>.rebound(<i>b</i>)` and
     *              `<i>b</i>.rebound(<i>a</i>)` must be called <b>exactly once<b> each.
     */
    public void rebound(Body b) {
        if(b == this) {
            return;
        }
        
        Vector lineOfAction = getPosition().minus(b.getPosition());
        this.exertForce(
            reboundForce(lineOfAction),
            lineOfAction.normalise().scale(getRadius())
        );
    }
}
