package de.SweetCode.e.math;

public class EMath {

    /**
     * <p>
     *    Checks if the provided value is in the bounds. The bounds are inclusive. If the value exceeds the bounds it
     *    always returns the value of the exceeded border.
     * </p>
     *
     * @param min   The min border value.
     * @param value The value to check.
     * @param max   The max border value.
     * @return The determined value.
     */
    public static int bound(int min, int value, int max) {
        return (value < min ? min : (value > max ? max : value));
    }

    /**
     * <p>
     *    Checks if the provided value is in the bounds. The bounds are inclusive. If the value exceeds the bounds it
     *    always returns the value of the exceeded border.
     * </p>
     *
     * @param min   The min border value.
     * @param value The value to check.
     * @param max   The max border value.
     * @return The determined value.
     */
    public static double bound(double min, double value, double max) {
        return (value < min ? min : (value > max ? max : value));
    }

    /**
     * <p>
     *    Checks if the provided value is in the bounds. The bounds are inclusive. If the value exceeds the bounds it
     *    always returns the value of the exceeded border.
     * </p>
     *
     * @param min   The min border value.
     * @param value The value to check.
     * @param max   The max border value.
     * @return The determined value.
     */
    public static float bound(float min, float value, float max) {
        return (value < min ? min : (value > max ? max : value));
    }

    /**
     * <p>
     *    Checks if the provided value is in the bounds. The bounds are inclusive. If the value exceeds the bounds it
     *    always returns the value of the exceeded border.
     * </p>
     *
     * @param min   The min border value.
     * @param value The value to check.
     * @param max   The max border value.
     * @return The determined value.
     */
    public static long bound(long min, long value, long max) {
        return (value < min ? min : (value > max ? max : value));
    }

}
