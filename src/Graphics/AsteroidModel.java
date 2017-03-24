package Graphics;

import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;

/**
 * Creates an Asteroid Model
 * @author Dominic
 *
 */
public class AsteroidModel {
    private Vector position;
    private Rotation orientation;
    private double radius;
    
    private Color[] colors = new Color[4];
    private Vector[] vertices = new Vector[4];
    private int[][] sides = {
        {0, 1, 2},
        {0, 1, 3},
        {0, 2, 3},
        {1, 2, 3}
    };
    
    /**
     * Creates a new Asteroid graphical model
     * @param position The central position of the asteroid
     * @param radius The radius of the asteroid
     * @param orientation The orientation of the asteroid
     */
    public AsteroidModel(Vector position, double radius, Rotation orientation){
	    this.position = position;
	    this.radius = radius;
		this.orientation = orientation;
		
		createPoints();
		
		for(int i = 0; i < 4; i++){
			colors[i] = Color.RED;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}
	
    /**
     * Creates the correct vertices for the asteroid, given the position and rotation
     */
    private void createPoints() {
    	
    	vertices[0] = (                                                                                                        Vector.K.scale(radius));
        vertices[1] = (new Rotation(0,                     Math.toRadians(90), Math.toRadians(90) - Math.acos(-1.0/3)) .apply( Vector.I.scale(radius)) );
        vertices[2] = (new Rotation(Math.toRadians(120),   Math.toRadians(90), Math.toRadians(90) - Math.acos(-1.0/3)) .apply( Vector.I.scale(radius)) );
        vertices[3] = (new Rotation(Math.toRadians(240),   Math.toRadians(90), Math.toRadians(90) - Math.acos(-1.0/3)) .apply( Vector.I.scale(radius)) );
    	
        if(radius != 1.0) {
          for (int i = 0; i < vertices.length; i++) {
              vertices[i] = vertices[i].scale(radius);
          }
        }
        
        orientation.apply(vertices);
    	
        for(int i = 0; i < vertices.length; i++){
        	vertices[i] = position.plus(vertices[i]);
        }
	}
}
