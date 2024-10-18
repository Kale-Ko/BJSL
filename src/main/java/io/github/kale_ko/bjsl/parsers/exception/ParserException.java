package io.github.kale_ko.bjsl.parsers.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when an exception occurs during parsing
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class ParserException extends RuntimeException {
    /**
     * Create a new ParserException
     *
     * @param cause The cause of the exception
     */
    public ParserException(@NotNull Exception cause) {
        super("Error while parsing:", cause);
    }
}