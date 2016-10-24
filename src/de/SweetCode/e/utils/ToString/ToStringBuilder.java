package de.SweetCode.e.utils.ToString;

import de.SweetCode.e.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class ToStringBuilder {

    private final StringBuffer buffer;
    private final Object object;

    public ToStringBuilder(Object object, StringBuffer buffer) {
        this.buffer = buffer;
        this.object = object;
        this.start();

    }

    public ToStringBuilder(Object object) {
        this(object, new StringBuffer(100000));
    }

    private void start() {
        String value = String.format("{%s -> ", object.getClass().getSimpleName());
        this.buffer.append(
                value
        );
    }

    public ToStringBuilder append(String fieldName, ToStringCallback toStringCallback) {
        this.buffer.append(fieldName).append(": ").append("{").append(toStringCallback.create()).append("}, ");
        return this;
    }

    public ToStringBuilder append(String fieldName, Object value) {
        this.buffer.append(fieldName).append(": ").append("{").append(value == null ? "null" : value.toString()).append("}, ");
        return this;
    }

    public ToStringBuilder append(Object object) {

        StringBuffer buffer = new StringBuffer();

        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        buffer.append(
                String.format("{%s -> ", clazz.getSimpleName(), object.hashCode())
        );

        for(Field field : fields) {

            buffer.append(String.format("%s: ", field.getName()));
            field.setAccessible(true);
            try {

                Object value = field.get(object);
                Class valueClazz = null;

                if(!(value == null)) {
                    valueClazz = value.getClass();
                }

                if(valueClazz == String.class) {
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
                } else {
                    // @TODO improve
                    buffer.append(Objects.toString(value));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            buffer.append(", ");

        }

        buffer.setLength(buffer.length() - 2);
        buffer.append("}, ");
        this.buffer.append(buffer.toString());

        return this;

    }

    public ToStringBuilder append(String fieldName, String value) {
        this.buffer.append(fieldName).append(": ").append(value).append(", ");
        return this;
    }

    public ToStringBuilder append(String fieldName, String[] value) {
        this.buffer.append(fieldName).append(": ").append("{").append(StringUtils.join(value, ", ")).append("}, ");
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean value) {
        this.buffer.append(String.format("%s: %b, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean[] value) {
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, byte value) {
        this.buffer.append(String.format("%s: 0x%02X, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, byte[] value) {
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, short value) {
        this.buffer.append(String.format("%s: %d, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, short[] value) {
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, int value) {
        this.buffer.append(String.format("%s: %d, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, int[] value) {
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ")));
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
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ", precision)));
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
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ", precision)));
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
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public ToStringBuilder append(String fieldName, char value) {
        this.buffer.append(String.format("%s: %s, ", fieldName, value));
        return this;
    }

    public ToStringBuilder append(String fieldName, char[] value) {
        this.buffer.append(String.format("%s: [%s], ", fieldName, StringUtils.join(value, ", ")));
        return this;
    }

    public String build() {
        this.buffer.replace(this.buffer.length() - 2, this.buffer.length(), "");
        this.buffer.append("}");
        return this.buffer.toString();
    }

    @Override
    public String toString() {
        return this.build();
    }

    public static ToStringBuilder create(Object object) {
        return new ToStringBuilder(object);
    }

}
