public class Resource {
    private int level;
    private final int MAX_LEVEL;
    
    public Resource (int l, int max) {
        level = l;
        MAX_LEVEL = max;
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
        this.change(+5);
    }
    
    public void decrease() {
        this.change(-5);
    }
    
    public int get() {
        return level;
    }
}
