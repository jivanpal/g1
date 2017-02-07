package GameLogic;

public class Resource {
    private int level;
    private final int MAX_LEVEL;
    private final int CHANGE;
    
    public Resource (int l, int max, int change) {
        level = l;
        MAX_LEVEL = max;
        CHANGE = change;
    }
    
    public void change(int delta) {
        level += delta;
        if (level < 0) {
            level = 0;
        } else if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }
    }
    
    public void increase() {
        this.change(CHANGE);
    }
    
    public void decrease() {
        this.change(-CHANGE);
    }
    
    public int get() {
        return level;
    }
    
    public int getChange(){
    	return CHANGE;
    }
    
    public int getMaxLevel(){
    	return MAX_LEVEL;
    }
 
}
