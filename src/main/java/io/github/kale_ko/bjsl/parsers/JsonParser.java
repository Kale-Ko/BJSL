package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.JsonFactory;

public class JsonParser extends Parser {
    public JsonParser() {
        super(JsonFactory.builder().build());
    }
}