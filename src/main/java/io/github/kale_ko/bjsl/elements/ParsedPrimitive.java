package io.github.kale_ko.bjsl.elements;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import com.fasterxml.jackson.core.io.BigIntegerParser;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper for a primitive object used to represent String/int/float/etc. values
 * <p>
 * Note: Values are not stored as their normal type but rather higher bit types for ease of conversion
 *
 * @version 2.1.0
 * @since 1.0.0
 */
public class ParsedPrimitive extends ParsedElement {
    /**
     * An enum representing different types of Primitives
     *
     * @version 1.0.0
     * @see ParsedPrimitive
     * @since 1.0.0
     */
    public enum PrimitiveType {
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
         * Represents a {@link Long}
         *
         * @since 1.0.0
         */
        LONG,

        /**
         * Represents a {@link BigInteger}
         *
         * @since 1.0.0
         */
        BIGINTEGER,

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
    final Object primitive;

    /**
     * The type of the primitive
     *
     * @see PrimitiveType
     * @since 1.0.0
     */
    final @NotNull PrimitiveType primitiveType;

    /**
     * Create a new {@link ParsedPrimitive}
     *
     * @param value The value of the primitive
     * @param type  The type of the primitive
     *
     * @since 1.0.0
     */
    ParsedPrimitive(Object value, @NotNull PrimitiveType type) {
        this.primitive = value;
        this.primitiveType = type;
    }

    /**
     * Get the type of this primitive
     *
     * @return The type of this primitive
     *
     * @see PrimitiveType
     * @since 1.0.0
     */
    public @NotNull PrimitiveType getType() {
        return this.primitiveType;
    }

    /**
     * Get if the type of this primitive is a string
     *
     * @return If the type of this primitive is a string
     *
     * @since 1.0.0
     */
    public boolean isString() {
        return this.primitiveType == PrimitiveType.STRING;
    }

    /**
     * Get if the type of this primitive is a byte
     *
     * @return If the type of this primitive is a byte
     *
     * @since 1.0.0
     */
    public boolean isByte() {
        return this.primitiveType == PrimitiveType.BYTE;
    }

    /**
     * Get if the type of this primitive is a char
     *
     * @return If the type of this primitive is a char
     *
     * @since 1.0.0
     */
    public boolean isChar() {
        return this.primitiveType == PrimitiveType.CHAR;
    }

    /**
     * Get if the type of this primitive is a short
     *
     * @return If the type of this primitive is a short
     *
     * @since 1.0.0
     */
    public boolean isShort() {
        return this.primitiveType == PrimitiveType.SHORT;
    }

    /**
     * Get if the type of this primitive is an integer
     *
     * @return If the type of this primitive is an integer
     *
     * @since 1.0.0
     */
    public boolean isInteger() {
        return this.primitiveType == PrimitiveType.INTEGER;
    }

    /**
     * Get if the type of this primitive is a long
     *
     * @return If the type of this primitive is a long
     *
     * @since 1.0.0
     */
    public boolean isLong() {
        return this.primitiveType == PrimitiveType.LONG;
    }

    /**
     * Get if the type of this primitive is a big integer
     *
     * @return If the type of this primitive is a big integer
     *
     * @since 1.0.0
     */
    public boolean isBigInteger() {
        return this.primitiveType == PrimitiveType.BIGINTEGER;
    }

    /**
     * Get if the type of this primitive is a float
     *
     * @return If the type of this primitive is a float
     *
     * @since 1.0.0
     */
    public boolean isFloat() {
        return this.primitiveType == PrimitiveType.FLOAT;
    }

    /**
     * Get if the type of this primitive is a double
     *
     * @return If the type of this primitive is a double
     *
     * @since 1.0.0
     */
    public boolean isDouble() {
        return this.primitiveType == PrimitiveType.DOUBLE;
    }

    /**
     * Get if the type of this primitive is a big decimal
     *
     * @return If the type of this primitive is a big decimal
     *
     * @since 1.0.0
     */
    public boolean isBigDecimal() {
        return this.primitiveType == PrimitiveType.BIGDECIMAL;
    }

    /**
     * Get if the type of this primitive is a boolean
     *
     * @return If the type of this primitive is a boolean
     *
     * @since 1.0.0
     */
    public boolean isBoolean() {
        return this.primitiveType == PrimitiveType.BOOLEAN;
    }

    /**
     * Get if the type of this primitive is null
     *
     * @return If the type of this primitive is null
     *
     * @since 1.0.0
     */
    public boolean isNull() {
        return this.primitiveType == PrimitiveType.NULL;
    }

    /**
     * Get the value of this primitive as it's type
     *
     * @return The value of this primitive as it's type
     *
     * @throws java.lang.ClassCastException If the value is not a primitive
     * @since 1.0.0
     */
    public Object get() {
        switch (this.primitiveType) {
            case STRING -> {
                return (String) this.primitive;
            }
            case BYTE -> {
                return (byte) (long) this.primitive;
            }
            case CHAR -> {
                return (char) (long) this.primitive;
            }
            case SHORT -> {
                return (short) (long) this.primitive;
            }
            case INTEGER -> {
                return (int) (long) this.primitive;
            }
            case LONG -> {
                return (long) this.primitive;
            }
            case BIGINTEGER -> {
                return (BigInteger) this.primitive;
            }
            case FLOAT -> {
                return (float) (double) this.primitive;
            }
            case DOUBLE -> {
                return (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return (BigDecimal) this.primitive;
            }
            case BOOLEAN -> {
                return (boolean) this.primitive;
            }
            case NULL -> {
                return this.primitive;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a string
     *
     * @return The value of this primitive as a string
     *
     * @throws java.lang.ClassCastException If the value is not a String
     * @since 1.0.0
     */
    public @NotNull String asString() {
        if (this.primitiveType != PrimitiveType.STRING) {
            throw new ClassCastException("Value is not a string");
        }
        return (String) this.primitive;
    }

    /**
     * Converts the value of this primitive to a string no matter the type
     *
     * @return The value of this primitive as a string
     *
     * @since 2.1.0
     */
    public @NotNull String toString() {
        switch (this.primitiveType) {
            case STRING -> {
                return (String) this.primitive;
            }
            case BYTE -> {
                return Byte.toString((byte) (long) this.primitive);
            }
            case CHAR -> {
                return Character.toString((char) (long) this.primitive);
            }
            case SHORT -> {
                return Short.toString((short) (long) this.primitive);
            }
            case INTEGER -> {
                return Integer.toString((int) (long) this.primitive);
            }
            case LONG -> {
                return Long.toString((long) this.primitive);
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).toString();
            }
            case FLOAT -> {
                return Float.toString((float) (double) this.primitive);
            }
            case DOUBLE -> {
                return Double.toString((double) this.primitive);
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).toString();
            }
            case BOOLEAN -> {
                return Boolean.toString((boolean) this.primitive);
            }
            case NULL -> {
                return "null";
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a byte
     *
     * @return The value of this primitive as a byte
     *
     * @throws java.lang.ClassCastException If the value is not a Byte
     * @since 1.0.0
     */
    public byte asByte() {
        if (this.primitiveType != PrimitiveType.BYTE) {
            throw new ClassCastException("Value is not a byte");
        }
        return (byte) (long) this.primitive;
    }

    /**
     * Converts the value of this primitive to a byte no matter the type
     *
     * @return The value of this primitive as a byte
     *
     * @since 2.1.0
     */
    public byte toByte() {
        switch (this.primitiveType) {
            case STRING -> {
                return Byte.parseByte((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (byte) (long) this.primitive;
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).byteValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (byte) (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).byteValue();
            }
            case BOOLEAN -> {
                return (byte) ((boolean) this.primitive ? 1 : 0);
            }
            case NULL -> {
                return (byte) 0;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a char
     *
     * @return The value of this primitive as a char
     *
     * @throws java.lang.ClassCastException If the value is not a Character
     * @since 1.0.0
     */
    public char asChar() {
        if (this.primitiveType != PrimitiveType.CHAR) {
            throw new ClassCastException("Value is not a char");
        }
        return (char) (long) this.primitive;
    }

    /**
     * Converts the value of this primitive to a char no matter the type
     *
     * @return The value of this primitive as a char
     *
     * @since 2.1.0
     */
    public char toChar() {
        switch (this.primitiveType) {
            case STRING -> {
                return (char) Short.parseShort((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (char) (long) this.primitive;
            }
            case BIGINTEGER -> {
                return (char) ((BigInteger) this.primitive).shortValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (char) (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return (char) ((BigDecimal) this.primitive).shortValue();
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? '\u0001' : '\u0000';
            }
            case NULL -> {
                return 0;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a short
     *
     * @return The value of this primitive as a short
     *
     * @throws java.lang.ClassCastException If the value is not a Short
     * @since 1.0.0
     */
    public short asShort() {
        if (this.primitiveType != PrimitiveType.SHORT) {
            throw new ClassCastException("Value is not a short");
        }
        return (short) (long) this.primitive;
    }

    /**
     * Converts the value of this primitive to a short no matter the type
     *
     * @return The value of this primitive as a short
     *
     * @since 2.1.0
     */
    public short toShort() {
        switch (this.primitiveType) {
            case STRING -> {
                return Short.parseShort((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (short) (long) this.primitive;
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).shortValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (short) (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).shortValue();
            }
            case BOOLEAN -> {
                return (short) ((boolean) this.primitive ? 1 : 0);
            }
            case NULL -> {
                return (short) 0;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as an integer
     *
     * @return The value of this primitive as an integer
     *
     * @throws java.lang.ClassCastException If the value is not an Integer
     * @since 1.0.0
     */
    public int asInteger() {
        if (this.primitiveType != PrimitiveType.INTEGER) {
            throw new ClassCastException("Value is not a integer");
        }
        return (int) (long) this.primitive;
    }

    /**
     * Converts the value of this primitive to an int no matter the type
     *
     * @return The value of this primitive as an int
     *
     * @since 2.1.0
     */
    public int toInteger() {
        switch (this.primitiveType) {
            case STRING -> {
                return Integer.parseInt((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (int) (long) this.primitive;
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).intValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (int) (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).intValue();
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? 1 : 0;
            }
            case NULL -> {
                return 0;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a long
     *
     * @return The value of this primitive as a long
     *
     * @throws java.lang.ClassCastException If the value is not a Long
     * @since 1.0.0
     */
    public long asLong() {
        if (this.primitiveType != PrimitiveType.LONG) {
            throw new ClassCastException("Value is not a long");
        }
        return (long) this.primitive;
    }

    /**
     * Converts the value of this primitive to a long no matter the type
     *
     * @return The value of this primitive as a long
     *
     * @since 2.1.0
     */
    public long toLong() {
        switch (this.primitiveType) {
            case STRING -> {
                return Long.parseLong((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (long) this.primitive;
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).longValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (long) (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).longValue();
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? 1L : 0L;
            }
            case NULL -> {
                return 0L;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a big integer
     *
     * @return The value of this primitive as a big integer
     *
     * @throws java.lang.ClassCastException If the value is not a BigInteger
     * @since 1.0.0
     */
    public @NotNull BigInteger asBigInteger() {
        if (this.primitiveType != PrimitiveType.BIGINTEGER) {
            throw new ClassCastException("Value is not a big integer");
        }
        return (BigInteger) this.primitive;
    }

    /**
     * Converts the value of this primitive to a big integer no matter the type
     *
     * @return The value of this primitive as a big integer
     *
     * @since 2.1.0
     */
    public @NotNull BigInteger toBigInteger() {
        switch (this.primitiveType) {
            case STRING -> {
                return BigIntegerParser.parseWithFastParser((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return BigInteger.valueOf((long) this.primitive);
            }
            case BIGINTEGER -> {
                return (BigInteger) this.primitive;
            }
            case FLOAT,
                 DOUBLE -> {
                return BigInteger.valueOf((long) Math.round((double) this.primitive));
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).toBigInteger();
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? BigInteger.ONE : BigInteger.ZERO;
            }
            case NULL -> {
                return BigInteger.ZERO;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a float
     *
     * @return The value of this primitive as a float
     *
     * @throws java.lang.ClassCastException If the value is not a Float
     * @since 1.0.0
     */
    public float asFloat() {
        if (this.primitiveType != PrimitiveType.FLOAT) {
            throw new ClassCastException("Value is not a float");
        }
        return (float) (double) this.primitive;
    }

    /**
     * Converts the value of this primitive to a float no matter the type
     *
     * @return The value of this primitive as a float
     *
     * @since 2.1.0
     */
    public float toFloat() {
        switch (this.primitiveType) {
            case STRING -> {
                return Float.parseFloat((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (float) (long) this.primitive;
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).floatValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (float) (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).floatValue();
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? 1.0f : 0.0f;
            }
            case NULL -> {
                return 0.0f;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a double
     *
     * @return The value of this primitive as a double
     *
     * @throws java.lang.ClassCastException If the value is not a Double
     * @since 1.0.0
     */
    public double asDouble() {
        if (this.primitiveType != PrimitiveType.DOUBLE) {
            throw new ClassCastException("Value is not a double");
        }
        return (double) this.primitive;
    }

    /**
     * Converts the value of this primitive to a double no matter the type
     *
     * @return The value of this primitive as a double
     *
     * @since 2.1.0
     */
    public double toDouble() {
        switch (this.primitiveType) {
            case STRING -> {
                return Double.parseDouble((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return (double) (long) this.primitive;
            }
            case BIGINTEGER -> {
                return ((BigInteger) this.primitive).doubleValue();
            }
            case FLOAT,
                 DOUBLE -> {
                return (double) this.primitive;
            }
            case BIGDECIMAL -> {
                return ((BigDecimal) this.primitive).doubleValue();
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? 1.0 : 0.0;
            }
            case NULL -> {
                return 0.0;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a big decimal
     *
     * @return The value of this primitive as a big decimal
     *
     * @throws java.lang.ClassCastException If the value is not a BigDecimal
     * @since 1.0.0
     */
    public @NotNull BigDecimal asBigDecimal() {
        if (this.primitiveType != PrimitiveType.BIGDECIMAL) {
            throw new ClassCastException("Value is not a big decimal");
        }
        return (BigDecimal) this.primitive;
    }

    /**
     * Converts the value of this primitive to a big decimal no matter the type
     *
     * @return The value of this primitive as a big decimal
     *
     * @since 2.1.0
     */
    public @NotNull BigDecimal toBigDecimal() {
        switch (this.primitiveType) {
            case STRING -> {
                return BigDecimalParser.parse((String) this.primitive);
            }
            case BYTE,
                 CHAR,
                 SHORT,
                 INTEGER,
                 LONG -> {
                return BigDecimal.valueOf((long) this.primitive);
            }
            case BIGINTEGER -> {
                return new BigDecimal((BigInteger) this.primitive);
            }
            case FLOAT,
                 DOUBLE -> {
                return BigDecimal.valueOf((double) this.primitive);
            }
            case BIGDECIMAL -> {
                return (BigDecimal) this.primitive;
            }
            case BOOLEAN -> {
                return (boolean) this.primitive ? BigDecimal.ONE : BigDecimal.ZERO;
            }
            case NULL -> {
                return BigDecimal.ZERO;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as a boolean
     *
     * @return The value of this primitive as a boolean
     *
     * @throws java.lang.ClassCastException If the value is not a Boolean
     * @since 1.0.0
     */
    public boolean asBoolean() {
        if (this.primitiveType != PrimitiveType.BOOLEAN) {
            throw new ClassCastException("Value is not a boolean");
        }
        return (boolean) this.primitive;
    }

    /**
     * Converts the value of this primitive to a boolean no matter the type
     *
     * @return The value of this primitive as a boolean
     *
     * @since 2.1.0
     */
    public boolean toBoolean() {
        switch (this.primitiveType) {
            case STRING -> {
                return Boolean.parseBoolean((String) this.primitive);
            }
            case BYTE -> {
                return (byte) (long) this.primitive != 0;
            }
            case CHAR -> {
                return (char) (long) this.primitive != 0;
            }
            case SHORT -> {
                return (short) (long) this.primitive != 0;
            }
            case INTEGER -> {
                return (int) (long) this.primitive != 0;
            }
            case LONG -> {
                return (long) this.primitive != 0;
            }
            case BIGINTEGER -> {
                return !((BigInteger) this.primitive).equals(BigInteger.ZERO);
            }
            case FLOAT -> {
                return (float) (double) this.primitive != 0;
            }
            case DOUBLE -> {
                return (double) this.primitive != 0;
            }
            case BIGDECIMAL -> {
                return !((BigDecimal) this.primitive).equals(BigDecimal.ZERO);
            }
            case BOOLEAN -> {
                return (boolean) this.primitive;
            }
            case NULL -> {
                return false;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Converts the value of this primitive to the specified type no matter what type it is
     *
     * @param primitiveType The type to convert to
     *
     * @return The value of this primitive as the specified type
     *
     * @since 2.1.0
     */
    public @Nullable Object to(@NotNull PrimitiveType primitiveType) {
        switch (primitiveType) {
            case STRING -> {
                return this.toString();
            }
            case BYTE -> {
                return this.toByte();
            }
            case CHAR -> {
                return this.toChar();
            }
            case SHORT -> {
                return this.toShort();
            }
            case INTEGER -> {
                return this.toInteger();
            }
            case LONG -> {
                return this.toLong();
            }
            case BIGINTEGER -> {
                return this.toBigInteger();
            }
            case FLOAT -> {
                return this.toFloat();
            }
            case DOUBLE -> {
                return this.toDouble();
            }
            case BIGDECIMAL -> {
                return this.toBigDecimal();
            }
            case BOOLEAN -> {
                return this.toBoolean();
            }
            case NULL -> {
                return null;
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Get the value of this primitive as null
     *
     * @return The value of this primitive as null
     *
     * @throws java.lang.ClassCastException If the value is not Null
     * @since 1.0.0
     */
    public @Nullable Object asNull() {
        if (this.primitiveType != PrimitiveType.NULL) {
            throw new ClassCastException("Value is not null");
        }
        return this.primitive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return ((ParsedPrimitive) obj).primitiveType == this.primitiveType && (((ParsedPrimitive) obj).primitive != null ? ((ParsedPrimitive) obj).primitive.equals(this.primitive) : this.primitive == null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = this.primitiveType.hashCode();
        result = 31 * result + (this.primitive != null ? this.primitive.hashCode() : 0);
        return result;
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed value
     *
     * @param value The value to use
     *
     * @return A new {@link ParsedPrimitive} with the passed value
     *
     * @throws java.lang.ClassCastException If the value is not a primitive
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive from(@Nullable Object value) {
        if (value == null) {
            return fromNull();
        }

        switch (value) {
            case String string -> {
                return fromString(string);
            }
            case Byte byteValue -> {
                return fromByte(byteValue);
            }
            case Character charValue -> {
                return fromChar(charValue);
            }
            case Short shortValue -> {
                return fromShort(shortValue);
            }
            case Integer intValue -> {
                return fromInteger(intValue);
            }
            case Long longValue -> {
                return fromLong(longValue);
            }
            case BigInteger bigIntegerValue -> {
                return fromBigInteger(bigIntegerValue);
            }
            case Float floatValue -> {
                return fromFloat(floatValue);
            }
            case Double doubleValue -> {
                return fromDouble(doubleValue);
            }
            case BigDecimal bigDecimalValue -> {
                return fromBigDecimal(bigDecimalValue);
            }
            case Boolean booleanValue -> {
                return fromBoolean(booleanValue);
            }
            default -> {
                throw new ClassCastException("Value is not a primitive");
            }
        }
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed string
     *
     * @param value The string to use
     *
     * @return A new {@link ParsedPrimitive} with the passed string
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromString(@NotNull String value) {
        return new ParsedPrimitive(value, PrimitiveType.STRING);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed byte
     *
     * @param value The byte to use
     *
     * @return A new {@link ParsedPrimitive} with the passed byte
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromByte(byte value) {
        return new ParsedPrimitive((long) value, PrimitiveType.BYTE);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed char
     *
     * @param value The char to use
     *
     * @return A new {@link ParsedPrimitive} with the passed char
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromChar(char value) {
        return new ParsedPrimitive((long) value, PrimitiveType.CHAR);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed short
     *
     * @param value The short to use
     *
     * @return A new {@link ParsedPrimitive} with the passed short
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromShort(short value) {
        return new ParsedPrimitive((long) value, PrimitiveType.SHORT);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed integer
     *
     * @param value The integer to use
     *
     * @return A new {@link ParsedPrimitive} with the passed integer
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromInteger(int value) {
        return new ParsedPrimitive((long) value, PrimitiveType.INTEGER);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed long
     *
     * @param value The long to use
     *
     * @return A new {@link ParsedPrimitive} with the passed long
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromLong(long value) {
        return new ParsedPrimitive(value, PrimitiveType.LONG);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed big integer
     *
     * @param value The big integer to use
     *
     * @return A new {@link ParsedPrimitive} with the passed big integer
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromBigInteger(@NotNull BigInteger value) {
        return new ParsedPrimitive(value, PrimitiveType.BIGINTEGER);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed float
     *
     * @param value The float to use
     *
     * @return A new {@link ParsedPrimitive} with the passed float
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromFloat(float value) {
        return new ParsedPrimitive((double) value, PrimitiveType.FLOAT);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed double
     *
     * @param value The double to use
     *
     * @return A new {@link ParsedPrimitive} with the passed double
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromDouble(double value) {
        return new ParsedPrimitive(value, PrimitiveType.DOUBLE);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed big decimal
     *
     * @param value The big decimal to use
     *
     * @return A new {@link ParsedPrimitive} with the passed big decimal
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromBigDecimal(@NotNull BigDecimal value) {
        return new ParsedPrimitive(value, PrimitiveType.BIGDECIMAL);
    }

    /**
     * Create a new {@link ParsedPrimitive} with the passed boolean
     *
     * @param value The boolean to use
     *
     * @return A new {@link ParsedPrimitive} with the passed boolean
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromBoolean(boolean value) {
        return new ParsedPrimitive(value, PrimitiveType.BOOLEAN);
    }

    /**
     * Create a new {@link ParsedPrimitive} with null
     *
     * @return A new {@link ParsedPrimitive} with null
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedPrimitive fromNull() {
        return new ParsedPrimitive(null, PrimitiveType.NULL);
    }
}