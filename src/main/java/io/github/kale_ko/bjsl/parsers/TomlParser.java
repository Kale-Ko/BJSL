package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.dataformat.toml.TomlFactory;

public class TomlParser extends Parser {
    public TomlParser() {
        super(TomlFactory.builder().build());
    }
}