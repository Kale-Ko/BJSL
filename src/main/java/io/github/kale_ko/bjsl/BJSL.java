package io.github.kale_ko.bjsl;

import io.github.kale_ko.bjsl.parsers.BinaryParser;
import io.github.kale_ko.bjsl.parsers.JsonParser;
import io.github.kale_ko.bjsl.parsers.Parser;
import io.github.kale_ko.bjsl.parsers.PropertiesParser;
import io.github.kale_ko.bjsl.parsers.TomlParser;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;

public class BJSL {
    private static ObjectProcessor objectProcessor = new ObjectProcessor(false);

    private static JsonParser jsonParser = new JsonParser();
    private static YamlParser yamlParser = new YamlParser();
    private static TomlParser tomlParser = new TomlParser();
    private static PropertiesParser propertiesParser = new PropertiesParser();
    private static BinaryParser binaryParser = new BinaryParser();

    protected Parser parser;
    protected ObjectProcessor processor;

    public BJSL(Parser parser, ObjectProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    public <T> T parse(String data, Class<T> clazz) {
        return this.processor.toObject(this.parser.toElement(data), clazz);
    }

    public String stringify(Object object) {
        return this.parser.toString(this.processor.toElement(object));
    }

    public static <T> T parseJson(String data, Class<T> clazz) {
        return objectProcessor.toObject(jsonParser.toElement(data), clazz);
    }

    public static String stringifyJson(Object object) {
        return jsonParser.toString(objectProcessor.toElement(object));
    }

    public static <T> T parseYaml(String data, Class<T> clazz) {
        return objectProcessor.toObject(yamlParser.toElement(data), clazz);
    }

    public static String stringifyYaml(Object object) {
        return yamlParser.toString(objectProcessor.toElement(object));
    }

    public static <T> T parseToml(String data, Class<T> clazz) {
        return objectProcessor.toObject(tomlParser.toElement(data), clazz);
    }

    public static String stringifyToml(Object object) {
        return tomlParser.toString(objectProcessor.toElement(object));
    }

    public static <T> T parseProperties(String data, Class<T> clazz) {
        return objectProcessor.toObject(propertiesParser.toElement(data), clazz);
    }

    public static String stringifyProperties(Object object) {
        return propertiesParser.toString(objectProcessor.toElement(object));
    }

    public static <T> T parseBinary(String data, Class<T> clazz) {
        return objectProcessor.toObject(binaryParser.toElement(data), clazz);
    }

    public static String stringifyBinary(Object object) {
        return binaryParser.toString(objectProcessor.toElement(object));
    }
}