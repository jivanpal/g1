package Views;

import java.awt.event.KeyEvent;

/**
 * Created by James on 20/02/17.
 * Takes a sequence of keys and manages the players progress through pressing them all. Fires events back to the parent
 * for when the player succeeds / fails the sequence.
 */
public class KeySequenceManager {
    private KeySequenceResponder parent;
    private String keySequence;
    private int position;
    private boolean restart;
    private boolean active;

    /**
     * Create a new KeySequence manager
     * @param parent The responder containing the manager.
     */
    public KeySequenceManager(KeySequenceResponder parent) {
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

            parent.correctKeyPress();

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
                active = false;

                // Tell the parent we have completely failed
                parent.keySequenceHardFailure();
            } else {
                parent.keySequenceSoftFailure();
            }

            return false;
        }
    }

    /**
     * Initialise the key sequence manager. Sets the key sequence to begin from the start and makes the manager active.
     * @param sequence The key sequence the player has to type.
     * @param restart Whether the key sequence should automatically restart once complete/failed.
     */
    public void initialiseKeySequenceManager(String sequence, boolean restart) {
        System.out.println("Initialised with " + sequence);
        this.keySequence = sequence;
        this.restart = restart;
        this.active = true;
        this.position = 0;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Deactivate the key sequence manager. Any further key presses will have no effect until the manager is reinitialised.
     */
    public void deactivate() {
        this.active = false;
    }
}
