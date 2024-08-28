package io.github.kale_ko.bjsl.processor.exception;

/**
 * Thrown when an expectation isn't met
 *
 * @version 1.11.0
 * @since 1.11.0
 */
public class ExpectFailedException extends RuntimeException {
    /**
     * Create a new ExpectFailedException
     *
     * @param expectation The expectation that was failed
     */
    public ExpectFailedException(String expectation) {
        super("Expectation failed: " + expectation);
    }
}