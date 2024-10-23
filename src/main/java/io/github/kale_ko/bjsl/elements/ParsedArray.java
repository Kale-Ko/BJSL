package io.github.kale_ko.bjsl.elements;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A wrapper for an ordered map used to represent an Array in most data formats
 *
 * @version 2.0.0
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
    public @NotNull @Unmodifiable Collection<ParsedElement> getValues() {
        return Collections.unmodifiableCollection(this.array);
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
        return this.array.get(index);
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
        this.array.add(index, value);
    }

    /**
     * Add a list of values to this array
     *
     * @param values The values to add
     *
     * @since 1.0.0
     */
    public void addAll(@NotNull Collection<ParsedElement> values) {
        this.array.addAll(values);
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
        this.array.set(index, value);
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
        this.array.remove(index);
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "[array=" + this.array + "]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return ((ParsedArray) obj).array.equals(this.array);
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
        return new ParsedArray(new LinkedList<>());
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
    public static @NotNull ParsedArray from(@NotNull Collection<ParsedElement> array) {
        return new ParsedArray(new LinkedList<>(array));
    }
}