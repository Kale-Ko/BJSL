package io.github.kale_ko.bjsl.elements;

import org.jetbrains.annotations.NotNull;

/**
 * An abstract class that all element types extend from
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ParsedElement {
    /**
     * Create a new {@link ParsedElement}
     *
     * @since 1.0.0
     */
    ParsedElement() {
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    /**
     * Check if this element is a {@link ParsedObject}
     *
     * @return If the element is a {@link ParsedObject}
     *
     * @since 1.0.0
     */
    public boolean isObject() {
        return this instanceof ParsedObject;
    }

    /**
     * Check if this element is a {@link ParsedArray}
     *
     * @return If the element is a {@link ParsedArray}
     *
     * @since 1.0.0
     */
    public boolean isArray() {
        return this instanceof ParsedArray;
    }

    /**
     * Check if this element is a {@link ParsedPrimitive}
     *
     * @return If the element is a {@link ParsedPrimitive}
     *
     * @since 1.0.0
     */
    public boolean isPrimitive() {
        return this instanceof ParsedPrimitive;
    }

    /**
     * Cast this element to a {@link ParsedObject}
     * <p>
     * Note: Does not catch casting errors
     *
     * @return This element as a {@link ParsedObject}
     *
     * @throws java.lang.ClassCastException If this element is not an object
     * @since 1.0.0
     */
    public @NotNull ParsedObject asObject() {
        return (ParsedObject) this;
    }

    /**
     * Cast this element to a {@link ParsedArray}
     * <p>
     * Note: Does not catch casting errors
     *
     * @return This element as a {@link ParsedArray}
     *
     * @throws java.lang.ClassCastException If this element is not an array
     * @since 1.0.0
     */
    public @NotNull ParsedArray asArray() {
        return (ParsedArray) this;
    }

    /**
     * Cast this element to a {@link ParsedPrimitive}
     * <p>
     * Note: Does not catch casting errors
     *
     * @return This element as a {@link ParsedPrimitive}
     *
     * @throws java.lang.ClassCastException If this element is not a primitive
     * @since 1.0.0
     */
    public @NotNull ParsedPrimitive asPrimitive() {
        return (ParsedPrimitive) this;
    }
}