package io.github.kale_ko.bjsl.parsers.exception;

/**
 * Thrown when an invalid/unknown type is passed to a method that can't handle it
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class InvalidTypeException extends RuntimeException {
    /**
     * Create a new InvalidTypeException
     *
     * @param type The type that was unable to be handled
     */
    public InvalidTypeException(Class<?> type) {
        super("\"" + type.getSimpleName() + "\" is not a processable type");
    }
}