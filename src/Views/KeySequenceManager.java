package Views;

import java.awt.event.KeyEvent;

/**
 * Created by James on 20/02/17.
 * Takes a sequence of keys and manages the players progress through pressing them all. Fires events back to the parent
 * for when the player succeeds / fails the sequence.
 */
public class KeySequenceManager {
    private EngineerView parent;
    private String keySequence;
    private int position;
    private boolean restart;
    private boolean active;

    public KeySequenceManager(EngineerView parent) {
        this.active = false;
        this.parent = parent;
    }

    /**
     * Handle a key press by the user. If correctly pressed, advance through the sequence. If incorrectly pressed, return
     * false and return to the beginning of the sequence.
     * @param e The KeyEvent that just occurred.
     * @return Whether the KeyEvent was the correct key press or not.
     */
    public boolean keyPressed(KeyEvent e) {
        if (!active) return false;

        // Check if the player pressed a key correctly
        if (e.getKeyChar() == keySequence.charAt(position)) {
            if(position + 1 >= keySequence.length()) {
                // Tell the parent we have succeeded
                parent.keySequencePassed();

                position = 0;

                // Check if we should start the KeySequence again without reinitialisation.
                if(!restart) {
                    active = false;
                }
            } else {
                position++;
            }

            return true;
        } else {
            position = 0;

            if(!restart) {
                // Tell the parent we have failed
                parent.keySequenceFailed();
                active = false;
            }

            return false;
        }
    }

    public void initialiseKeySequenceManager(String sequence, boolean restart) {
        this.keySequence = sequence;
        this.restart = restart;
        this.active = true;
        this.position = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void deactiveate() {
        this.active = false;
    }
}
