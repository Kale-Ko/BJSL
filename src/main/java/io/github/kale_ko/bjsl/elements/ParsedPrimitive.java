package io.github.kale_ko.bjsl.elements;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A wrapper for a primitive object used to represent String/int/float/etc values
 * <p>
 * Note: Values are often not stored as their normal type but rather higher bit types for ease of conversion
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParsedPrimitive extends ParsedElement {
    /**
     * An enum representing different types of Primitives
     *
     * @see ParsedPrimitive
     * @version 1.0.0
     * @since 1.0.0
     */
    public static enum PrimitiveType {
        /**
         * Represents a {@link String}
         *
         * @since 1.0.0
         */
        STRING,

        /**
         * Represents a {@link Byte}
         *
         * @since 1.0.0
         */
        BYTE,

        /**
         * Represents a {@link Character}
         *
         * @since 1.0.0
         */
        CHAR,

        /**
         * Represents a {@link Short}
         *
         * @since 1.0.0
         */
        SHORT,

        /**
         * Represents a {@link Integer}
         *
         * @since 1.0.0
         */
        INTEGER,

        /**
         * Represents a {@link BigInteger}
         *
         * @since 1.0.0
         */
        BIGINTEGER,

        /**
         * Represents a {@link Long}
         *
         * @since 1.0.0
         */
        LONG,

        /**
         * Represents a {@link Float}
         *
         * @since 1.0.0
         */
        FLOAT,

        /**
         * Represents a {@link Double}
         *
         * @since 1.0.0
         */
        DOUBLE,

        /**
         * Represents a {@link BigDecimal}
         *
         * @since 1.0.0
         */
        BIGDECIMAL,

        /**
         * Represents a {@link Boolean}
         *
         * @since 1.0.0
         */
        BOOLEAN,

        /**
         * Represents null
         *
         * @since 1.0.0
         */
        NULL
    }

    /**
     * The value of the primitive
     *
     * @since 1.0.0
     */
    protected Object primitive;

    /**
     * The type of the primitive
     *
     * @see PrimitiveType
     * @since 1.0.0
     */
    protected PrimitiveType primitiveType;

    /**
     * Create a new {@link ParsedPrimitive}
     *
     * @param value
     *        The value of the primitive
     * @param type
     *        The type of the primitive
     * @since 1.0.0
     */
    protected ParsedPrimitive(Object value, PrimitiveType type) {
        if (type == null) {
            throw new NullPointerException("Type can not be null");
        }

        this.primitive = value;
        this.primitiveType = type;
    }

    /**
     * Get the type of this primitive
     *
     * @return The type of this primitive
     * @see PrimitiveType
     * @since 1.0.0
     */
    public PrimitiveType getType() {
        return this.primitiveType;
    }

    /**
     * Get if the type of this primitive is a string
     *
     * @return If the type of this primitive is a string
     * @since 1.0.0
     */
    public boolean isString() {
        return this.primitiveType == PrimitiveType.STRING;
    }

    /**
     * Get if the type of this primitive is a byte
     *
     * @return If the type of this primitive is a byte
     * @since 1.0.0
     */
    public boolean isByte() {
        return this.primitiveType == PrimitiveType.BYTE;
    }

    /**
     * Get if the type of this primitive is a char
     *
     * @return If the type of this primitive is a char
     * @since 1.0.0
     */
    public boolean isChar() {
        return this.primitiveType == PrimitiveType.CHAR;
    }

    /**
     * Get if the type of this primitive is a short
     *
     * @return If the type of this primitive is a short
     * @since 1.0.0
     */
    public boolean isShort() {
        return this.primitiveType == PrimitiveType.SHORT;
    }

    /**
     * Get if the type of this primitive is an integer
     *
     * @return If the type of this primitive is an integer
     * @since 1.0.0
     */
    public boolean isInteger() {
        return this.primitiveType == PrimitiveType.INTEGER;
    }

    /**
     * Get if the type of this primitive is a big integer
     *
     * @return If the type of this primitive is a big integer
     * @since 1.0.0
     */
    public boolean isBigInteger() {
        return this.primitiveType == PrimitiveType.BIGINTEGER;
    }

    /**
     * Get if the type of this primitive is a long
     *
     * @return If the type of this primitive is a long
     * @since 1.0.0
     */
    public boolean isLong() {
        return this.primitiveType == PrimitiveType.LONG;
    }

    /**
     * Get if the type of this primitive is a float
     *
     * @return If the type of this primitive is a float
     * @since 1.0.0
     */
    public boolean isFloat() {
        return this.primitiveType == PrimitiveType.FLOAT;
    }

    /**
     * Get if the type of this primitive is a double
     *
     * @return If the type of this primitive is a double
     * @since 1.0.0
     */
    public boolean isDouble() {
        return this.primitiveType == PrimitiveType.DOUBLE;
    }

    /**
     * Get if the type of this primitive is a big decimal
     *
     * @return If the type of this primitive is a big decimal
     * @since 1.0.0
     */
    public boolean isBigDecimal() {
        return this.primitiveType == PrimitiveType.BIGDECIMAL;
    }

    /**
     * Get if the type of this primitive is a boolean
     *
     * @return If the type of this primitive is a boolean
     * @since 1.0.0
     */
    public boolean isBoolean() {
        return this.primitiveType == PrimitiveType.BOOLEAN;
    }

    /**
     * Get if the type of this primitive is null
     *
     * @return If the type of this primitive is null
     * @since 1.0.0
     */
    public boolean isNull() {
        return this.primitiveType == PrimitiveType.NULL;
    }

    /**
     * Get the value of this primitive as it's type
     *
     * @return The value of this primitive as it's type
     * @since 1.0.0
     */
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
        } else if (this.primitiveType == PrimitiveType.BIGINTEGER) {
            return (BigInteger) this.primitive;
        } else if (this.primitiveType == PrimitiveType.LONG) {
            return (long) this.primitive;
        } else if (this.primitiveType == PrimitiveType.FLOAT) {
            return (float) (double) this.primitive;
        } else if (this.primitiveType == PrimitiveType.DOUBLE) {
            return (double) this.primitive;
        } else if (this.primitiveType == PrimitiveType.BIGDECIMAL) {
            return (BigDecimal) this.primitive;
        } else if (this.primitiveType == PrimitiveType.BOOLEAN) {
            return (boolean) this.primitive;
        } else if (this.primitiveType == PrimitiveType.NULL) {
            return null;
        } else {
            throw new ClassCastException("Value is not a primitive");
        }
    }

    /**
     * Get the value of this primitive as a string
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a string
     * @since 1.0.0
     */
    public String asString() {
        if (this.primitiveType == PrimitiveType.STRING) {
            return (String) this.primitive;
        } else {
            throw new ClassCastException("Value is not a string");
        }
    }

    /**
     * Get the value of this primitive as a byte
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a byte
     * @since 1.0.0
     */
    public byte asByte() {
        if (this.primitiveType == PrimitiveType.BYTE) {
            return (byte) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a byte");
        }
    }

    /**
     * Get the value of this primitive as a char
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a char
     * @since 1.0.0
     */
    public char asChar() {
        if (this.primitiveType == PrimitiveType.CHAR) {
            return (char) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a char");
        }
    }

    /**
     * Get the value of this primitive as a short
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a short
     * @since 1.0.0
     */
    public short asShort() {
        if (this.primitiveType == PrimitiveType.SHORT) {
            return (short) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a short");
        }
    }

    /**
     * Get the value of this primitive as a integer
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a integer
     * @since 1.0.0
     */
    public int asInteger() {
        if (this.primitiveType == PrimitiveType.INTEGER) {
            return (int) (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a integer");
        }
    }

    /**
     * Get the value of this primitive as a big integer
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a big integer
     * @since 1.0.0
     */
    public BigInteger asBigInteger() {
        if (this.primitiveType == PrimitiveType.BIGINTEGER) {
            return (BigInteger) this.primitive;
        } else {
            throw new ClassCastException("Value is not a big integer");
        }
    }

    /**
     * Get the value of this primitive as a long
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a long
     * @since 1.0.0
     */
    public long asLong() {
        if (this.primitiveType == PrimitiveType.LONG) {
            return (long) this.primitive;
        } else {
            throw new ClassCastException("Value is not a long");
        }
    }

    /**
     * Get the value of this primitive as a float
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a float
     * @since 1.0.0
     */
    public float asFloat() {
        if (this.primitiveType == PrimitiveType.FLOAT) {
            return (float) (double) this.primitive;
        } else {
            throw new ClassCastException("Value is not a float");
        }
    }

    /**
     * Get the value of this primitive as a double
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a double
     * @since 1.0.0
     */
    public double asDouble() {
        if (this.primitiveType == PrimitiveType.DOUBLE) {
            return (double) this.primitive;
        } else {
            throw new ClassCastException("Value is not a double");
        }
    }

    /**
     * Get the value of this primitive as a big decimal
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a big decimal
     * @since 1.0.0
     */
    public BigDecimal asBigDecimal() {
        if (this.primitiveType == PrimitiveType.BIGDECIMAL) {
            return (BigDecimal) this.primitive;
        } else {
            throw new ClassCastException("Value is not a big decimal");
        }
    }

    /**
     * Get the value of this primitive as a boolean
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as a boolean
     * @since 1.0.0
     */
    public boolean asBoolean() {
        if (this.primitiveType == PrimitiveType.BOOLEAN) {
            return (boolean) this.primitive;
        } else {
            throw new ClassCastException("Value is not a boolean");
        }
    }

    /**
     * Get the value of this primitive as null
     * <p>
     * Note: Does not catch casting errors
     *
     * @return The value of this primitive as null
     * @since 1.0.0
     */
    public Object asNull() {
        if (this.primitiveType == PrimitiveType.NULL) {
            return this.primitive;
        } else {
            throw new ClassCastException("Value is not null");
        }
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed value
     *
     * @param value
     *        The value to use
     * @return A new {@link ParsedPrimitive} with the passed value
     * @since 1.0.0
     */
    public static ParsedPrimitive from(Object value) {
        if (value == null) {
            return fromNull();
        } else if (value instanceof String) {
            return fromString((String) value);
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

    /**
     * Create a new {@link ParsedPrimitive} with the passed string
     *
     * @param value
     *        The string to use
     * @return A new {@link ParsedPrimitive} with the passed string
     * @since 1.0.0
     */
    public static ParsedPrimitive fromString(String value) {
        if (value == null) {
            throw new NullPointerException("Value must be a string");
        }

        return new ParsedPrimitive(value, PrimitiveType.STRING);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed byte
     *
     * @param value
     *        The byte to use
     * @return A new {@link ParsedPrimitive} with the passed byte
     * @since 1.0.0
     */
    public static ParsedPrimitive fromByte(byte value) {
        return new ParsedPrimitive((long) value, PrimitiveType.BYTE);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed char
     *
     * @param value
     *        The char to use
     * @return A new {@link ParsedPrimitive} with the passed char
     * @since 1.0.0
     */
    public static ParsedPrimitive fromChar(char value) {
        return new ParsedPrimitive((long) value, PrimitiveType.CHAR);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed short
     *
     * @param value
     *        The short to use
     * @return A new {@link ParsedPrimitive} with the passed short
     * @since 1.0.0
     */
    public static ParsedPrimitive fromShort(short value) {
        return new ParsedPrimitive((long) value, PrimitiveType.SHORT);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed integer
     *
     * @param value
     *        The integer to use
     * @return A new {@link ParsedPrimitive} with the passed integer
     * @since 1.0.0
     */
    public static ParsedPrimitive fromInteger(int value) {
        return new ParsedPrimitive((long) value, PrimitiveType.INTEGER);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed big integer
     *
     * @param value
     *        The big integer to use
     * @return A new {@link ParsedPrimitive} with the passed big integer
     * @since 1.0.0
     */
    public static ParsedPrimitive fromBigInteger(BigInteger value) {
        return new ParsedPrimitive(value, PrimitiveType.BIGINTEGER);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed long
     *
     * @param value
     *        The long to use
     * @return A new {@link ParsedPrimitive} with the passed long
     * @since 1.0.0
     */
    public static ParsedPrimitive fromLong(long value) {
        return new ParsedPrimitive(value, PrimitiveType.LONG);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed float
     *
     * @param value
     *        The float to use
     * @return A new {@link ParsedPrimitive} with the passed float
     * @since 1.0.0
     */
    public static ParsedPrimitive fromFloat(float value) {
        return new ParsedPrimitive((double) value, PrimitiveType.FLOAT);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed double
     *
     * @param value
     *        The double to use
     * @return A new {@link ParsedPrimitive} with the passed double
     * @since 1.0.0
     */
    public static ParsedPrimitive fromDouble(double value) {
        return new ParsedPrimitive(value, PrimitiveType.DOUBLE);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed big decimal
     *
     * @param value
     *        The big decimal to use
     * @return A new {@link ParsedPrimitive} with the passed big decimal
     * @since 1.0.0
     */
    public static ParsedPrimitive fromBigDecimal(BigDecimal value) {
        return new ParsedPrimitive(value, PrimitiveType.BIGDECIMAL);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed boolean
     *
     * @param value
     *        The boolean to use
     * @return A new {@link ParsedPrimitive} with the passed boolean
     * @since 1.0.0
     */
    public static ParsedPrimitive fromBoolean(boolean value) {
        return new ParsedPrimitive(value, PrimitiveType.BOOLEAN);
    }

    /**
     * Create a new {@link ParsedPrimitive} with null
     *
     * @return A new {@link ParsedPrimitive} with null
     * @since 1.0.0
     */
    public static ParsedPrimitive fromNull() {
        return new ParsedPrimitive(null, PrimitiveType.NULL);
    }
}