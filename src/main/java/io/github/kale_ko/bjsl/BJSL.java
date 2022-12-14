package io.github.kale_ko.bjsl;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.JsonParser;
import io.github.kale_ko.bjsl.parsers.PropertiesParser;
import io.github.kale_ko.bjsl.parsers.TomlParser;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;

public class BJSL {
    public static ParsedElement parseJson(String json) {
        return new JsonParser().toElement(json);
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        return ObjectProcessor.toObject(new JsonParser().toElement(json), clazz);
    }

    public static String stringifyJson(ParsedElement json) {
        return new JsonParser().toString(json);
    }

    public static ParsedElement parseYaml(String yaml) {
        return new YamlParser().toElement(yaml);
    }

    public static <T> T parseYaml(String yaml, Class<T> clazz) {
        return ObjectProcessor.toObject(new YamlParser().toElement(yaml), clazz);
    }

    public static String stringifyYaml(ParsedElement yaml) {
        return new YamlParser().toString(yaml);
    }

    public static ParsedElement parseToml(String toml) {
        return new TomlParser().toElement(toml);
    }

    public static <T> T parseToml(String toml, Class<T> clazz) {
        return ObjectProcessor.toObject(new TomlParser().toElement(toml), clazz);
    }

    public static String stringifyToml(ParsedElement toml) {
        return new TomlParser().toString(toml);
    }

    public static ParsedElement parseProperties(String properties) {
        return new PropertiesParser().toElement(properties);
    }

    public static <T> T parseProperties(String properties, Class<T> clazz) {
        return ObjectProcessor.toObject(new PropertiesParser().toElement(properties), clazz);
    }

    public static String stringifyProperties(ParsedElement properties) {
        return new PropertiesParser().toString(properties);
    }
}