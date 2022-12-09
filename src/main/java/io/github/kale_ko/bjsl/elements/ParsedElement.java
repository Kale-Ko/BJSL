package io.github.kale_ko.bjsl.elements;

import java.util.List;
import java.util.SortedMap;

public class ParsedElement {
    protected SortedMap<String, ParsedElement> object;
    protected List<ParsedElement> array;
    protected Object primitive;

    protected ParsedElement(SortedMap<String, ParsedElement> map, List<ParsedElement> array, Object primitive) {
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