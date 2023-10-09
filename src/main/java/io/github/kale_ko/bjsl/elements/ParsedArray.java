package io.github.kale_ko.bjsl.elements;

import io.github.kale_ko.bjsl.BJSL;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A wrapper for an ordered map used to represent an Array in most data formats
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParsedArray extends ParsedElement {
    /**
     * The array this object stores
     *
     * @since 1.0.0
     */
    final @NotNull List<ParsedElement> array;

    /**
     * Create a new {@link ParsedArray}
     *
     * @since 1.0.0
     */
    ParsedArray() {
        this(new LinkedList<>());
    }

    /**
     * Create a new {@link ParsedArray} from an array
     *
     * @param array The array to use
     *
     * @since 1.0.0
     */
    ParsedArray(@NotNull List<ParsedElement> array) {
        this.array = array;
    }

    /**
     * Get the amount of values in this array
     *
     * @return The amount of values in this array
     *
     * @since 1.0.0
     */
    public int getSize() {
        return this.array.size();
    }

    /**
     * Get a list of all the values in this array
     * <p>
     * Note: Returns a copy of the list
     *
     * @return A list of all the values in this array
     *
     * @since 1.0.0
     */
    public @NotNull @Unmodifiable List<ParsedElement> getValues() {
        return List.copyOf(this.array);
    }

    /**
     * Get the value of a certain index in this array
     *
     * @param index The index to get
     *
     * @return The value of a certain index in this array
     *
     * @throws java.lang.IndexOutOfBoundsException If there is no value associated with the index
     * @since 1.0.0
     */
    public @NotNull ParsedElement get(int index) {
        if (index >= 0 && index < this.array.size()) {
            return this.array.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Add a value to this array at the end
     *
     * @param value The value to add
     *
     * @since 1.0.0
     */
    public void add(@NotNull ParsedElement value) {
        this.array.add(value);
    }

    /**
     * Add a value to this array at a certain index
     *
     * @param index The index to add it at
     * @param value The value to add
     *
     * @throws java.lang.IndexOutOfBoundsException If there is no value associated with the index
     * @since 1.0.0
     */
    public void addAt(int index, @NotNull ParsedElement value) {
        if (index >= 0 && index <= this.array.size()) {
            this.array.add(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Add a list of values to this array
     *
     * @param values The values to add
     *
     * @since 1.0.0
     */
    public void addAll(@NotNull List<ParsedElement> values) {
        for (ParsedElement value : values) {
            this.add(value);
        }
    }

    /**
     * Set the value of a certain index in this array
     *
     * @param index The index to set
     * @param value The value to set
     *
     * @throws java.lang.IndexOutOfBoundsException If there is no value associated with the index
     * @since 1.0.0
     */
    public void set(int index, @NotNull ParsedElement value) {
        if (index >= 0 && index < this.array.size()) {
            this.array.set(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Remove the value of a certain index in this array
     *
     * @param index The index to remove
     *
     * @throws java.lang.IndexOutOfBoundsException If there is no value associated with the index
     * @since 1.0.0
     */
    public void remove(int index) {
        if (index >= 0 && index < this.array.size()) {
            this.array.remove(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "=" + BJSL.stringifyJson(this);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ParsedArray) {
            return this.array.equals(((ParsedArray) obj).array);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.array.hashCode();
    }

    /**
     * Create a new empty {@link ParsedArray}
     *
     * @return A new empty {@link ParsedArray}
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedArray create() {
        return new ParsedArray();
    }

    /**
     * Create a new {@link ParsedArray} that will be populated with the passed list
     *
     * @param array The list to use to populate the new array
     *
     * @return A new {@link ParsedArray} populated with the passed list
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedArray from(@NotNull List<ParsedElement> array) {
        return new ParsedArray(new LinkedList<>(array));
    }
}