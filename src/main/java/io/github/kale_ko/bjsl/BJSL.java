package io.github.kale_ko.bjsl;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.BinaryParser;
import io.github.kale_ko.bjsl.parsers.JsonParser;
import io.github.kale_ko.bjsl.parsers.PropertiesParser;
import io.github.kale_ko.bjsl.parsers.TomlParser;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;

public class BJSL {
    private static ObjectProcessor objectProcessor = new ObjectProcessor();

    private static JsonParser jsonParser = new JsonParser();
    private static YamlParser yamlParser = new YamlParser();
    private static TomlParser tomlParser = new TomlParser();
    private static PropertiesParser propertiesParser = new PropertiesParser();
    private static BinaryParser binaryParser = new BinaryParser();

    public static ParsedElement parseJson(String data) {
        return jsonParser.toElement(data);
    }

    public static <T> T parseJson(String data, Class<T> clazz) {
        return objectProcessor.toObject(jsonParser.toElement(data), clazz);
    }

    public static String stringifyJson(ParsedElement element) {
        return jsonParser.toString(element);
    }

    public static ParsedElement parseYaml(String data) {
        return yamlParser.toElement(data);
    }

    public static <T> T parseYaml(String data, Class<T> clazz) {
        return objectProcessor.toObject(yamlParser.toElement(data), clazz);
    }

    public static String stringifyYaml(ParsedElement element) {
        return yamlParser.toString(element);
    }

    public static ParsedElement parseToml(String data) {
        return tomlParser.toElement(data);
    }

    public static <T> T parseToml(String data, Class<T> clazz) {
        return objectProcessor.toObject(tomlParser.toElement(data), clazz);
    }

    public static String stringifyToml(ParsedElement element) {
        return tomlParser.toString(element);
    }

    public static ParsedElement parseProperties(String data) {
        return propertiesParser.toElement(data);
    }

    public static <T> T parseProperties(String data, Class<T> clazz) {
        return objectProcessor.toObject(propertiesParser.toElement(data), clazz);
    }

    public static String stringifyProperties(ParsedElement element) {
        return propertiesParser.toString(element);
    }

    public static ParsedElement parseBinary(String data) {
        return binaryParser.toElement(data);
    }

    public static <T> T parseBinary(String data, Class<T> clazz) {
        return objectProcessor.toObject(binaryParser.toElement(data), clazz);
    }

    public static String stringifyBinary(ParsedElement element) {
        return binaryParser.toString(element);
    }
}