package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.dataformat.smile.SmileFactory;

public class BinaryParser extends Parser {
    public BinaryParser() {
        super(SmileFactory.builder().build(), null);
    }
}