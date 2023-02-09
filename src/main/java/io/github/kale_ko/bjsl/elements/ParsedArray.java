package io.github.kale_ko.bjsl.elements;

import java.util.ArrayList;
import java.util.List;

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
    protected ArrayList<ParsedElement> array;

    /**
     * Create a new {@link ParsedArray}
     *
     * @since 1.0.0
     */
    protected ParsedArray() {
        this(new ArrayList<ParsedElement>());
    }

    /**
     * Create a new {@link ParsedArray} from an array
     *
     * @param array
     *        The array to use
     * @since 1.0.0
     */
    protected ParsedArray(ArrayList<ParsedElement> array) {
        this.array = array;
    }

    /**
     * Get the amount of values in this array
     *
     * @return The amount of values in this array
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
     * @since 1.0.0
     */
    public List<ParsedElement> getValues() {
        return new ArrayList<ParsedElement>(this.array);
    }

    /**
     * Get the value of a certain index in this array
     *
     * @param index
     *        The index to get
     * @return The value of a certain index in this array
     * @since 1.0.0
     */
    public ParsedElement get(int index) {
        if (index >= 0 && index < this.array.size()) {
            return this.array.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Add a value to this array at the end
     *
     * @param value
     *        The value to add
     * @since 1.0.0
     */
    public void add(ParsedElement value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        this.array.add(value);
    }

    /**
     * Add a value to this array at a certain index
     *
     * @param index
     *        The index to add it at
     * @param value
     *        The value to add
     * @since 1.0.0
     */
    public void addAt(int index, ParsedElement value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        if (index >= 0 && index <= this.array.size()) {
            this.array.add(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Add a list of values to this array
     *
     * @param values
     *        The values to add
     * @since 1.0.0
     */
    public void addAll(List<ParsedElement> values) {
        if (values == null) {
            throw new NullPointerException("Values can not be null");
        }

        for (ParsedElement value : values) {
            add(value);
        }
    }

    /**
     * Set the value of a certain index in this array
     *
     * @param index
     *        The index to set
     * @param value
     *        The value to set
     * @since 1.0.0
     */
    public void set(int index, ParsedElement value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        if (index >= 0 && index < this.array.size()) {
            this.array.set(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Remove the value of a certain index in this array
     *
     * @param index
     *        The index to remove
     * @since 1.0.0
     */
    public void remove(int index) {
        if (index >= 0 && index < this.array.size()) {
            this.array.remove(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    /**
     * Create a new empty {@link ParsedArray}
     *
     * @return A new empty {@link ParsedArray}
     * @since 1.0.0
     */
    public static ParsedArray create() {
        return new ParsedArray();
    }

    /**
     * Create a new {@link ParsedArray} that will be populated with the passed list
     *
     * @param array
     *        The list to use to populate the new array
     * @return A new {@link ParsedArray} populated with the passed list
     * @since 1.0.0
     */
    public static ParsedArray from(List<ParsedElement> array) {
        if (array == null) {
            throw new NullPointerException("Array can not be null");
        }

        return new ParsedArray(new ArrayList<ParsedElement>(array));
    }
}