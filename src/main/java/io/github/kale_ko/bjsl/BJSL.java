package io.github.kale_ko.bjsl;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.JsonParser;
import io.github.kale_ko.bjsl.parsers.PropertiesParser;
import io.github.kale_ko.bjsl.parsers.TomlParser;
import io.github.kale_ko.bjsl.parsers.YamlParser;

public class BJSL {
    public static ParsedElement parseJson(String json) {
        return JsonParser.parse(json);
    }

    public static String stringifyJson(ParsedElement json) {
        return JsonParser.stringify(json);
    }

    public static ParsedElement parseYaml(String yaml) {
        return YamlParser.parse(yaml);
    }

    public static String stringifyYaml(ParsedElement yaml) {
        return YamlParser.stringify(yaml);
    }

    public static ParsedElement parseToml(String toml) {
        return TomlParser.parse(toml);
    }

    public static String stringifyToml(ParsedElement toml) {
        return TomlParser.stringify(toml);
    }

    public static ParsedElement parseProperties(String properties) {
        return PropertiesParser.parse(properties);
    }

    public static String stringifyProperties(ParsedElement properties) {
        return PropertiesParser.stringify(properties);
    }
}