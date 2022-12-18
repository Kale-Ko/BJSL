package io.github.kale_ko.bjsl;

import java.util.logging.Logger;
import io.github.kale_ko.bjsl.elements.ParsedElement;
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

    private static Logger logger = Logger.getLogger("BJSL");

    protected Parser parser;
    protected ObjectProcessor processor;

    public BJSL(Parser parser, ObjectProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    public ParsedElement parse(String data) {
        return this.parser.toElement(data);
    }

    public <T> T parse(String data, Class<T> clazz) {
        return this.processor.toObject(this.parser.toElement(data), clazz);
    }

    public String stringify(ParsedElement element) {
        return this.parser.toString(element);
    }

    public String stringify(Object object) {
        return this.parser.toString(this.processor.toElement(object));
    }

    public static ParsedElement parseJson(String data) {
        return jsonParser.toElement(data);
    }

    public static <T> T parseJson(String data, Class<T> clazz) {
        return objectProcessor.toObject(jsonParser.toElement(data), clazz);
    }

    public static String stringifyJson(ParsedElement element) {
        return jsonParser.toString(element);
    }

    public static String stringifyJson(Object object) {
        return jsonParser.toString(objectProcessor.toElement(object));
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

    public static String stringifyYaml(Object object) {
        return yamlParser.toString(objectProcessor.toElement(object));
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

    public static String stringifyToml(Object object) {
        return tomlParser.toString(objectProcessor.toElement(object));
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

    public static String stringifyProperties(Object object) {
        return propertiesParser.toString(objectProcessor.toElement(object));
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

    public static String stringifyBinary(Object object) {
        return binaryParser.toString(objectProcessor.toElement(object));
    }

    public static Logger getLogger() {
        return BJSL.logger;
    }

    public static void setLogger(Logger value) {
        BJSL.logger = value;
    }
}