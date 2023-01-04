package io.github.kale_ko.bjsl.elements;

public class ParsedPrimitive extends ParsedElement {
    public enum PrimitiveType {
        STRING, BYTE, CHAR, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, NULL
    }

    protected Object primitive;
    protected PrimitiveType primitiveType;

    protected ParsedPrimitive(Object value, PrimitiveType type) {
        if (type == null) {
            throw new NullPointerException("Type can not be null");
        }

        this.primitive = value;
        this.primitiveType = type;
    }

    public PrimitiveType getType() {
        return this.primitiveType;
    }

    public boolean isString() {
        return this.primitiveType == PrimitiveType.STRING;
    }

    public boolean isByte() {
        return this.primitiveType == PrimitiveType.BYTE;
    }

    public boolean isChar() {
        return this.primitiveType == PrimitiveType.CHAR;
    }

    public boolean isShort() {
        return this.primitiveType == PrimitiveType.SHORT;
    }

    public boolean isInteger() {
        return this.primitiveType == PrimitiveType.INTEGER;
    }

    public boolean isLong() {
        return this.primitiveType == PrimitiveType.LONG;
    }

    public boolean isFloat() {
        return this.primitiveType == PrimitiveType.FLOAT;
    }

    public boolean isDouble() {
        return this.primitiveType == PrimitiveType.DOUBLE;
    }

    public boolean isBoolean() {
        return this.primitiveType == PrimitiveType.BOOLEAN;
    }

    public boolean isNull() {
        return this.primitiveType == PrimitiveType.NULL;
    }

    public Object get() {
        if (this.primitiveType == PrimitiveType.STRING) {
            return (String) this.primitive;
        } else if (this.primitiveType == PrimitiveType.BYTE) {
            return (byte) (long) this.primitive;
        } else if (this.primitiveType == PrimitiveType.CHAR) {
            return (char) (long) this.primitive;
        } else if (this.primitiveType == PrimitiveType.SHORT) {
            return (short) (long) this.primitive;
        } else if (this.primitiveType == PrimitiveType.INTEGER) {
            return (int) (long) this.primitive;
        } else if (this.primitiveType == PrimitiveType.LONG) {
            return (long) this.primitive;
        } else if (this.primitiveType == PrimitiveType.FLOAT) {
            return (float) (double) this.primitive;
        } else if (this.primitiveType == PrimitiveType.DOUBLE) {
            return (double) this.primitive;
        } else if (this.primitiveType == PrimitiveType.BOOLEAN) {
            return (boolean) this.primitive;
        } else if (this.primitiveType == PrimitiveType.NULL) {
            return null;
        } else {
            throw new ClassCastException("Value is not a primitive");
        }
    }

    public String asString() {
        if (this.primitiveType == PrimitiveType.STRING) {
            return (String) this.primitive;
        } else {
            throw new ClassCastException("Value is not a string");
        }
    }

    public byte asByte() {
        if (this.primitiveType == PrimitiveType.BYTE) {
            return (byte) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a byte");
        }
    }

    public char asChar() {
        if (this.primitiveType == PrimitiveType.CHAR) {
            return (char) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a char");
        }
    }

    public short asShort() {
        if (this.primitiveType == PrimitiveType.SHORT) {
            return (short) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a short");
        }
    }

    public int asInteger() {
        if (this.primitiveType == PrimitiveType.INTEGER) {
            return (int) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a integer");
        }
    }

    public long asLong() {
        if (this.primitiveType == PrimitiveType.LONG) {
            return (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a long");
        }
    }

    public float asFloat() {
        if (this.primitiveType == PrimitiveType.FLOAT) {
            return (float) (double) this.primitive;
        } else {
            throw new ClassCastException("Value is not a float");
        }
    }

    public double asDouble() {
        if (this.primitiveType == PrimitiveType.DOUBLE) {
            return (double) this.primitive;
        } else {
            throw new ClassCastException("Value is not a double");
        }
    }

    public boolean asBoolean() {
        if (this.primitiveType == PrimitiveType.BOOLEAN) {
            return (boolean) this.primitive;
        } else {
            throw new ClassCastException("Value is not a boolean");
        }
    }

    public Object asNull() {
        if (this.primitiveType == PrimitiveType.NULL) {
            return this.primitive;
        } else {
            throw new ClassCastException("Value is not null");
        }
    }

    public static ParsedPrimitive from(Object value) {
        if (value == null) {
            return fromNull();
        } else if (value instanceof String) {
            return fromString((String) value);
        } else if (value.getClass() == byte[].class) {
            return fromString((byte[]) value);
        } else if (value.getClass() == char[].class) {
            return fromString((char[]) value);
        } else if (value instanceof Byte || value.getClass() == byte.class) {
            return fromByte((byte) value);
        } else if (value instanceof Character || value.getClass() == char.class) {
            return fromChar((char) value);
        } else if (value instanceof Short || value.getClass() == short.class) {
            return fromShort((short) value);
        } else if (value instanceof Integer || value.getClass() == int.class) {
            return fromInteger((int) value);
        } else if (value instanceof Long || value.getClass() == long.class) {
            return fromLong((long) value);
        } else if (value instanceof Float || value.getClass() == float.class) {
            return fromFloat((float) value);
        } else if (value instanceof Double || value.getClass() == double.class) {
            return fromDouble((double) value);
        } else if (value instanceof Boolean || value.getClass() == boolean.class) {
            return fromBoolean((boolean) value);
        } else {
            throw new ClassCastException("Value is not a primitive");
        }
    }

    public static ParsedPrimitive fromString(String value) {
        if (value == null) {
            throw new NullPointerException("Value must be a string");
        }

        return new ParsedPrimitive(value, PrimitiveType.STRING);
    }

    public static ParsedPrimitive fromString(byte[] value) {
        return new ParsedPrimitive(new String(value), PrimitiveType.STRING);
    }

    public static ParsedPrimitive fromString(char[] value) {
        return new ParsedPrimitive(new String(value), PrimitiveType.STRING);
    }

    public static ParsedPrimitive fromByte(byte value) {
        return new ParsedPrimitive((long) value, PrimitiveType.BYTE);
    }

    public static ParsedPrimitive fromChar(char value) {
        return new ParsedPrimitive((long) value, PrimitiveType.CHAR);
    }

    public static ParsedPrimitive fromShort(short value) {
        return new ParsedPrimitive((long) value, PrimitiveType.SHORT);
    }

    public static ParsedPrimitive fromInteger(int value) {
        return new ParsedPrimitive((long) value, PrimitiveType.INTEGER);
    }

    public static ParsedPrimitive fromLong(long value) {
        return new ParsedPrimitive(value, PrimitiveType.LONG);
    }

    public static ParsedPrimitive fromFloat(float value) {
        return new ParsedPrimitive((double) value, PrimitiveType.FLOAT);
    }

    public static ParsedPrimitive fromDouble(double value) {
        return new ParsedPrimitive(value, PrimitiveType.DOUBLE);
    }

    public static ParsedPrimitive fromBoolean(boolean value) {
        return new ParsedPrimitive(value, PrimitiveType.BOOLEAN);
    }

    public static ParsedPrimitive fromNull() {
        return new ParsedPrimitive(null, PrimitiveType.NULL);
    }
}