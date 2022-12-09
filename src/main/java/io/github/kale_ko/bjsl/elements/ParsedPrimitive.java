package io.github.kale_ko.bjsl.elements;

public class ParsedPrimitive extends ParsedElement {
    public enum PrimitiveType {
        STRING, NUMBER, BOOLEAN, NULL
    }

    protected PrimitiveType primitiveType;

    private ParsedPrimitive(Object value, PrimitiveType type) {
        super(null, null, value);

        if (type == null) {
            throw new NullPointerException("\"type\" can not be null");
        }

        this.primitiveType = type;
    }

    public boolean isString() {
        return this.primitiveType == PrimitiveType.STRING;
    }

    public boolean isByte() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isChar() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isShort() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isInteger() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isLong() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isFloat() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isDouble() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isBoolean() {
        return this.primitiveType == PrimitiveType.NUMBER;
    }

    public boolean isNull() {
        return this.primitiveType == PrimitiveType.NULL;
    }

    public String asString() {
        if (this.primitiveType == PrimitiveType.STRING) {
            return (String) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a string");
        }
    }

    public byte asByte() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (byte) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a byte");
        }
    }

    public char asChar() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (char) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a char");
        }
    }

    public short asShort() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (short) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a short");
        }
    }

    public int asInteger() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (int) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a integer");
        }
    }

    public long asLong() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (long) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a long");
        }
    }

    public float asFloat() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (float) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a float");
        }
    }

    public double asDouble() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (double) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a double");
        }
    }

    public boolean asBoolean() {
        if (this.primitiveType == PrimitiveType.NUMBER) {
            return (boolean) this.primitive;
        } else {
            throw new ClassCastException("\"value\" is not a boolean");
        }
    }

    public Object asNull() {
        if (this.primitiveType == PrimitiveType.NULL) {
            return null;
        } else {
            throw new ClassCastException("\"value\" is not null");
        }
    }

    public static ParsedPrimitive from(Object value) {
        if (value instanceof String) {
            return fromString((String) value);
        } else if (value instanceof Byte) {
            return fromByte((Byte) value);
        } else if (value instanceof Character) {
            return fromChar((Character) value);
        } else if (value instanceof Short) {
            return fromShort((Short) value);
        } else if (value instanceof Integer) {
            return fromInteger((Integer) value);
        } else if (value instanceof Long) {
            return fromLong((Long) value);
        } else if (value instanceof Float) {
            return fromFloat((Float) value);
        } else if (value instanceof Double) {
            return fromDouble((Double) value);
        } else if (value instanceof Boolean) {
            return fromBoolean((Boolean) value);
        } else if (value == null) {
            return fromNull();
        } else {
            throw new ClassCastException("\"value\" is not a primitive");
        }
    }

    public static ParsedPrimitive fromString(String value) {
        return new ParsedPrimitive(value, PrimitiveType.STRING);
    }

    public static ParsedPrimitive fromByte(byte value) {
        return new ParsedPrimitive((long) value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromChar(char value) {
        return new ParsedPrimitive((long) value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromShort(short value) {
        return new ParsedPrimitive((long) value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromInteger(int value) {
        return new ParsedPrimitive((long) value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromLong(long value) {
        return new ParsedPrimitive(value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromFloat(float value) {
        return new ParsedPrimitive((double) value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromDouble(double value) {
        return new ParsedPrimitive(value, PrimitiveType.NUMBER);
    }

    public static ParsedPrimitive fromBoolean(boolean value) {
        return new ParsedPrimitive(value, PrimitiveType.BOOLEAN);
    }

    public static ParsedPrimitive fromNull() {
        return new ParsedPrimitive(null, PrimitiveType.NULL);
    }
}