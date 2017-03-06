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
}
