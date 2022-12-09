package io.github.kale_ko.bjsl.elements;

import java.util.ArrayList;

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

    public ArrayList<ParsedElement> getValues() {
        return new ArrayList<ParsedElement>(this.array);
    }

    public ParsedElement get(int index) {
        if (index < this.array.size()) {
            return this.array.get(index);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public void add(ParsedElement value) {
        this.array.add(value);
    }

    public void addAt(int index, ParsedElement value) {
        if (index <= this.array.size()) {
            this.array.add(index, value);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public void set(int index, ParsedElement value) {
        if (index < this.array.size()) {
            this.array.set(index, value);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public void remove(int index) {
        if (index < this.array.size()) {
            this.array.remove(index);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public static ParsedArray create() {
        return new ParsedArray();
    }

    public static ParsedArray from(ArrayList<ParsedElement> array) {
        return new ParsedArray(array);
    }
}