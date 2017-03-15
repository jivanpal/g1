package Views;

/**
 * Created by james on 25/02/17.
 */
public interface KeySequenceResponder {
    void correctKeyPress();
    void keySequencePassed();
    void keySequenceSoftFailure();
    void keySequenceHardFailure();
}
