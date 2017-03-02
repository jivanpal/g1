package Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import GameLogic.Global;

/**
 * Creates a polygon for a given side of an object
 * @author Dominic
 *
 */
public class PolygonObj {
	private Polygon p;
	private Color c;
	public double avgDist = 0;
	public double light = 1;
	public boolean draw = true;
	private BufferedImage img;
	private boolean imgPresent;
	
	/**
	 * Creates a polygon from given arrays of x and y coordinates
	 * @param x An array of all the x-coordinates of the vertices of the polygon
	 * @param y An array of all the y-coordinates of the vertices of the polygon
	 * @param c The colour of the polygon
	 */
	public PolygonObj(double[] x, double[] y, Color c){
		p = new Polygon();
		for(int i = 0; i < x.length; i++){
			p.addPoint((int)x[i], (int)y[i]);
		}
		this.c = c;
		imgPresent = false;
	}
	
	public PolygonObj(double[] x, double[] y, BufferedImage img){
		p = new Polygon();
		for(int i = 0; i < x.length; i++){
			p.addPoint((int)x[i], (int)y[i]);
		}
		this.c = Color.BLACK;
		this.img = img;
		imgPresent = true;
	}
	
	/**
	 * Updates the position of the polygon
	 * @param x The x-coordinates of the polygon
	 * @param y The y-coordinates of the polygon
	 */
	public void update(double[] x, double[] y){
		p.reset();
		for(int i = 0; i < x.length; i++){
			p.xpoints[i] = (int)x[i];
			p.ypoints[i] = (int)y[i];
			p.npoints = x.length;
		}
	}
	
	//Testing method
	private boolean screenTest(){
		boolean onScreen = false;
		for(int i = 0; i < p.xpoints.length; i++){
			if(p.xpoints[i] >= 0 && p.xpoints[i] < Global.SCREEN_WIDTH && p.ypoints[i] >= 0 && p.ypoints[i] < Global.SCREEN_HEIGHT){
//				System.out.println("onscreen: " + p.xpoints[i] + ", " + p.ypoints[i]);
				onScreen = true;
			}
		}
		return onScreen;
	}
	
	/**
	 * Draws the polygon onto the Screen
	 * @param g The Graphics object
	 */
	public void drawPoly(Graphics g){
		if(draw && !imgPresent && screenTest()){
			g.setColor(new Color((int)(c.getRed() * light), (int)(c.getGreen() * light), (int)(c.getBlue() * light)));
			g.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
//			screenTest();
		}
		else if(draw && imgPresent){
//			javaxt.io.Image image = new javaxt.io.Image("bin/Graphics/spacebox.png");
//			image.setCorners(p.xpoints[0], p.ypoints[0],
//			                 p.xpoints[1], p.ypoints[1],
//			                 p.xpoints[2], p.ypoints[2],
//			                 p.xpoints[3], p.ypoints[3]);
//			BufferedImage bi = image.getBufferedImage();
//			g.drawImage(bi, 0, 0, null);
		}
	}
}
