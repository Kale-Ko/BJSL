package io.github.kale_ko.bjsl.processor.exception;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a value is attempted to be used as a map key but is not a string or other string-able primitive.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class NotAPrimitiveException extends RuntimeException {
    /**
     * Create a new NotAPrimitiveException
     *
     * @param parsedObject The parsed object that was invalid
     */
    public NotAPrimitiveException(@NotNull ParsedElement parsedObject) {
        super("ParedElement is not a string or other string-able primitive, must be to be a map key!\n" + parsedObject);
    }
}