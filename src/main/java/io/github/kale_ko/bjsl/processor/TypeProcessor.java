package io.github.kale_ko.bjsl.processor;

import io.github.kale_ko.bjsl.elements.ParsedElement;

public interface TypeProcessor {
    public ParsedElement toElement(Object object);

    public Object toObject(ParsedElement element);
}