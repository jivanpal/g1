package Views;

/**
 * Created by James on 25/02/17.
 * Classes should implement this to handle events fired by KeySequenceManager.java
 * @author James Brown
 */
public interface KeySequenceResponder {
    /**
     * Single key pressed correctly
     */
    void correctKeyPress();

    /**
     * Entire sequence passed successfully
     */
    void keySequencePassed();

    /**
     * Key sequence failed but auto-restarted
     */
    void keySequenceSoftFailure();

    /**
     * Key sequence failed and not auto-restarted
     */
    void keySequenceHardFailure();
}
