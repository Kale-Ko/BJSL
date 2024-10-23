package io.github.kale_ko.bjsl.processor.conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Expect that a field is always greater than a value. The type of the field will be used to pick which value is read.
 *
 * @since 2.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectGreaterThan {
    /**
     * The value as an integer
     *
     * @return The value as an integer
     *
     * @since 2.0.0
     */
    int intValue() default Integer.MIN_VALUE;

    /**
     * The value as a long
     *
     * @return The value as a long
     *
     * @since 2.0.0
     */
    long longValue() default Long.MIN_VALUE;

    /**
     * The value as a float
     *
     * @return The value as a float
     *
     * @since 2.0.0
     */
    float floatValue() default Float.MIN_VALUE;

    /**
     * The value as a double
     *
     * @return The value as a double
     *
     * @since 2.0.0
     */
    double doubleValue() default Double.MIN_VALUE;

    /**
     * If the check should be greater than or equal to instead of greater than
     *
     * @return If the check should be greater than or equal to instead of greater than
     *
     * @since 2.0.0
     */
    boolean orEqual() default false;
}