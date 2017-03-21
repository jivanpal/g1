package Utils;

/**
 * Created by James on 06/03/17.
 * Class to hold general Utility methods
 */
public class Utils {
    public static double scaleValueToRange(double value, double oldRangeMin, double oldRangeMax,
                                           double newRangeMin, double newRangeMax) {
        return (((newRangeMax - newRangeMin) * (value - oldRangeMin)) / (oldRangeMax - oldRangeMin)) + newRangeMin;
    }
    
    /**
     * Given a String 'number:sequence', returns just the number component
     *
     * @param sequenceWithNum A string in the form 'number:sequence'
     * @return The number part of the String
     */
    public static String parseNumber(String sequenceWithNum) {
        return sequenceWithNum.split(":")[0];
    }

    /**
     * Given a String 'number:sequence', returns just the sequence component
     *
     * @param sequenceWithNum A string in the form 'number:sequence'
     * @return The sequence part of the String
     */
    public static String parseSequence(String sequenceWithNum) {
        if (sequenceWithNum.length() >= 3) {
            return sequenceWithNum.split(":")[1];
        } else {
            return "";
        }
    }
}
