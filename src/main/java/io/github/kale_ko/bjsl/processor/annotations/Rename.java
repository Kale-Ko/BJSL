package io.github.kale_ko.bjsl.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field to have a different name in the output
 *
 * @since 1.8.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Rename {
    /**
     * The new name
     *
     * @return The new name
     *
     * @since 1.8.0
     */
    String name();
}