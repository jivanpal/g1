package Physics;

import Geometry.*;

public class Entity {
/// FIELDS
    private double m        = 1;                // Default mass is 1 kg.
    private Vector s        = Vector.ZERO;      // Default position is the origin.
    private Rotation orient = Rotation.NONE;    // Default orientation is on the x-y plane, pointing along the y-axis.
    
    private Vector v        = Vector.ZERO;      // Default velocity is zero.
    private Vector omega    = Vector.ZERO;      // Default angular velocity is zero.
    
/// CONSTRUCTORS
    
    /**
     * Create a new entity with the specified parameters.
     * @param   mass            The entity's mass, in kg.
     * @param   position        The entity's displacement from the origin, in meters.
     * @param   orientation     The entity's orientation, given as a rotation from
     *              the default orientation, which is on the x-y plane, pointing
     *              along the y-axis.
     * @param   velocity        The entity's velocity, in meters per second.
     * @param   angularVelocity The entity's right-handed angular velocity, in radians per second.
     */
    public Entity (
        double      mass,
        Vector      position,
        Rotation    orientation,
        Vector      velocity,
        Vector      angularVelocity
    ) {
        setMass(mass);
        setPosition(position);
        setOrientation(orientation);
        setVelocity(velocity);
        setAngularVelocity(angularVelocity);
    }
    
    /**
     * Create a new entity with the specified parameters.
     * @param   mass        The entity's mass, in kg.
     * @param   position    The entity's displacement from the origin, in meters.
     * @param   orientation The entity's orientation, given as a rotation from
     *              the default orientation, which is on the x-y plane, pointing
     *              along the y-axis.
     */
    public Entity (double mass, Vector position, Rotation orientation) {
        setMass(m);
        setPosition(position);
        setOrientation(orientation);
    }
    
    /**
     * Create a new entity with the specified parameters.
     * @param   mass    The entity's mass, in kg.
     */
    public Entity(double mass) {
        setMass(mass);
    }
    
/// INSTANCE METHODS
    
// Getters
    
    public double getMass() {
        return m;
    }
    
    public Vector getPosition() {
        return s;
    }
    
    public Rotation getOrientation() {
        return orient;
    }
    
    public Vector getVelocity() {
        return v;
    }
    
    public Vector getAngularVelocity() {
        return omega;
    }
    
// Setters
    
    public void setMass(double mass) {
        m = mass;
    }
    
    public void setPosition(Vector position) {
        s = position;
    }
    
    public void setOrientation(Rotation orientation) {
        orient = orientation;
    }
    
    public void setVelocity(Vector velocity) {
        v = velocity;
    }
    
    public void setAngularVelocity(Vector angularVelocity) {
        omega = angularVelocity;
    }
    
// Alterers
    
    public void alterMass(double delta) {
        m += delta;
    }
    
    public void move(Vector delta) {
        s = s.plus(delta);
    }
    
    public void rotate(Rotation delta) {
        orient = orient.then(delta);
    }
    
    public void changeVelocity(Vector delta) {
        v = v.plus(delta);
    }
    
    public void changeAngularVelocity(Vector delta) {
        omega = omega.plus(delta);
    }
    
// methods
    
    /**
     * Update the entity's position and orientation,
     * as if a given amount of time has passed.
     *
     * @param t The amount of time to simulate the passage of, in seconds.
     */
    public void update(double t) {
        // ∆s = v ∆t
        move( v.scale(t) );
        
        // (∆ orient) is proportional to omega and 1/t
        rotate( new Rotation(omega.scale(1/t)) );
    }
    
    /**
     * Change the change-related parameters of the entity,
     * as if a given force is acting on it.
     *
     * @param t The amount of time to simulate the passage of, in seconds.
     * @param f The force to simulate exertion of, in newtons.
     * @param s The displacement of the the point of action of the
     *   force from the body's barycenter, in meters.
     */
    public void exertForce(double t, Vector f, Vector r) {
        // ∆v = F ∆t / m
        changeVelocity( f.scale(t/m) );
        
        // ∆ omega = (r x F) ∆t / m r^2
        changeAngularVelocity( r.cross(f).scale( t/(m * r.length() * r.length()) ) );
    }
}
