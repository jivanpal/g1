package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener{
	
	private double sleepTime = 1000/60, lastRefresh = 0;
	public static double[] viewFrom = {15, 5, 10};
	public static double[] viewTo = {0, 0, 0};
	public static double[] lightDir = {1, 1, 1};
	private double lightPosition, mapSize = 10;
	private static double moveSpeed = 0.005, verticalLook = -0.9, horizontalLook = 0;
	private double verticalLookSpeed = 0.0001, horizontalLookSpeed = 0.0005;
	private double r;
	
	public static int nPoly = 0, nPoly3D = 0;
//	public static PolygonObj[] drawablePolygons = new PolygonObj[100];
	public static ArrayList<Poly3D> poly3Ds = new ArrayList<Poly3D>();
	int drawOrder[];
	boolean w, a, s, d, e, q;
	
	public Screen(){
		
		Poly3D poly1 = new Poly3D(new double[]{0, 0.3, 0.6}, new double[]{0, 0.4, 0}, new double[]{0, 0, 0}, Color.RED);
		Screen.poly3Ds.add(poly1);
		Poly3D poly2 = new Poly3D(new double[]{0, 0.3, 0.3}, new double[]{0, 0.4, 0.2}, new double[]{0, 0, 0.4}, Color.RED);
		Screen.poly3Ds.add(poly2);
		Poly3D poly3 = new Poly3D(new double[]{0, 0.6, 0.3}, new double[]{0, 0, 0.2}, new double[]{0, 0, 0.4}, Color.RED);
		Screen.poly3Ds.add(poly3);
		Poly3D poly4 = new Poly3D(new double[]{0.3, 0.6, 0.3}, new double[]{0.4, 0, 0.2}, new double[]{0, 0, 0.4}, Color.RED);
		Screen.poly3Ds.add(poly4);
			
		Random r = new Random();
		for(int i = 0; i < 100; i++){
			Asteroid.createAsteroid(r.nextInt((int)mapSize), r.nextInt((int)mapSize), r.nextInt((int)mapSize));
		}
		addKeyListener(this);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)GameEngine.screenSize.getWidth(), (int)GameEngine.screenSize.getHeight());
		
		controls();
		Calculations.setInfo();
		setLight();
		nPoly = poly3Ds.size();
		
		for(int i = 0; i < nPoly; i++){
			poly3Ds.get(i).update();
		}
		
		setDrawOrder();
		
		for(int i = 0; i < nPoly; i++){
//			System.out.println("Drawing Polygon " + i);
			poly3Ds.get(drawOrder[i]).poly.drawPoly(g);
		}
		
		g.setColor(Color.WHITE);
		g.drawString("x: " + viewFrom[0] + ", y: " + viewFrom[1] + ", z: " + viewFrom[2], 40, 40);
		g.drawString("x: " + viewTo[0] + ", y: " + viewTo[1] + ", z: " + viewTo[2], 40, 60);
		g.drawString("r: " + r, 40, 80);
		
		sleepAndRefresh();
	}
	
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
	
	private void setLight(){
		lightPosition += 0.005;
		lightDir[0] = mapSize/2 - (mapSize/2 + Math.cos(lightPosition) * mapSize * 10);
		lightDir[1] = mapSize/2 - (mapSize/2 + Math.sin(lightPosition) * mapSize * 10);
		lightDir[2] = -200;
	}
	
	private void controls(){
		Vector viewVec = new Vector(viewTo[0] - viewFrom[0], viewTo[1] - viewFrom[1], viewTo[2] - viewFrom[2]);
		
		double xMove = 0, yMove = 0, zMove = 0;
		
		if(e){
			xMove += viewVec.x;
			yMove += viewVec.y;
			zMove += viewVec.z;
		}
		if(q){
			xMove -= viewVec.x;
			yMove -= viewVec.y;
			zMove -= viewVec.z;
		}
		if(a){
			horizontalLook -= horizontalLookSpeed;
		}
		if(d){
			horizontalLook += horizontalLookSpeed;
		}
		if(s){
			verticalLook += verticalLookSpeed;
		}
		if(w){
			verticalLook -= verticalLookSpeed;
		}
		
		Vector moveVec = new Vector(xMove, yMove, zMove);
		moveTo(viewFrom[0] + moveVec.x * moveSpeed, viewFrom[1] + moveVec.y * moveSpeed, viewFrom[2] + moveVec.z * moveSpeed);
		
		
	}

	private void moveTo(double e, double f, double g) {
		viewFrom[0] = e;
		viewFrom[1] = f;
		viewFrom[2] = g;
		updateView();
	}

	private void updateView() {
		r = Math.sqrt(1 - (verticalLook * verticalLook));
		viewTo[0] = viewFrom[0] + r * Math.cos(horizontalLook);
		viewTo[1] = viewFrom[1] + r * Math.sin(horizontalLook);
//		viewTo[1] = viewFrom[1] + horizontalLook;
//		viewTo[2] = viewFrom[2] + verticalLook;
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent ev) {
		if(ev.getKeyCode() == KeyEvent.VK_W){
			w = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_A){
			a = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_S){
			s = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_D){
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
