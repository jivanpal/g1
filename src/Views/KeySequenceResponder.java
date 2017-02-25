package Views;

/**
 * Created by james on 25/02/17.
 */
public interface KeySequenceResponder {
    void keySequencePassed();
    void keySequenceSoftFailure();
    void keySequenceHardFailure();
}
