package de.SweetCode.e.utils;

import java.util.Arrays;

public class Assert {

    private Assert() {}

    public static void assertTrue(String message, boolean condition) {
        if(!(condition)) {
            Assert.fail(message);
        }
    }



    public static void assertArrayEquals(String message, int[] expected, int[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, long[] expected, long[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, double[] expected, double[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, char[] expected, char[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, short[] expected, short[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, byte[] expected, byte[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertArrayEquals(String message, float[] expected, float[] actual) {

        if(!(Arrays.equals(expected, actual))) {
            Assert.failNotEquals(message, expected, actual);
        }

    }

    public static void assertSame(Object expected, Object actual) {
        Assert.assertSame(null, expected, actual);
    }

    public static void assertSame(String message, Object expected, Object actual) {

        if(!(expected == actual)) {
            Assert.failNotSame(message, expected, actual);
        }

    }

    public static void assertTrue(boolean condition) {
        Assert.assertTrue(null, condition);
    }

    public static void assertFalse(String message, boolean condition) {
        Assert.assertTrue(message, !condition);
    }

    public static void assertFalse(boolean condition) {
        Assert.assertFalse(null, condition);
    }

    public static void assertNull(Object object) {
        Assert.assertNull(null, object);
    }

    public static void assertNull(String message, Object object) {

        if(!(object == null)) {
            Assert.failNotNull(message, object);
        }

    }

    public static void assertNotNull(Object object) {
        Assert.assertNull(null, object);
    }

    public static void assertNotNull(String message, Object object) {

        if(object == null) {
            Assert.failNull(message, object);
        }

    }

    public static void assertEquals(String message, Object expected, Object actual) {

        if(expected == actual) {
            return;
        }

        if(!(expected == null) && expected.equals(actual)) {
            return;
        }

        if(!(actual == null) && actual.equals(expected)) {
            return;
        }

        Assert.failNotSame(message, expected, actual);

    }

    private static boolean doubleIsDifferent(double d1, double d2, double delta) {

        if(Double.compare(d1, d2) == 0) {
            return false;
        }

        if(Math.abs(d1 - d2) <= delta) {
            return false;
        }

        return true;

    }

    private static boolean floatIsDifferent(float d1, float d2, float delta) {

        if(Float.compare(d1, d2) == 0) {
            return false;
        }

        if(Math.abs(d1 - d2) <= delta) {
            return false;
        }

        return true;

    }

    public static void fail() {
        Assert.fail(null);
    }

    public static void fail(String message) {

        if(message == null) {
            throw new AssertionError();
        }

        throw new AssertionError(message);

    }

    public static void failSame(String message) {

        String value = "";

        if(!(message == null)) {
            value = message + " ";
        }

        Assert.fail(value + "expected not same");

    }

    private static void failNotNull(String message, Object actual) {

        String value = "";

        if(!(message == null)) {
            value = message + " ";
        }

        Assert.fail(value + "expected null, but was:<" + actual + ">");

    }

    private static void failNull(String message, Object actual) {

        String value = "";

        if(!(message == null)) {
            value = message + " ";
        }

        Assert.fail(value + "expected not null, but was:<" + actual + ">");

    }

    private static void failNotSame(String message, Object expected, Object actual) {

        String value = "";

        if(!(message == null)) {
            value = message + " ";
        }

        Assert.fail(value + "expected same:<" + expected + "> was not:<" + actual + ">");
    }

    private static void failNotEquals(String message, Object expected, Object actual) {
        Assert.fail(Assert.format(message, expected, actual));
    }

    private static String format(String message, Object expected, Object actual) {

        String value = "";

        if(!(message == null) && !(message.equals(""))) {
            value = message + " ";
        }

        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);

        if(expectedString.equals(actualString)) {
            return (
                    value + "expected: " + Assert.formatClassAndValue(expected, expectedString)
                    + " but was: " + Assert.formatClassAndValue(actual, actualString)
                    );
        }

        return (

                value + "expected:<" + expectedString + "> but was:<" + actualString + ">"
                );

    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }

}
