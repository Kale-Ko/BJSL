package io.github.kale_ko.bjsl.processor;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type processor for converting elements to objects and objects to elements
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TypeProcessor {
    /**
     * Converts an Object into a {@link ParsedElement}
     *
     * @param object The object to convert
     *
     * @return A new {@link ParsedElement} representing the Object
     *
     * @since 1.0.0
     */
    public @NotNull ParsedElement toElement(@Nullable Object object);

    /**
     * Converts a {@link ParsedElement} into an Object
     *
     * @param element The element to convert
     *
     * @return A new Object representing the element
     *
     * @since 1.0.0
     */
    public @Nullable Object toObject(@NotNull ParsedElement element);
}