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

    public boolean isJsonObject() {
        return this.object != null;
    }

    public boolean isJsonArray() {
        return this.array != null;
    }

    public boolean isJsonPrimitive() {
        return this.primitive != null;
    }

    public ParsedObject asJsonObject() {
        return ParsedObject.from(this.object);
    }

    public ParsedArray asJsonArray() {
        return ParsedArray.from(this.array);
    }

    public ParsedPrimitive asJsonPrimitive() {
        return ParsedPrimitive.fromObject(this.primitive);
    }
}