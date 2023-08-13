package io.github.kale_ko.bjsl.parsers.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when an exception occurs during parsing
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class ParsingException extends RuntimeException {
    /**
     * Create a new ParsingException
     *
     * @param cause The cause of the exception
     */
    public ParsingException(@NotNull Exception cause) {
        super("Error while parsing:", cause);
    }
}