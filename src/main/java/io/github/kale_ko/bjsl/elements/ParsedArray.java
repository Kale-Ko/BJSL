package io.github.kale_ko.bjsl.elements;

import java.util.ArrayList;
import java.util.List;

public class ParsedArray extends ParsedElement {
    private ParsedArray() {
        this(new ArrayList<ParsedElement>());
    }

    private ParsedArray(ArrayList<ParsedElement> array) {
        super(null, array, null);
    }

    public int getSize() {
        return this.array.size();
    }

    public List<ParsedElement> getValues() {
        return new ArrayList<ParsedElement>(this.array);
    }

    public ParsedElement get(int index) {
        if (index < this.array.size()) {
            return this.array.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    public void add(ParsedElement value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        this.array.add(value);
    }

    public void addAt(int index, ParsedElement value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        if (index <= this.array.size()) {
            this.array.add(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    public void addAll(List<ParsedElement> values) {
        if (values == null) {
            throw new NullPointerException("Values can not be null");
        }

        for (ParsedElement value : values) {
            add(value);
        }
    }

    public void set(int index, ParsedElement value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        if (index < this.array.size()) {
            this.array.set(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    public void remove(int index) {
        if (index < this.array.size()) {
            this.array.remove(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + this.array.size());
        }
    }

    public static ParsedArray create() {
        return new ParsedArray();
    }

    public static ParsedArray from(List<ParsedElement> array) {
        if (array == null) {
            throw new NullPointerException("Array can not be null");
        }

        return new ParsedArray(new ArrayList<ParsedElement>(array));
    }
}