package de.SweetCode.e.utils;

import java.util.Arrays;
import java.util.Collection;

public class StringUtils {

    public final static String CR = "\r";
    public final static String LF = "\n";
    public final static String EMPTY = "";

    public final static int INDEX_NOT_FOUND = -1;

    private StringUtils() {}

    public static boolean isEmpty(final CharSequence value) {
        return (value == null || value.length() == 0);
    }

    public static boolean isNotEmpty(final CharSequence value) {
        return !(StringUtils.isEmpty(value));
    }

    public static boolean isBlank(final CharSequence value) {

        if(StringUtils.isNotEmpty(value)) {
            return false;
        }

        for(int i = 0; i < value.length(); i++) {
            if(!(value.charAt(i) == ' ')) {
                return false;
            }
        }

        return true;

    }

    public static String trim(final String value) {
        return (value == null ? StringUtils.EMPTY : value.trim());
    }

    public static String join(double[] value, String delimiter) {
        return StringUtils.join(value, delimiter, 2);
    }

    public static String join(double[] value, String delimiter, int precision) {

        precision = Math.max(precision, 0);

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(double entry : value) {

            builder.append(String.format("%." + precision + "f", entry)).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static String join(float[] value, String delimiter) {
        return StringUtils.join(value, delimiter, 2);
    }

    public static String join(float[] value, String delimiter, int precision) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(double entry : value) {

            builder.append(String.format("%." + precision + "f", entry)).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;


    }

    public static String join(long[] value, String delimiter) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(long entry : value) {
            builder.append(entry).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static String join(int[] value, String delimiter) {
        return StringUtils.join(Arrays.asList(value), delimiter);
    }

    public static String join(short[] value, String delimiter) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(double entry : value) {

            builder.append(entry).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static String join(byte[] value, String delimiter) {
        return StringUtils.join(value, delimiter, false);
    }

    public static String join(byte[] value, String delimiter, boolean hex) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(byte entry : value) {

            builder.append(hex ? String.format("0x%02X", entry) : entry).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static String join(char[] value, String delimiter) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(char entry : value) {

            builder.append(entry).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static String join(boolean[] value, String delimiter) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(boolean entry : value) {

            builder.append(String.format("%b", entry)).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static <T> String join(T[] value, String delimiter) {
        return StringUtils.join(Arrays.asList(value), delimiter);
    }


    public static <T> String join(Collection<T> value, String delimiter) {

        if(value == null) {
            return null;
        }

        StringBuffer builder = new StringBuffer();

        for(T entry : value) {

            builder.append(entry).append(delimiter);

        }

        if(builder.length() > 0) {
            return builder.toString().substring(0, builder.toString().length() - delimiter.length());
        }

        return StringUtils.EMPTY;

    }

    public static int indexOf(String value, char character) {
        return StringUtils.indexOf(value, character, 0, value.length());
    }

    public static int indexOf(String value, char character, int start) {
        return StringUtils.indexOf(value, character, start, value.length());
    }

    public static int indexOf(String value, char character, int start, int end) {

        if(start < 0) {
            throw new IllegalArgumentException("Start cannot less than zero.");
        }

        if(end < start) {
            throw new IllegalArgumentException("End cannot be less than start.");
        }

        if(end > value.length()) {
            throw new IllegalArgumentException("End cannot be greater than the value length.");
        }

        if(value == null) {
            return StringUtils.INDEX_NOT_FOUND;
        }

        char[] array = value.toCharArray();

        for(int i = start; i < end; i++) {
            if(array[i] == character) {
                return i;
            }
        }

        return StringUtils.INDEX_NOT_FOUND;

    }

    public static int lastIndexOf(String value, char character) {

        if(value == null) {
            return StringUtils.INDEX_NOT_FOUND;
        }

        char[] array = value.toCharArray();
        int index = StringUtils.INDEX_NOT_FOUND;

        for(int i = 0; i < array.length; i++) {
            if(array[i] == character) {
                index = i;
            }
        }

        return index;

    }

}
