package Views;

import java.awt.event.KeyEvent;

/**
 * Created by james on 20/02/17.
 */
public class KeySequenceManager {
    private EngineerView parent;
    private String keySequence;
    private int position;
    private boolean restart;
    private boolean active;

    public KeySequenceManager(String sequence, boolean restart, EngineerView parent) {
        this.position = 0;
        this.keySequence = sequence;
        this.restart = restart;
        this.active = true;
        this.parent = parent;
    }

    public boolean keyPressed(KeyEvent e) {

    }
}
