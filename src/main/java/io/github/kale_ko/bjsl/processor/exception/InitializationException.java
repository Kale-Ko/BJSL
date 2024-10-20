package io.github.kale_ko.bjsl.processor.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a type can't be initialized
 *
 * @version 2.0.0
 * @since 1.7.0
 */
public class InitializationException extends RuntimeException {
    /**
     * Create a new InitializationException
     *
     * @param type The type that was unable to be initialized
     */
    public InitializationException(@NotNull Class<?> type) {
        super("No 0-args constructors for \"" + type.getSimpleName() + "\" found");
    }

    /**
     * Create a new InitializationException
     *
     * @param cause The cause of the exception
     */
    public InitializationException(@NotNull Exception cause) {
        super(cause);
    }
}