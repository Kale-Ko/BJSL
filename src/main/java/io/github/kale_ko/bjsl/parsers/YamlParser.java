package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlParser extends Parser {
    public YamlParser() {
        super(YAMLFactory.builder().build(), null);
    }
}