package Graphics;

import java.awt.Graphics;

public class Point {
	public double x, y, z;
	public double h = 1;
	
	public Point(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point pointPlusVector(Vector v){
		Point q = new Point(x + v.x, y + v.y, z + v.z);
		return q;
	}
	
	public Point pointMinusVector(Vector v){
		Point q = new Point(x - v.x, y - v.y, z - v.z);
		return q;
	}
	
	public Vector pointMinusPoint(Point q){
		Vector v = new Vector(x - q.x, y - q.y, z - q.z);
		return v;
	}
	
	void setPoint(Point p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	void drawPoint(Graphics g){
	}
}
