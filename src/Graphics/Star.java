package Graphics;

import Geometry.Vector;
import Physics.Body;

public class Star extends Body{
	private Vector position;
	
	public Star(int x, int y, int z){
		position = new Vector(x, y, z);
	}
	
	public Vector getPosition(){
		return position;
	}
}
