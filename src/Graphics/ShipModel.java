package Graphics;

import java.awt.Color;

import Geometry.Rotation;
import Geometry.Vector;

/**
 * A graphical representation of a Ship
 * @author Dominic
 *
 */
public class ShipModel {
	private double x, y, z, size;
	private Vector[] vertices = new Vector[45];
	private int[][] sides = {	{0, 4, 1},
								{0, 4, 2},
								{0, 1, 2},
								{1, 4 ,3},
								{2, 3, 4},
								{1, 3, 2},
								{5, 8, 6},
								{5, 6, 7},
								{6, 8, 7},
								{9, 10, 12},
								{9, 11, 10},
								{10, 11, 12},
								{13, 14, 15, 16},
								{13, 16, 20, 17},
								{17, 20, 19, 18},
								{14, 18, 19, 15},
								{15, 22, 21, 16},
								{16, 21, 24, 20},
								{19, 20, 24, 23},
								{15, 19, 23, 22},
								{13, 17, 18, 14},
								{21, 22, 23, 24},
								{25, 26, 27, 28},
								{25, 28, 32, 29},
								{29, 32, 31, 30},
								{26, 30, 31, 27},
								{27, 34, 33, 28},
								{28, 33, 36, 35},
								{31, 32, 36, 35},
								{27, 31, 35, 34},
								{25, 29, 30, 26},
								{33, 34, 35, 36},
								{37, 39, 40},
								{37, 40, 38},
								{37, 38, 39},
								{38, 40, 39},
								{41, 42, 44},
								{41, 44, 43},
								{41, 43, 42},
								{42, 43, 44}};
	private Color teamColor;
	private Rotation orientation;
	
	
	/**
	 * Creates a new graphical representation of a ship, and adds it to the screen
	 * @param v x, y and z coordinates of its position
	 * @param size Size of the object
	 * @param orientation The orientation of the object
	 * @param teamNumber The number of the team piloting the ship. This affects the colour of their wing-tips
	 */
	public ShipModel(Vector v, double size, Rotation orientation, int teamNumber) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		this.orientation = orientation;
		
		switch(teamNumber){
			case 1:
				teamColor = Color.RED;
			case 2:
				teamColor = Color.BLUE;
			case 3:
				teamColor = Color.YELLOW;
			case 4:
				teamColor = Color.GREEN;
			default:
				teamColor = Color.BLACK;
		}
		
		Color[] colors = {Color.GRAY, Color.GRAY, Color.GRAY, Color.CYAN, Color.CYAN, Color.CYAN, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
							Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.ORANGE, Color.ORANGE,
							Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.ORANGE, Color.ORANGE,
							teamColor, teamColor, teamColor, teamColor, teamColor, teamColor, teamColor, teamColor};
		
		createPoints();
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}

	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Vector(0, size/2, 0);
		vertices[1] = new Vector(size/5, -size/2, 0);
		vertices[2] = new Vector(-size/5, -size/2, 0);
		vertices[3] = new Vector(0, -size/2, size/5);
		vertices[4] = new Vector(0, 0, size/10);
		vertices[5] = new Vector(size/20, size/4, size/20);
		vertices[6] = new Vector(size/2, -size/2, size/20);
		vertices[7] = new Vector(size*3/20, -size/4, size/20);
		vertices[8] = new Vector(size/10, -size/4, size/10);
		vertices[9] = new Vector(-size/20, size/4, size/20);
		vertices[10] = new Vector(-size/2, -size/2, size/20);
		vertices[11] = new Vector(-size*3/20, -size/4, size/20);
		vertices[12] = new Vector(-size/10, -size/4, size/10);
		vertices[13] = new Vector(size*3/10, 0, size/10);
		vertices[14] = new Vector(size/5, 0, size/10);
		vertices[15] = new Vector(size/5, -size*2/5, size/10);
		vertices[16] = new Vector(size*3/10, -size*2/5, size/10);
		vertices[17] = new Vector(size*3/10, 0, 0);
		vertices[18] = new Vector(size/5, 0, 0);
		vertices[19] = new Vector(size/5, -size*2/5, 0);
		vertices[20] = new Vector(size*3/10, -size*2/5, 0);
		vertices[21] = new Vector(size*7/20, -size*9/20, size*3/20);
		vertices[22] = new Vector(size*3/20, -size*9/20, size*3/20);
		vertices[23] = new Vector(size*3/20, -size*9/20, -size/20);
		vertices[24] = new Vector(size*7/20, -size*9/20, -size/20);
		vertices[25] = new Vector(-size*3/10, 0, size/10);
		vertices[26] = new Vector(-size/5, 0, size/10);
		vertices[27] = new Vector(-size/5, -size*2/5, size/10);
		vertices[28] = new Vector(-size*3/10, -size*2/5, size/10);
		vertices[29] = new Vector(-size*3/10, 0, 0);
		vertices[30] = new Vector(-size/5, 0, 0);
		vertices[31] = new Vector(-size/5, -size*2/5, 0);
		vertices[32] = new Vector(-size*3/10, -size*2/5, 0);
		vertices[33] = new Vector(-size*7/20, -size*9/20, size*3/20);
		vertices[34] = new Vector(-size*3/20, -size*9/20, size*3/20);
		vertices[35] = new Vector(-size*3/20, -size*9/20, -size/20);
		vertices[36] = new Vector(-size*7/20, -size*9/20, -size/20);
		vertices[37] = new Vector(size/2, -size*2/5, size/40);
		vertices[38] = new Vector(size*21/40, -size*3/5, size/40);
		vertices[39] = new Vector(size*19/40, -size*3/5, size/40);
		vertices[40] = new Vector(size/2, -size*3/5, size*3/40);
		vertices[41] = new Vector(-size/2, -size*2/5, size/40);
		vertices[42] = new Vector(-size*21/40, -size*3/5, size/40);
		vertices[43] = new Vector(-size*19/40, -size*3/5, size/40);
		vertices[44] = new Vector(-size/2, -size*3/5, size*3/40);
		
		orientation.apply(vertices);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vector(vertices[i].getX() + x, vertices[i].getY() + y, vertices[i].getZ() + z);
		}
	}
}
