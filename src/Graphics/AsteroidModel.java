package Graphics;

import java.awt.Color;
import java.util.Random;
import Geometry.*;

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
	
    private void createPoints() {
        vertices[0] = position.plus(Vector.K);
        vertices[1] = position.plus(new Rotation(0,Math.toRadians(90),Math.toRadians(90)-Math.acos(-1.0/3)).apply(Vector.I));
        vertices[2] = position.plus(new Rotation(Math.toRadians(120),Math.toRadians(90),Math.toRadians(90)-Math.acos(-1.0/3)).apply(Vector.I));
        vertices[3] = position.plus(new Rotation(Math.toRadians(240),Math.toRadians(90),Math.toRadians(90)-Math.acos(-1.0/3)).apply(Vector.I));
        
        if(radius != 1.0) {
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i].scale(radius);
            }
        }
        
        orientation.apply(vertices);
        
//		Random generation that I'm temporarily giving up on
		
//		Random r = new Random();
//		for(int i = 0; i < 4; i++){
//			vertices[i] = new Vector((r.nextDouble() - 0.5) * size + x, (r.nextDouble() - 0.5) * size + y, (r.nextDouble() - 0.5) * size + z);
//			orientation.apply(vertices[i]);
//		}
	}
}
