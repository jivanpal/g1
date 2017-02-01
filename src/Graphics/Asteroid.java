package Graphics;
import java.awt.Color;
import java.util.Random;

public class Asteroid {
	
	public Asteroid(){
		
	}
	
	public static void createAsteroid(int x, int y, int z){
		
		Random r = new Random();
		
		int n = r.nextInt(5) + 5;
		
		double[] xs = new double[n];
		double[] ys = new double[n];
		double[] zs = new double[n];
		
		for(int i = 0; i < 4; i++){
			xs[i] = (r.nextDouble() - 0.5) /4 + x;
			ys[i] = (r.nextDouble() - 0.5) /4 + y;
			zs[i] = (r.nextDouble() - 0.5) /4 + z;
		}
//		System.out.println("Creating Asteroid...");
//		System.out.println(xs);
//		System.out.println(ys);
//		System.out.println(zs);
		
		Poly3D asteroidSide1 = new Poly3D(new double[] {xs[0], xs[1], xs[2]}, new double[] {ys[0], ys[1], ys[2]}, new double[] {zs[0], zs[1], zs[2]}, Color.RED);
		Poly3D asteroidSide2 = new Poly3D(new double[] {xs[0], xs[1], xs[3]}, new double[] {ys[0], ys[1], ys[3]}, new double[] {zs[0], zs[1], zs[3]}, Color.RED);
		Poly3D asteroidSide3 = new Poly3D(new double[] {xs[0], xs[2], xs[3]}, new double[] {ys[0], ys[2], ys[3]}, new double[] {zs[0], zs[2], zs[3]}, Color.RED);
		Poly3D asteroidSide4 = new Poly3D(new double[] {xs[1], xs[2], xs[3]}, new double[] {ys[1], ys[2], ys[3]}, new double[] {zs[1], zs[2], zs[3]}, Color.RED);

		Poly3D[] asteroidSides = {asteroidSide1, asteroidSide2, asteroidSide3, asteroidSide4};
		
		for(int j = 0; j < 4; j++){
			Screen.poly3Ds.add(asteroidSides[j]);
		}
	}
}
