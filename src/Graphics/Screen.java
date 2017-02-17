package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import GameLogic.Asteroid;
import GameLogic.Map;
import GameLogic.Ship;
import Physics.Body;
import Geometry.Vector;

/**
 * The viewport of the ship, contains the camera and all objects to be rendered by it
 * @author Dominic
 *
 */
public class Screen extends JPanel{
	
	private double sleepTime = 1000/GameLogic.Global.REFRESH_RATE, lastRefresh = 0;
	public static Vector viewFrom;
	public static Vector viewTo;
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
	private Integer shipIndex = null;
	private boolean pilot;
	
	public Screen(String nickname, boolean pilot){
		
		this.nickname = nickname;
		this.pilot = pilot;
		map = new Map(0, 0, 0);
		Body asteroid = new Body();
		asteroid.move(new Vector(0, 2, 0));
		map.add(asteroid);
		
		//Create starting vectors
		viewFrom = new Vector(0, 0, 0);
		viewTo = new Vector(0, 0, 1);
		lightDir = new Vector(1, 1, 1);
		lightDir.normalise();
		
		//Create camera vectors
		N = viewTo.plus(viewFrom);
		N.normalise();
		U = new Vector(0, 1, 0);
		U.normalise();
		V = U.cross(N);
		V.normalise();
		U = N.cross(V);
		U.normalise();
		
		cameraSystem = new double[][] { {V.getX(), V.getY(), V.getZ(), 0},
										{U.getX(), U.getY(), U.getZ(), 0},
										{N.getX(), N.getY(), N.getZ(), 0},
										{0,        0,        0,        1}};
										
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
//		Calculations.setInfo();
		setLight();
		
		createObjects();
		
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
		g.drawString("x: " + viewFrom.getX() + ", y: " + viewFrom.getY() + ", z: " + viewFrom.getZ() + " x: " + camCoords.getX() + ", y: " + camCoords.getY() + ", z: " + camCoords.getZ(), 40, 40);
		g.drawString("x: " + viewTo.getX() + ", y: " + viewTo.getY() + ", z: " + viewTo.getZ(), 40, 60);
		g.drawString("r: " + r + " vert: " + verticalLook, 40, 80);
		g.drawLine((int)getWidth()/2 - 5, (int)getHeight()/2, (int)getWidth()/2 + 5, (int)getHeight()/2);
		g.drawLine((int)getWidth()/2, (int)getHeight()/2 - 5, (int)getWidth()/2, (int)getHeight()/2 + 5);
		g.drawString("V: " + V.getX() + ", " + V.getY() + ", " + V.getZ(), 40, 100);
		g.drawString("U: " + U.getX() + ", " + U.getY() + ", " + U.getZ(), 40, 120);
		g.drawString("N: " + N.getX() + ", " + N.getY() + ", " + N.getZ(), 40, 140);
		
		g.setColor(Color.RED);
		Point origin = new Point(getWidth() - 40, 40, 1);
		Vector uLine = (new Vector(0, 0, 0)).plus(U.scale(20));
		Vector uLine2 = Matrix.multiplyVector(CM, uLine);
		g.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + (uLine2.getX())), (int)(origin.y + (-uLine2.getY())));
		g.setColor(Color.BLUE);
		Vector vLine = (new Vector(0, 0, 0)).plus(V.scale(20));
		Vector vLine2 = Matrix.multiplyVector(CM, vLine);
		g.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + (vLine2.getX())), (int)(origin.y + (-vLine2.getY())));
		g.setColor(Color.GREEN);
		Vector nLine = (new Vector(0, 0, 0)).plus(N.scale(20));
		Vector nLine2 = Matrix.multiplyVector(CM, nLine);
		g.drawLine((int)origin.x, (int)origin.y, (int)(origin.x + (nLine2.getX())), (int)(origin.y + (-nLine2.getY())));
		
		sleepAndRefresh();
	}
	
	private void createObjects() {
		for(Body b : map){
			//if(/*Body ID is not already present on the map*/){
				Class<? extends Body> bClass = b.getClass();
				if(bClass == Ship.class){
					Icosahedron ship = new Icosahedron(b.getPosition(), 0.01);
				}
				else if(bClass == Asteroid.class){
					AsteroidModel asteroid = new AsteroidModel(b.getPosition(), 0.25);
				}
			//}
		}
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
		lightDir = new Vector (mapSize.getX()/2 - (mapSize.getX()/2 + Math.cos(lightPosition) * mapSize.getX() * 10), mapSize.getY()/2 - (mapSize.getY()/2 + Math.sin(lightPosition) * mapSize.getY() * 10), -200);
	}
	
	/**
	 * Updates the camera vectors
	 */
	private void camera(){
		
		if(shipIndex != null){
			Ship ship = (Ship) map.get(shipIndex);
			U = ship.getUpVector();
			V = ship.getRightVector();
			if(pilot){
				N = ship.getForwardVector();
			}
			else{
				N = ship.getForwardVector();
			}
			viewFrom = ship.getPosition();
			viewTo = viewFrom.plus(N);
			
			//Generate CM matrix for transforming points from global coordinate system to camera coordinate system
			CM = Matrix.getCM(viewFrom, V, U, N, 2);
		}
		
		
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
