package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.dataformat.smile.SmileFactory;

public class SmileParser extends Parser {
    public SmileParser() {
        super(SmileFactory.builder().build(), null);
    }
}