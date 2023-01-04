package io.github.kale_ko.bjsl.elements;

public abstract class ParsedElement {
    public boolean isObject() {
        return this instanceof ParsedObject;
    }

    public boolean isArray() {
        return this instanceof ParsedArray;
    }

    public boolean isPrimitive() {
        return this instanceof ParsedPrimitive;
    }

    public ParsedObject asObject() {
        return (ParsedObject) this;
    }

    public ParsedArray asArray() {
        return (ParsedArray) this;
    }

    public ParsedPrimitive asPrimitive() {
        return (ParsedPrimitive) this;
    }
}