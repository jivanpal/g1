package Physics;

import Geometry.*;

public class Body implements Cloneable {
/// FIELDS
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
    
    public Object clone() {
        return super.clone();
    }
    
// Getters

    public double getMass() {
        return m;
    }
    
    public double getRadius() {
        return radius;
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
    
    public Basis getBasis() {
        return new Basis(s, orient);
    }

// Setters

    public void setMass(double mass) {
        m = mass;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
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
        setVelocity(reference.getOrientation().rotate(v));
        setAngularVelocity(reference.getOrientation().rotate(omega));
        alterVelocity(reference.getVelocity());
        alterAngularVelocity(reference.getAngularVelocity());
    }

// Evolution

    /**
     * Update the body's position and orientation, as if a given amount of
     * time has passed.
     *
     * @param   t   The amount of time to simulate the passage of, in seconds.
     */
    public void update(double t) {
        // ∆s = v ∆t
        move(v.scale(t));

        // (∆ orient) is proportional to omega and t
        rotate(new Rotation(omega.scale(t)));
    }

    /**
     * Change the rate-related parameters of the body as if a force
     * of given magnitude acted on it at a given displacement from its barycenter.
     *
     * @param   t   The amount of time to simulate the passage of, in seconds.
     * @param   f   The force to simulate exertion of, in newtons.
     * @param   r   The displacement of the the point of action of the force from
     *              the body's barycenter, in meters.
     */
    public void exertForce(double t, Vector f, Vector r) {
        // ∆v = F ∆t / m
        alterVelocity(f.scale(t / m));

        // ∆ omega = (r x F) ∆t / m r^2
        alterAngularVelocity(r.cross(f).scale(t / (m * r.length() * r.length())));
    }
    
    /**
     * Determine whether this body is touching or colliding with another.
     * @param   b   The body the check whether this one is touching.
     * @return  whether the two bodies are touching, as either `true` or `false`.
     */
    public boolean isTouching(Body b) {
        return this.getPosition().minus(b.getPosition()).length() < this.getRadius() + b.getRadius();
    }
}
