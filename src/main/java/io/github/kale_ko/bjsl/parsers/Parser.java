package io.github.kale_ko.bjsl.parsers;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class that all parsers extend from
 *
 * @version 2.1.0
 * @since 1.0.0
 */
public interface Parser {
    /**
     * Parse this string into a {@link ParsedElement}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.0.0
     */
    public @NotNull ParsedElement toElement(@NotNull String data);

    /**
     * Parse these bytes into a {@link ParsedElement}
     *
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.0.0
     */
    public @NotNull ParsedElement toElement(byte @NotNull [] data);

    /**
     * Serializes this element into a String
     *
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.0.0
     */
    public @NotNull String toString(@NotNull ParsedElement element);


    /**
     * Serializes this element into bytes
     *
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.0.0
     */
    public byte @NotNull [] toBytes(@NotNull ParsedElement element);

    /**
     * Serializes an empty object element into a string
     *
     * @return A string for a new/empty object
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.3.0
     */
    public @NotNull String emptyString();

    /**
     * Serializes an empty array element into a string
     *
     * @return A string for a new/empty array
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.4.0
     */
    public @NotNull String emptyArrayString();

    /**
     * Serializes an empty object element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.3.0
     */
    public byte @NotNull [] emptyBytes();

    /**
     * Serializes an empty array element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @throws io.github.kale_ko.bjsl.parsers.exception.ParserException If there is an exception while parsing
     * @since 1.4.0
     */
    public byte @NotNull [] emptyArrayBytes();
}