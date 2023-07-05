package io.github.kale_ko.bjsl.processor.exception;

/**
 * Thrown when an invalid/unknown type is attempted to be parsed as an enum
 *
 * @version 1.7.0
 * @since 1.7.0
 */
public class EnumExpectedException extends RuntimeException {
    /**
     * Create a new EnumExpectedException
     *
     * @param type The type that was invalid
     */
    public EnumExpectedException(Class<?> type) {
        super("Element for type \"" + type.getSimpleName() + "\" is not a enum");
    }
}