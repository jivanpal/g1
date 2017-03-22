package Views;

/**
 * Created by James on 25/02/17.
 * Classes should implement this to handle events fired by KeySequenceManager.java
 * @author James Brown
 */
public interface KeySequenceResponder {
    void correctKeyPress();
    void keySequencePassed();
    void keySequenceSoftFailure();
    void keySequenceHardFailure();
}
