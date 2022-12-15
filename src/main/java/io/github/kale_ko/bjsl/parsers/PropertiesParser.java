package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;

public class PropertiesParser extends Parser {
    public PropertiesParser() {
        super(JavaPropsFactory.builder().build(), null);
    }
}