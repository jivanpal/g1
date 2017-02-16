package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import Geometry.Vector;

import javax.swing.JPanel;

/**
 * The viewport of the ship, contains the camera and all objects to be rendered by it
 * @author Dominic
 *
 */
public class TestScreen extends JPanel implements KeyListener{
	
	private double sleepTime = 1000/GameLogic.Global.REFRESH_RATE, lastRefresh = 0;
	public static Point viewFrom;
	public static Point viewTo;
	public static Vector lightDir;
	public static double[][] cameraSystem, worldToCamera, CM;
	public static Vector N, U, V;
	
	private double lightPosition, mapSize = 10;
	private static double moveSpeed = 0.01, verticalLook = 0;
	private double verticalLookSpeed = 0.01, horizontalLookSpeed = 0.01;
	private double r;
	
	public static int nPoly = 0, nPoly3D = 0;
	public static ArrayList<Poly3D> poly3Ds = new ArrayList<Poly3D>();
	int drawOrder[];
	boolean w, a, s, d, e, q;
	
	public TestScreen(){
		
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
		
		
		Icosahedron testShip = new Icosahedron(0, 0, 200, 0.01);
			
		Random r = new Random();
		for(int i = 0; i < 100; i++){
			AsteroidModel.createAsteroid(r.nextInt((int)mapSize), r.nextInt((int)mapSize), r.nextInt((int)mapSize));
		}
		addKeyListener(this);
		setFocusable(true);
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
		lightDir.x = mapSize/2 - (mapSize/2 + Math.cos(lightPosition) * mapSize * 10);
		lightDir.y = mapSize/2 - (mapSize/2 + Math.sin(lightPosition) * mapSize * 10);
		lightDir.z = -200;
	}
	
	/**
	 * Updates the camera vectors when keys are pressed
	 */
	private void camera(){
		
		//Forwards
		if(e){
//			double[][] translate = Matrix.getTranslationMatrix(N.x * moveSpeed, N.y * moveSpeed, N.z * moveSpeed);
//			cameraSystem = Matrix.multiply(translate, cameraSystem);
			Vector move = N.scale(moveSpeed, moveSpeed, moveSpeed);
			viewFrom = viewFrom.pointPlusVector(move);
			viewTo = viewFrom.pointPlusVector(N);
		}
		
		//Backwards
		else if(q){
//			double[][] translate = Matrix.getTranslationMatrix(N.x * -moveSpeed, N.y * -moveSpeed, N.z * -moveSpeed);
//			cameraSystem = Matrix.multiply(translate, cameraSystem);
			Vector move = N.scale(-moveSpeed, -moveSpeed, -moveSpeed);
			viewFrom = viewFrom.pointPlusVector(move);
			viewTo = viewFrom.pointPlusVector(N);
		}
		
		//Rotate along N-axis anti-clockwise
		if(a){
//			double[][] rotate = Matrix.multiply(Matrix.multiply(Matrix.getTranslationMatrix(viewFrom.x, viewFrom.y, viewFrom.z), Matrix.getRotationMatrix(-horizontalLookSpeed, N)), Matrix.getTranslationMatrix(-viewFrom.x, -viewFrom.y, -viewFrom.z));
			double[][] rotate = Matrix.getRotationMatrix(-horizontalLookSpeed, N);
			U = Matrix.multiplyVector(rotate, U);
			U.normalise();
			V = Matrix.multiplyVector(rotate, V);
			V.normalise();
		}
		
		//Rotate along N-axis clockwise
		else if(d){
//			double[][] rotate = Matrix.multiply(Matrix.multiply(Matrix.getTranslationMatrix(viewFrom.x, viewFrom.y, viewFrom.z), Matrix.getRotationMatrix(horizontalLookSpeed, N)), Matrix.getTranslationMatrix(-viewFrom.x, -viewFrom.y, -viewFrom.z));
			double[][] rotate = Matrix.getRotationMatrix(horizontalLookSpeed, N);
			U = Matrix.multiplyVector(rotate, U);
			U.normalise();
			V = Matrix.multiplyVector(rotate, V);
			V.normalise();
		}
		
		//Rotate along the V-axis upwards
		if(s){
//			double[][] rotate = Matrix.multiply(Matrix.multiply(Matrix.getTranslationMatrix(-viewFrom.x, -viewFrom.y, -viewFrom.z), Matrix.getRotationMatrix(verticalLookSpeed, V)), Matrix.getTranslationMatrix(viewFrom.x, viewFrom.y, viewFrom.z));
			double[][] rotate = Matrix.getRotationMatrix(-verticalLookSpeed, V);
			N = Matrix.multiplyVector(rotate, N);
			N.normalise();
			U = Matrix.multiplyVector(rotate, U);
			U.normalise();
			viewTo = viewFrom.pointPlusVector(N);
		}
		
		//Rotate along the V-axis downwards
		else if(w){
//			double[][] rotate = Matrix.multiply(Matrix.multiply(Matrix.getTranslationMatrix(-viewFrom.x, -viewFrom.y, -viewFrom.z), Matrix.getRotationMatrix(-verticalLookSpeed, V)), Matrix.getTranslationMatrix(viewFrom.x, viewFrom.y, viewFrom.z));
			double[][] rotate = Matrix.getRotationMatrix(verticalLookSpeed, V);
			N = Matrix.multiplyVector(rotate, N);
			N.normalise();
			U = Matrix.multiplyVector(rotate, U);
			U.normalise();
			viewTo = viewFrom.pointPlusVector(N);
		}
		
		//Update where the camera is looking at
		
		//Update U, V and N values
//		updateVectors();
//		Matrix.printMatrix(cameraSystem);
		
		//Generate CM matrix for transforming points from global coordinate system to camera coordinate system
		CM = Matrix.getCM(viewFrom, V, U, N, 2);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent ev) {
		if(ev.getKeyCode() == KeyEvent.VK_W){
			s = false;
			w = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_A){
			d = false;
			a = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_S){
			w = false;
			s = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_D){
			a = false;
			d = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_E){
			if(q){
				q = false;
			}
			else{
				e = true;
			}
		}
		if(ev.getKeyCode() == KeyEvent.VK_Q){
			if(e){
				e = false;
			}
			else{
				q = true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent ev) {
		if(ev.getKeyCode() == KeyEvent.VK_W){
			w = false;
		}
		if(ev.getKeyCode() == KeyEvent.VK_A){
			a = false;
		}
		if(ev.getKeyCode() == KeyEvent.VK_S){
			s = false;
		}
		if(ev.getKeyCode() == KeyEvent.VK_D){
			d = false;
		}
	}
}
