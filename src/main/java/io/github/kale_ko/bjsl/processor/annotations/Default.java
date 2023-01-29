package io.github.kale_ko.bjsl.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the default value for a field
 *
 * @since 1.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    /**
     * The value as a string
     *
     * @return The value as a string
     * @since 1.0.0
     */
    public String stringValue() default "";

    /**
     * The value as a byte
     *
     * @return The value as a byte
     * @since 1.0.0
     */
    public byte byteValue() default Byte.MIN_VALUE;

    /**
     * The value as a char
     *
     * @return The value as a char
     * @since 1.0.0
     */
    public char charValue() default Character.MIN_VALUE;

    /**
     * The value as a short
     *
     * @return The value as a short
     * @since 1.0.0
     */
    public short shortValue() default Short.MIN_VALUE;

    /**
     * The value as a integer
     *
     * @return The value as a integer
     * @since 1.0.0
     */
    public int intValue() default Integer.MIN_VALUE;

    /**
     * The value as a long
     *
     * @return The value as a long
     * @since 1.0.0
     */
    public long longValue() default Long.MIN_VALUE;

    /**
     * The value as a float
     *
     * @return The value as a float
     * @since 1.0.0
     */
    public float floatValue() default Float.MIN_VALUE;

    /**
     * The value as a double
     *
     * @return The value as a double
     * @since 1.0.0
     */
    public double doubleValue() default Double.MIN_VALUE;
}