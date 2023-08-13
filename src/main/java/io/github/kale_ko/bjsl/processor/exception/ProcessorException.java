package io.github.kale_ko.bjsl.processor.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when an exception occurs during processing
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class ProcessorException extends RuntimeException {
    /**
     * Create a new ProcessorException
     *
     * @param cause The cause of the exception
     */
    public ProcessorException(@NotNull Exception cause) {
        super("Error while processing:", cause);
    }
}