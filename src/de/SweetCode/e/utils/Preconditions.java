package de.SweetCode.e.utils;

public class Preconditions {

    private Preconditions() {}

    public static void checkExpression(boolean expression, String message) {
        if(expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkArgument(boolean value) {
        Preconditions.checkExpression(value == false, "The value is false expected the value to be true.");
    }

    public static <T> T checkNotNull(T value) {
        Preconditions.checkExpression(value == null, "The value cannot be null.");

        return value;
    }

}
