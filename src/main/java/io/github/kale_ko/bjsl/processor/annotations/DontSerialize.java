package io.github.kale_ko.bjsl.processor.annotations;

import java.lang.annotation.*;

/**
 * Mark a field to never be serialized no matter what it contains
 *
 * @since 1.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DontSerialize {
}