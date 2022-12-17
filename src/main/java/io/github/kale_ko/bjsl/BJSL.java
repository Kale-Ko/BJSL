package io.github.kale_ko.bjsl;

import java.io.IOException;
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

    public ParsedElement parse(String data) throws IOException {
        return this.parser.toElement(data);
    }

    public <T> T parse(String data, Class<T> clazz) throws IOException {
        return this.processor.toObject(this.parser.toElement(data), clazz);
    }

    public String stringify(ParsedElement element) throws IOException {
        return this.parser.toString(element);
    }

    public String stringify(Object object) throws IOException {
        return this.parser.toString(this.processor.toElement(object));
    }

    public static ParsedElement parseJson(String data) throws IOException {
        return jsonParser.toElement(data);
    }

    public static <T> T parseJson(String data, Class<T> clazz) throws IOException {
        return objectProcessor.toObject(jsonParser.toElement(data), clazz);
    }

    public static String stringifyJson(ParsedElement element) throws IOException {
        return jsonParser.toString(element);
    }

    public static String stringifyJson(Object object) throws IOException {
        return jsonParser.toString(objectProcessor.toElement(object));
    }

    public static ParsedElement parseYaml(String data) throws IOException {
        return yamlParser.toElement(data);
    }

    public static <T> T parseYaml(String data, Class<T> clazz) throws IOException {
        return objectProcessor.toObject(yamlParser.toElement(data), clazz);
    }

    public static String stringifyYaml(ParsedElement element) throws IOException {
        return yamlParser.toString(element);
    }

    public static String stringifyYaml(Object object) throws IOException {
        return yamlParser.toString(objectProcessor.toElement(object));
    }

    public static ParsedElement parseToml(String data) throws IOException {
        return tomlParser.toElement(data);
    }

    public static <T> T parseToml(String data, Class<T> clazz) throws IOException {
        return objectProcessor.toObject(tomlParser.toElement(data), clazz);
    }

    public static String stringifyToml(ParsedElement element) throws IOException {
        return tomlParser.toString(element);
    }

    public static String stringifyToml(Object object) throws IOException {
        return tomlParser.toString(objectProcessor.toElement(object));
    }

    public static ParsedElement parseProperties(String data) throws IOException {
        return propertiesParser.toElement(data);
    }

    public static <T> T parseProperties(String data, Class<T> clazz) throws IOException {
        return objectProcessor.toObject(propertiesParser.toElement(data), clazz);
    }

    public static String stringifyProperties(ParsedElement element) throws IOException {
        return propertiesParser.toString(element);
    }

    public static String stringifyProperties(Object object) throws IOException {
        return propertiesParser.toString(objectProcessor.toElement(object));
    }

    public static ParsedElement parseBinary(String data) throws IOException {
        return binaryParser.toElement(data);
    }

    public static <T> T parseBinary(String data, Class<T> clazz) throws IOException {
        return objectProcessor.toObject(binaryParser.toElement(data), clazz);
    }

    public static String stringifyBinary(ParsedElement element) throws IOException {
        return binaryParser.toString(element);
    }

    public static String stringifyBinary(Object object) throws IOException {
        return binaryParser.toString(objectProcessor.toElement(object));
    }

    public static Logger getLogger() {
        return BJSL.logger;
    }

    public static void setLogger(Logger value) {
        BJSL.logger = value;
    }
}