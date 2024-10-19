package io.github.kale_ko.bjsl.processor.conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Expect that a field is always less than a value. The type of the field will be used to pick which value is read.
 *
 * @since 1.11.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectLessThan {
    /**
     * The value as an integer
     *
     * @return The value as an integer
     *
     * @since 2.0.0
     */
    int intValue() default Integer.MAX_VALUE;

    /**
     * The value as a long
     *
     * @return The value as a long
     *
     * @since 2.0.0
     */
    long longValue() default Long.MAX_VALUE;

    /**
     * The value as a float
     *
     * @return The value as a float
     *
     * @since 2.0.0
     */
    float floatValue() default Float.MAX_VALUE;

    /**
     * The value as a double
     *
     * @return The value as a double
     *
     * @since 2.0.0
     */
    double doubleValue() default Double.MAX_VALUE;

    /**
     * If the check should be less than or equal to instead of less than
     *
     * @return If the check should be less than or equal to instead of less than
     *
     * @since 2.0.0
     */
    boolean orEqual() default false;
}