package io.github.kale_ko.bjsl.elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ParsedElement {
    protected LinkedHashMap<String, ParsedElement> object;
    protected ArrayList<ParsedElement> array;
    protected Object primitive;

    protected ParsedElement(LinkedHashMap<String, ParsedElement> map, ArrayList<ParsedElement> array, Object primitive) {
        this.object = map;
        this.array = array;
        this.primitive = primitive;
    }

    public boolean isObject() {
        return this.object != null;
    }

    public boolean isArray() {
        return this.array != null;
    }

    public boolean isPrimitive() {
        return this.primitive != null;
    }

    public ParsedObject asObject() {
        return ParsedObject.from(this.object);
    }

    public ParsedArray asArray() {
        return ParsedArray.from(this.array);
    }

    public ParsedPrimitive asPrimitive() {
        return ParsedPrimitive.fromObject(this.primitive);
    }
}