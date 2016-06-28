package de.SweetCode.e.utils;

import java.lang.reflect.Field;
import java.util.*;

public class ToStringBuilder {

    private final StringBuffer buffer;
    private final Object object;
    private final Map<Object, Integer> previousCheck;

    private ToStringBuilder(Object object, StringBuffer buffer, Map<Object, Integer> previousCheck) {
        this.buffer = buffer;
        this.object = object;
        this.previousCheck = previousCheck;
        this.start();
    }

    public ToStringBuilder(Object object) {
        this(object, new StringBuffer(), new HashMap<>());
    }

    private void start() {
        String value = (object == null ? "{null" : String.format("{%s -> ", object.getClass().getSimpleName()));
        this.buffer.append(
                value
        );
    }

    public ToStringBuilder append(Object object) {
        return this.append(null, object);
    }

    public ToStringBuilder append(String fieldName, Object object) {

        if(object == null) {
            return this;
        }

        StringBuffer buffer = new StringBuffer();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if(fieldName == null) {
            buffer.append(
                    String.format("{[%s|%d|$%d] -> ", clazz.getSimpleName(), object.hashCode(), this.previousCheck.containsKey(object) ? this.previousCheck.get(object) : -1)
            );
        } else {
            buffer.append(
                    String.format("%s: {[%s|%d|$%d] -> ", fieldName, clazz.getSimpleName(), object.hashCode(), this.previousCheck.containsKey(object) ? this.previousCheck.get(object) : -1)
            );
        }

        for(Field field : fields) {

            buffer.append(String.format("%s: ", field.getName()));
            field.setAccessible(true);
            try {

                Object value = field.get(object);
                Class valueClazz = null;

                if(!(value == null)) {
                    valueClazz = value.getClass();
                }

                if(value == null) {
                    buffer.append("null");
                } else if(valueClazz == String.class) {
                    buffer.append(String.valueOf(value));
                } else if(valueClazz == String[].class) {
                    buffer.append(StringUtils.join((String[]) value, ", "));
                } else if(valueClazz == Integer.class) {
                    buffer.append(value);
                } else if(valueClazz == Integer[].class) {
                    buffer.append(StringUtils.join((int[]) value, ", "));
                } else if(valueClazz == Double.class) {
                    buffer.append(value);
                } else if(valueClazz == Double[].class) {
                    buffer.append(StringUtils.join((double[]) value, ", "));
                } else if(valueClazz == Byte.class) {
                    buffer.append(value);
                } else if(valueClazz == Byte[].class) {
                    buffer.append(StringUtils.join((byte[]) value, ", "));
                } else if(valueClazz == Short.class) {
                    buffer.append(value);
                } else if(valueClazz == Short[].class) {
                    buffer.append(StringUtils.join((short[]) value, ", "));
                } else if(valueClazz == Long.class) {
                    buffer.append(value);
                } else if(valueClazz == Long[].class) {
                    buffer.append(StringUtils.join((long[]) value, ", "));
                } else if(valueClazz == Float.class) {
                    buffer.append(value);
                } else if(valueClazz == Float[].class) {
                    buffer.append(StringUtils.join((float[]) value, ", "));
                } else if(valueClazz == Character.class) {
                    buffer.append(value);
                } else if(valueClazz == Character[].class) {
                    buffer.append(StringUtils.join((char[]) value, ", "));
                } else if(valueClazz == Collection.class) {
                    buffer.append(StringUtils.join((Collection) value, ", "));
                } else if(!(this.previousCheck.containsKey(value))) {
                    this.previousCheck.put(value, this.previousCheck.size());
                    buffer.append(ToStringBuilder.create(value, this.previousCheck).append(field.getName(), value).toString());
                } else if(this.previousCheck.containsKey(value)) {
                    buffer.append(String.format("{$%d->%s}", this.previousCheck.get(value), value.getClass().getSimpleName()));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            buffer.append(String.format(", "));

        }

        this.buffer.append(buffer.toString());

        return this;

    }

    public ToStringBuilder append(String fieldName, String value) {
        this.buffer.append(fieldName).append(": ").append(value).append(", ");
        return this;
    }

    public ToStringBuilder append(String fieldName, String[] value) {
        this.buffer.append(fieldName).append(": ").append("{").append(StringUtils.join(value, ", ")).append("}");
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean value) {
        this.buffer.append(String.format("%s: %b, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean[] value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, byte value) {
        this.buffer.append(String.format("%s: 0x%02X, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, byte[] value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, short value) {
        this.buffer.append(String.format("%s: %d, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, short[] value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, int value) {
        this.buffer.append(String.format("%s: %d, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, int[] value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, double value, int precision) {
        this.buffer.append(String.format("%s: %." + precision + "f, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, double value) {
        return this.append(fieldName, value, 2);
    }

    public ToStringBuilder append(String fieldName, double[] value, int precision) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ", precision)));
        return this;
    }

    public ToStringBuilder append(String fieldName, double[] value) {
        return this.append(fieldName, value, 2);
    }

    public ToStringBuilder append(String fieldName, float value, int precision) {
        this.buffer.append(String.format("%s: %." + precision + "f, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, float value) {
        return this.append(fieldName, value, 2);
    }

    public ToStringBuilder append(String fieldName, float[] value, int precision) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ", precision)));
        return this;
    }

    public ToStringBuilder append(String fieldName, float[] value) {
        return this.append(fieldName, value, 2);
    }

    public ToStringBuilder append(String fieldName, long value) {
        this.buffer.append(String.format("%s: %d, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, long[] value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, char value) {
        this.buffer.append(String.format("%s: %s, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, char[] value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, Collection value) {
        this.buffer.append(String.format("%s: {%s}, ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public String build() {
        if(this.buffer.length() > 5) {
            this.buffer.replace(this.buffer.length() - 2, this.buffer.length(), "");
        }

        this.buffer.append("}");
        return this.buffer.toString();
    }

    @Override
    public String toString() {
        return this.build();
    }

    private static ToStringBuilder create(Object object, Map<Object, Integer> previousCheck) {
        return new ToStringBuilder(object, new StringBuffer(), previousCheck);
    }

    public static ToStringBuilder create(Object object) {
        return new ToStringBuilder(object);
    }

}
