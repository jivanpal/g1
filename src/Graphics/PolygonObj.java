package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class PolygonObj {
	private Polygon p;
	private Color c;
	public double avgDist = 0;
	public double light = 1;
	public boolean draw = true;
	
	public PolygonObj(double[] x, double[] y, Color c, int n){
		p = new Polygon();
		for(int i = 0; i < x.length; i++){
			p.addPoint((int)x[i], (int)y[i]);
		}
		this.c = c;
	}
	
	void update(double[] x, double[] y){
		p.reset();
		for(int i = 0; i < x.length; i++){
			p.xpoints[i] = (int)x[i];
			p.ypoints[i] = (int)y[i];
			p.npoints = x.length;
		}
	}
	
	void drawPoly(Graphics g){
		if(draw){
			g.setColor(new Color((int)(c.getRed() * light), (int)(c.getGreen() * light), (int)(c.getBlue() * light)));
			g.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
		}
	}
}
