package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

import GameLogic.Asteroid;
import GameLogic.Bullet;
import GameLogic.Map;
import GameLogic.Ship;
import Physics.Body;
import jdk.nashorn.internal.objects.Global;
import Geometry.Rotation;
import Geometry.Vector;
import GameLogic.Resource;

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
	
	private double lightPosition = 0;
	
	public static int nPoly = 0, nPoly3D = 0;
	public static ArrayList<Poly3D> poly3Ds = new ArrayList<Poly3D>();
	boolean w, a, s, d, e, q;
	
	private Map map;
	private String nickname;
	private Integer shipIndex = null;
	private boolean pilot;
	private Map starMap;
	private boolean selfDestruct = false;
	private int destructCount = 1;
	private boolean crosshair;
	private boolean debug = false;
	private int asteroidCount = 0;
	
	/**
	 * Creates a new Screen object
	 * @param nickname The nickname of the user who is the pilot of the ship
	 * @param pilot Whether this user is the pilot or the engineer
	 */
	public Screen(String nickname, boolean pilot, boolean crosshair){
		
		this.nickname = nickname;
		this.pilot = pilot;
		this.crosshair = crosshair;
		map = new Map(0, 0, 0);
		Body asteroid = new Body();
		asteroid.move(new Vector(0, 2, 0));
		map.add(asteroid);
		
		starMap = new Map(2000, 2000, 2000);
		Random r = new Random();
		r.ints(0, (int) starMap.getDimensions().getX());
		for(int i = 0; i < starMap.getDimensions().getX(); i++){
			starMap.add(new Star(r.nextInt(), r.nextInt(), r.nextInt()));
		}
		
		//Create starting vectors
		viewFrom = new Vector(0, 0, 0);
		viewTo = new Vector(0, 0, 1);
		lightDir = new Vector(1, 1, 1);
		
		//Create camera vectors
		N = viewTo.plus(viewFrom);
		N = N.normalise();
		U = new Vector(0, 1, 0);
		U = U.normalise();
		V = U.cross(N);
		V = V.normalise();
		U = N.cross(V);
		U = U.normalise();
		
		cameraSystem = new double[][] { {V.getX(), V.getY(), V.getZ(), 0},
										{U.getX(), U.getY(), U.getZ(), 0},
										{N.getX(), N.getY(), N.getZ(), 0},
										{0,        0,        0,        1}};
										
		CM = Matrix.getCM(viewFrom, V, U, N, 2);
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
		setLight();
		
		g.setColor(Color.WHITE);

		starMap.bodies().parallelStream()
				.map(Star.class::cast)
				.forEach( s -> {
					Vector v = s.getPosition();
					Point p = Calculations.calcPos(viewFrom, viewTo, v);
					g.fillOval((int)p.x, (int)p.y, 1, 1);
					g.drawOval((int)p.x, (int)p.y, 1, 1);
				});
		
		createObjects();
		
		//Draw all polygons onto the screen
		nPoly = poly3Ds.size();

		poly3Ds.parallelStream().forEach(poly3D -> poly3D.update());

		setDrawOrder();
		
		for(Poly3D p : poly3Ds){
			p.poly.drawPoly(g);
		}
		
//		System.out.println("Predicted Polygons: " + asteroidCount * 4 * 27);
//		System.out.println("Actual Number: " + poly3Ds.size());
		
		warningLight(g);
		
		
		
		//Draw debugging information
		Vector camCoords = Matrix.multiplyVector(cameraSystem, new Vector(0, 0, 0));
		g.setColor(Color.WHITE);
		
		if(crosshair){
			g.drawLine(GameLogic.Global.SCREEN_WIDTH/2 - 5, GameLogic.Global.SCREEN_HEIGHT/2, GameLogic.Global.SCREEN_WIDTH/2 + 5, GameLogic.Global.SCREEN_HEIGHT/2);
			g.drawLine(GameLogic.Global.SCREEN_WIDTH/2, GameLogic.Global.SCREEN_HEIGHT/2 - 5, GameLogic.Global.SCREEN_WIDTH/2, GameLogic.Global.SCREEN_HEIGHT/2 + 5);
		}
		if(debug){
	        g.drawString("viewFrom: "+viewFrom, 40, 40);
	        g.drawString("camera: "+camCoords,  40, 60);
	        g.drawString("viewTo: "+viewTo,     40, 80);
	        g.drawString("V: "+V,               40, 100);
	        g.drawString("U: "+U,               40, 120);
	        g.drawString("N: "+N,               40, 140);

	        try {
				g.drawString("Vel: " + map.get(shipIndex).getVelocity(), 40, 160);
			} catch (Exception e) {
	        	//whatever
			}
        
	        for(Body b : map.bodies()) {
	            if (!(shipIndex == null) && b.getID() != shipIndex) {
	            	Class bClass = b.getClass();
	                if(bClass == Asteroid.class){
	                    for(Vector v : map.getAllPositions(b.getPosition())){
	                    	Point newPos = Calculations.calcPos(viewFrom, viewTo, v);
	                    	if(newPos.z > 0){
	                    		Vector distance = viewFrom.minus(v);
	                    		g.drawString("" + (int) distance.length(), (int)(newPos.x + GameLogic.Global.SCREEN_WIDTH/2), (int)(newPos.y + GameLogic.Global.SCREEN_HEIGHT/2));
	                    	}
	                    }
	                }
	                if(debug) {
		                for(Vector v : map.getAllPositions(b.getPosition())){
			                Point newPos = Calculations.calcPos(viewFrom, viewTo, v);
			                Point newPos2 = Calculations.calcPos(viewFrom, viewTo, v.plus(b.getVelocity().scale(GameLogic.Global.REFRESH_PERIOD)));
			                g.drawLine((int)(newPos.x + GameLogic.Global.SCREEN_WIDTH/2), (int)(newPos.y + GameLogic.Global.SCREEN_HEIGHT/2), (int)(newPos2.x + GameLogic.Global.SCREEN_WIDTH/2), (int)(newPos2.y + GameLogic.Global.SCREEN_HEIGHT/2));
		                }
	                }
	            }
	        }
		}
        
		sleepAndRefresh();
	}
	
	/**
	 * If the ship is about to be destroyed the screen will fade in and out a red light
	 * @param g Graphics object to use
	 */
	private void warningLight(Graphics g) {
		if(selfDestruct){
			if(destructCount <= 10){
				Color warning = new Color(255 * destructCount / 10, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else if(destructCount > 10 && destructCount <= 20){
				Color warning = new Color(255, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else if(destructCount > 20 && destructCount <= 30){
				Color warning = new Color(255 * (30 - destructCount) / 10, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else if(destructCount > 30 && destructCount <= 40){
				Color warning = new Color(25, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
				destructCount++;
			}
			else{
				destructCount = 1;
				Color warning = new Color(25, 0, 0, 100);
				g.setColor(warning);
				g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
			}
		}
	}

    /**
     * Creates graphical representations of all the objects in a map
     */
    private void createObjects() {
    	
        poly3Ds.clear();
        int shipCount = 1;
        asteroidCount = 0;
        for(Body b : map.bodies()) {
            if (!(shipIndex == null) && b.getID() != shipIndex) {
                Class<? extends Body> bClass = b.getClass();
                
                if (bClass == Ship.class) {
//                	Vector pos = Vector.ZERO;
//                	double distance = Integer.MAX_VALUE;
                    for(Vector v : map.getAllPositions(b.getPosition())){
//                    	if(viewFrom.minus(v).length() < distance){
//                    		distance = viewFrom.minus(v).length();
//                    		pos = v;
//                    	}
                    	new ShipModel(v, b.getRadius(), b.getOrientation(), shipCount);
                    }
//                    new ShipModel(pos, b.getRadius(), b.getOrientation(), shipCount);
                    shipCount++;
                }
                else if(bClass == Asteroid.class){
                    
                    for(Vector v : map.getAllPositions(b.getPosition())){
                    	new AsteroidModel(v, b.getRadius(), b.getOrientation());
                    }
                    asteroidCount++;
                }
                else if(bClass == Bullet.class){
                	Bullet bul = (Bullet) b;
                	double distance = Integer.MAX_VALUE;
                	Vector pos = Vector.ZERO;
                	for(Vector v : map.getAllPositions(b.getPosition())){
                		if(viewFrom.minus(v).length() < distance){
                			distance = viewFrom.minus(v).length();
                			pos = v;
                		}
                	}
                    switch (bul.type()) {
                    case LASER:
                        new Laser(pos, b.getRadius(), b.getOrientation());
                        break;
                    case PLASMA:
                        new Plasma(pos, b.getRadius(), b.getOrientation());
                        break;
                    case TORPEDO:
                        new Torpedo(pos, b.getRadius(), b.getOrientation(), bul.getTimeToLive());
                        break;
                    }
                }
            }
        }
        
//        map.bodies().parallelStream()
//		.forEach( b -> {
//			if (!(shipIndex == null) && b.getID() != shipIndex) {
//                Class<? extends Body> bClass = b.getClass();
//                if (bClass == Ship.class) {
//                	Vector pos = new Vector(0, 0, 0);
//                	double distance = Integer.MAX_VALUE;
//                    for(Vector v : map.getAllPositions(b.getPosition())){
////                         System.out.println("Drawing Ship: " + b.getID() + ", " + shipIndex);
//                    	if(viewFrom.minus(v).length() < distance){
//                    		distance = viewFrom.minus(v).length();
//                    		pos = v;
//                    	}
//                    }
//                    new ShipModel(pos, 2, b.getOrientation(), b.getID());
//                }
//                else if(bClass == Asteroid.class){
////                	System.out.println(b.getID() + ": " + b.getPosition());
//                    for(Vector v : map.getAllPositions(b.getPosition())){
////                    	System.out.println(v);
//                    	new AsteroidModel(v, 2, b.getOrientation());
//                    }
//                }
//                else if(bClass == Bullet.class){
//                    for(Vector v : map.getAllPositions(b.getPosition())){
////                    	System.out.println("Laser orientation: " + b.getBasis());
//                        new Laser(v, 0.05, b.getOrientation());
//                    }
//                }
//            }
//		});
//        System.out.println("Completed createObjects()");
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
	private void setDrawOrder() {
		poly3Ds.sort((poly1, poly2) -> {
			if(poly1.avgDistance > poly2.avgDistance){
				return -1;
			}
			else if(poly1.avgDistance == poly2.avgDistance){
				return 0;
			}
			else{
				return 1;
			}
		});
	}
	
	/**
	 * Sets the light position
	 */
	private void setLight(){
		lightPosition += 0.005;
		Vector mapSize = map.getDimensions();
		lightDir = new Vector (mapSize.getX()/2 - (mapSize.getX()/2 + Math.cos(lightPosition) * mapSize.getX() * 10), mapSize.getY()/2 - (mapSize.getY()/2 + Math.sin(lightPosition) * mapSize.getY() * 10), -200);
//		System.out.println(lightDir);
	}
	
	/**
	 * Updates the camera vectors
	 */
	private void camera(){
		
		if(shipIndex != null) {
			Ship ship = (Ship) map.get(shipIndex);
			U = ship.getDownVector();
			if(pilot) {
				N = ship.getFrontVector();
				V = ship.getLeftVector();
			}
			else {
				N = ship.getRearVector();
				V  =ship.getRightVector();
			}
			viewFrom = ship.getPosition();
			viewTo = viewFrom.plus(N);
			
			// Generate CM matrix for transforming points from global coordinate system to camera coordinate system
			CM = Matrix.getCM(viewFrom, V, U, N, 10);
		}
		
		
	}
	
	/**
	 * Sets the map that the screen should display to a new map
	 * @param map The map that you want to display
	 */
	public void setMap(Map map){
		this.map = map;
		shipIndex = null;
		for(Body b : map.bodies()) {
			if(b.getClass() == Ship.class) {
				Ship s = (Ship)b;
				if(s.getPilotName().equals(nickname) || s.getEngineerName().equals(nickname)){
					shipIndex = b.getID();
					if(s.getResource(Resource.Type.HEALTH).get() <= 10){
						selfDestruct = true;
					}
					else{
						selfDestruct = false;
					}
					break;
				}
			}
		}
	}
}
