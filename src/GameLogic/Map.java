package GameLogic;

import java.util.ArrayList;
import Geometry.*;
import Physics.Body;

public class Map {
    private Vector dimensions;
    private ArrayList<Body> stuffOnMap;
    
    
    public Map(int x, int y, int z) {
        this(new Vector(x,y,z));
    }
    
    public Map(Vector dimensions) {
        this.dimensions = dimensions;
    }
    
    public void add(Body b) {
        stuffOnMap.add(b);
    }
    
    // public void update()
    // to be implemented
}
