package Geometry;

/**
 * This class allows you to create an object that describes an orthogonal basis
 * in a 3D space whose unit vectors are all unit vectors with respect to the initial
 * basis.
 * @author jivan
 *
 */
public class Basis {
/// FIELDS
	private Vector origin;
	private Rotation orientation;
	
/// CONSTRUCTORS
	/**
	 * Create an object describing a basis with a given origin and orientation.
	 * @param	origin		The origin of the basis with respect to the initial basis.
	 * @param	orientation	A `Rotation` that describes the orientation of the basis
	 * 				with respect to the initial basis.
	 */
	public Basis (Vector origin, Rotation orientation) {
		this.origin = origin;
		this.orientation = orientation;
	}

/// INSTANCE METHODS
	
// Getters
	
	public Vector getOrigin() {
		return origin;
	}
	
	public Rotation getOrientation() {
		return orientation;
	}
	
//	public Matrix getTransformationMatrix() {
//		return new Matrix (
//			a,b,c,d,
//			e,f,g,h,
//			i,j,k,l
//		);
//	}
	
	public Vector getXAxis() {
		return Vector.ZERO;
	}
	
	public Vector getYAxis() {
		return Vector.ZERO;
	}
	public Vector getZAxis() {
		return Vector.ZERO;
	}
	 
	
	
}
