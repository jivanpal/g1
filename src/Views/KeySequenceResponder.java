package Views;

/**
 * Created by James on 25/02/17.
 * @author James Brown
 */
public interface KeySequenceResponder {
    void correctKeyPress();
    void keySequencePassed();
    void keySequenceSoftFailure();
    void keySequenceHardFailure();
}
