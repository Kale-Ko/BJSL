package io.github.kale_ko.bjsl.processor.exception;

/**
 * Thrown when a type can't be initialized
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class InitializationException extends RuntimeException {
    /**
     * Create a new InitializationException
     *
     * @param type The type that was unable to be initialized
     */
    public InitializationException(Class<?> type) {
        super("No 0-args constructors for \"" + type.getSimpleName() + "\" found and unsafe initialization failed");
    }
}