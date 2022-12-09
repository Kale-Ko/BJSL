package io.github.kale_ko.bjsl.elements;

import java.util.ArrayList;
import java.util.List;

public class ParsedArray extends ParsedElement {
    private ParsedArray() {
        this(new ArrayList<ParsedElement>());
    }

    private ParsedArray(List<ParsedElement> array) {
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

    public static ParsedArray from(List<ParsedElement> array) {
        return new ParsedArray(array);
    }
}