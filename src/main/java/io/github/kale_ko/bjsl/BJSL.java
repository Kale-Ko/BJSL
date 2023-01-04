package io.github.kale_ko.bjsl;

import java.util.logging.Logger;
import com.fasterxml.jackson.databind.JavaType;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.JsonParser;
import io.github.kale_ko.bjsl.parsers.Parser;
import io.github.kale_ko.bjsl.parsers.PropertiesParser;
import io.github.kale_ko.bjsl.parsers.SmileParser;
import io.github.kale_ko.bjsl.parsers.TomlParser;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;

public class BJSL {
    private static ObjectProcessor objectProcessor = new ObjectProcessor(false, false);

    private static JsonParser jsonParser = new JsonParser(false);
    private static JsonParser prettyJsonParser = new JsonParser(true, 2);
    private static YamlParser yamlParser = new YamlParser();
    private static TomlParser tomlParser = new TomlParser();
    private static PropertiesParser propertiesParser = new PropertiesParser();
    private static SmileParser smileParser = new SmileParser();

    private static Logger logger = Logger.getLogger("BJSL");

    protected Parser parser;
    protected ObjectProcessor processor;

    public BJSL(Parser parser) {
        this(parser, new ObjectProcessor());
    }

    public BJSL(Parser parser, ObjectProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    public ParsedElement parse(String data) {
        return this.parser.toElement(data);
    }

    public <T> T parse(String data, Class<T> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    public Object parse(String data, JavaType type) {
        return this.processor.toObject(this.parse(data), type);
    }

    public String stringify(ParsedElement element) {
        return this.parser.toString(element);
    }

    public String stringify(Object object) {
        return this.stringify(this.processor.toElement(object));
    }

    public static ParsedElement parseJson(String data) {
        return jsonParser.toElement(data);
    }

    public static <T> T parseJson(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    public static Object parseJson(String data, JavaType type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    public static String stringifyJson(ParsedElement element) {
        return stringifyJson(element, true);
    }

    public static String stringifyJson(ParsedElement element, Boolean pretty) {
        if (pretty) {
            return prettyJsonParser.toString(element);
        } else {
            return jsonParser.toString(element);
        }
    }

    public static String stringifyJson(Object object) {
        return stringifyJson(object, true);
    }

    public static String stringifyJson(Object object, Boolean pretty) {
        return stringifyJson(objectProcessor.toElement(object), pretty);
    }

    public static ParsedElement parseYaml(String data) {
        return yamlParser.toElement(data);
    }

    public static <T> T parseYaml(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    public static Object parseYaml(String data, JavaType type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    public static String stringifyYaml(ParsedElement element) {
        return yamlParser.toString(element);
    }

    public static String stringifyYaml(Object object) {
        return stringifyYaml(objectProcessor.toElement(object));
    }

    public static ParsedElement parseToml(String data) {
        return tomlParser.toElement(data);
    }

    public static <T> T parseToml(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    public static Object parseToml(String data, JavaType type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    public static String stringifyToml(ParsedElement element) {
        return tomlParser.toString(element);
    }

    public static String stringifyToml(Object object) {
        return stringifyToml(objectProcessor.toElement(object));
    }

    public static ParsedElement parseProperties(String data) {
        return propertiesParser.toElement(data);
    }

    public static <T> T parseProperties(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    public static Object parseProperties(String data, JavaType type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    public static String stringifyProperties(ParsedElement element) {
        return propertiesParser.toString(element);
    }

    public static String stringifyProperties(Object object) {
        return stringifyProperties(objectProcessor.toElement(object));
    }

    public static ParsedElement parseSmile(String data) {
        return smileParser.toElement(data);
    }

    public static <T> T parseSmile(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseSmile(data), clazz);
    }

    public static Object parseSmile(String data, JavaType type) {
        return objectProcessor.toObject(parseSmile(data), type);
    }

    public static String stringifySmile(ParsedElement element) {
        return smileParser.toString(element);
    }

    public static String stringifySmile(Object object) {
        return stringifySmile(objectProcessor.toElement(object));
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger value) {
        logger = value;
    }
}