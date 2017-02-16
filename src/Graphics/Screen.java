package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import GameLogic.Map;
import GameLogic.Ship;
import Physics.Body;

/**
 * The viewport of the ship, contains the camera and all objects to be rendered by it
 * @author Dominic
 *
 */
public class Screen extends JPanel{
	
	private double sleepTime = 1000/GameLogic.Global.REFRESH_RATE, lastRefresh = 0;
	public static Point viewFrom;
	public static Point viewTo;
	public static Vector lightDir;
	public static double[][] cameraSystem, worldToCamera, CM;
	public static Vector N, U, V;
	
	private double lightPosition;
	private static double moveSpeed = 0.01, verticalLook = 0;
	private double verticalLookSpeed = 0.01, horizontalLookSpeed = 0.01;
	private double r;
	
	public static int nPoly = 0, nPoly3D = 0;
	public static ArrayList<Poly3D> poly3Ds = new ArrayList<Poly3D>();
	int drawOrder[];
	boolean w, a, s, d, e, q;
	
	private Map map;
	private String nickname;
	private int shipIndex;
	
	public Screen(String nickname){
		
		this.nickname = nickname;
		map = new Map(0, 0, 0);
		
		//Create starting vectors
		viewFrom = new Point(0, 0, 0);
		viewTo = new Point(0, 0, 1);
		lightDir = new Vector(1, 1, 1);
		lightDir.normalise();
		
		//Create camera vectors
		N = viewTo.pointMinusPoint(viewFrom);
		N.normalise();
		U = new Vector(0, 1, 0);
		U.normalise();
		V = U.crossProduct(N);
		V.normalise();
		U = N.crossProduct(V);
		U.normalise();
		
		cameraSystem = new double[][] { {V.x,  V.y,  V.z,  0},
										{U.x,  U.y,  U.z,  0},
										{N.x, N.y, N.z, 0},
										{0,    0,    0,    1}};
										
		CM = Matrix.getCM(viewFrom, V, U, N, 2);
		Matrix.printMatrix(CM);
						
		Matrix.printMatrix(cameraSystem);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){
		
		//Draw the background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
		
		//Perform camera calculations based on current keypresses
		camera();
		Calculations.setInfo();
		setLight();
		
		//Draw all polygons onto the screen
		nPoly = poly3Ds.size();
		
		for(int i = 0; i < nPoly; i++){
			poly3Ds.get(i).update();
		}
		
		setDrawOrder();
		
		for(int i = 0; i < nPoly; i++){
//			System.out.println("Drawing Polygon " + i);
			poly3Ds.get(drawOrder[i]).poly.drawPoly(g);
		}
		
		//Draw debugging information
		Vector camCoords = Matrix.multiplyVector(cameraSystem, new Vector(0, 0, 0));
		g.setColor(Color.WHITE);
		g.drawString("x: " + viewFrom.x + ", y: " + viewFrom.y + ", z: " + viewFrom.z + " x: " + camCoords.x + ", y: " + camCoords.y + ", z: " + camCoords.z, 40, 40);
		g.drawString("x: " + viewTo.x + ", y: " + viewTo.y + ", z: " + viewTo.z, 40, 60);
		g.drawString("r: " + r + " vert: " + verticalLook, 40, 80);
		g.drawLine((int)getWidth()/2 - 5, (int)getHeight()/2, (int)getWidth()/2 + 5, (int)getHeight()/2);
		g.drawLine((int)getWidth()/2, (int)getHeight()/2 - 5, (int)getWidth()/2, (int)getHeight()/2 + 5);
		g.drawString("V: " + V.x + ", " + V.y + ", " + V.z, 40, 100);
		g.drawString("U: " + U.x + ", " + U.y + ", " + U.z, 40, 120);
		g.drawString("N: " + N.x + ", " + N.y + ", " + N.z, 40, 140);
		
		g.setColor(Color.RED);
		Point origin = new Point(getWidth() - 40, 40, 1);
		Point uLine = (new Point(0, 0, 0)).pointPlusVector(U.scale(20, 20, 20));
		Point uLine2 = Matrix.multiplyPoint(CM, uLine);
		g.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + (uLine2.x)), (int)(origin.y + (-uLine2.y)));
		g.setColor(Color.BLUE);
		Point vLine = (new Point(0, 0, 0)).pointPlusVector(V.scale(20, 20, 20));
		Point vLine2 = Matrix.multiplyPoint(CM, vLine);
		g.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + (vLine2.x)), (int)(origin.y + (-vLine2.y)));
		g.setColor(Color.GREEN);
		Point nLine = (new Point(0, 0, 0)).pointPlusVector(N.scale(20, 20, 20));
		Point nLine2 = Matrix.multiplyPoint(CM, nLine);
		g.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + (nLine2.x)), (int)(origin.y + (-nLine2.y)));
		
		sleepAndRefresh();
	}
	
	/**
	 * If it has been longer than the sleepTime since the last refresh, repaint is called
	 */
	public void sleepAndRefresh(){
		while(true){
			if(System.currentTimeMillis() - lastRefresh > sleepTime){
				lastRefresh = System.currentTimeMillis();
				repaint();
				break;
			}
			else{
				try{
					Thread.sleep((long)(sleepTime - (System.currentTimeMillis() - lastRefresh)));
				}
				catch(Exception e){
					
				}
			}
		}
	}
	
	/**
	 * Create the order that the polygons should be drawn in in order to make sure hidden sides are hidden
	 */
	private void setDrawOrder(){
		double[] k = new double[nPoly];
		drawOrder = new int[nPoly];
		nPoly = poly3Ds.size();
		
		for(int i = 0; i < nPoly; i++){
			k[i] = poly3Ds.get(i).avgDistance;
			drawOrder[i] = i;
		}
		
		double temp;
		int tempr;
		for(int a = 0; a < k.length; a++){
			for(int b = 0; b < k.length - 1; b++){
				if(k[b] < k[b+1]){
					temp = k[b];
					tempr = drawOrder[b];
					drawOrder[b] = drawOrder[b+1];
					k[b] = k[b+1];
					
					drawOrder[b+1] = tempr;
					k[b+1] = temp;
				}
			}
		}
	}
	
	/**
	 * Sets the light position
	 */
	private void setLight(){
		Geometry.Vector mapSize = map.getDimensions();
		lightDir.x = mapSize.getX()/2 - (mapSize.getX()/2 + Math.cos(lightPosition) * mapSize.getX() * 10);
		lightDir.y = mapSize.getY()/2 - (mapSize.getY()/2 + Math.sin(lightPosition) * mapSize.getY() * 10);
		lightDir.z = -200;
	}
	
	/**
	 * Updates the camera vectors when keys are pressed
	 */
	private void camera(){
		
		Ship ship = (Ship) map.get(shipIndex);
		
		
		//Generate CM matrix for transforming points from global coordinate system to camera coordinate system
		CM = Matrix.getCM(viewFrom, V, U, N, 2);
	}
	
	public void setMap(Map map){
		this.map = map;
		for(Body b : map){
			if(b.getClass() == Ship.class){
				Ship s = (Ship)b;
				if(s.getPilotName().equals(nickname)){
					shipIndex = map.indexOf(b);
					break;
				}
			}
		}
	}
}
