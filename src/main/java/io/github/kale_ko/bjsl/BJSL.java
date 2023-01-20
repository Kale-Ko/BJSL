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
    private static JsonParser jsonParser = new JsonParser.Builder().setPrettyPrint(false).build();
    private static JsonParser prettyJsonParser = new JsonParser.Builder().setPrettyPrint(true).build();
    private static YamlParser yamlParser = new YamlParser.Builder().build();
    private static TomlParser tomlParser = new TomlParser.Builder().build();
    private static PropertiesParser propertiesParser = new PropertiesParser.Builder().build();
    private static SmileParser smileParser = new SmileParser.Builder().build();

    private static ObjectProcessor objectProcessor = new ObjectProcessor.Builder().build();

    private static Logger logger = Logger.getLogger("BJSL");

    protected Parser parser;
    protected ObjectProcessor processor;

    public BJSL(Parser parser) {
        this(parser, new ObjectProcessor.Builder().build());
    }

    public BJSL(Parser parser, ObjectProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    public Parser getParser() {
        return this.parser;
    }

    public ObjectProcessor getProcessor() {
        return this.processor;
    }

    public ParsedElement parse(String data) {
        return this.parser.toElement(data);
    }

    public ParsedElement parse(byte[] data) {
        return this.parser.toElement(data);
    }

    public <T> T parse(String data, Class<T> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    public <T> T parse(byte[] data, Class<T> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    public Object parse(String data, JavaType type) {
        return this.processor.toObject(this.parse(data), type);
    }

    public Object parse(byte[] data, JavaType type) {
        return this.processor.toObject(this.parse(data), type);
    }

    public String stringify(ParsedElement element) {
        return this.parser.toString(element);
    }

    public byte[] byteify(ParsedElement element) {
        return this.parser.toBytes(element);
    }

    public String stringify(Object object) {
        return this.stringify(this.processor.toElement(object));
    }

    public byte[] byteify(Object object) {
        return this.byteify(this.processor.toElement(object));
    }

    public static ParsedElement parseJson(String data) {
        return jsonParser.toElement(data);
    }

    public static ParsedElement parseJson(byte[] data) {
        return jsonParser.toElement(data);
    }

    public static <T> T parseJson(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    public static <T> T parseJson(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    public static Object parseJson(String data, JavaType type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    public static Object parseJson(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    public static String stringifyJson(ParsedElement element) {
        return stringifyJson(element, true);
    }

    public static byte[] byteifyJson(ParsedElement element) {
        return byteifyJson(element, true);
    }

    public static String stringifyJson(ParsedElement element, Boolean pretty) {
        if (pretty) {
            return prettyJsonParser.toString(element);
        } else {
            return jsonParser.toString(element);
        }
    }

    public static byte[] byteifyJson(ParsedElement element, Boolean pretty) {
        if (pretty) {
            return prettyJsonParser.toBytes(element);
        } else {
            return jsonParser.toBytes(element);
        }
    }

    public static String stringifyJson(Object object) {
        return stringifyJson(object, true);
    }

    public static byte[] byteifyJson(Object object) {
        return byteifyJson(object, true);
    }

    public static String stringifyJson(Object object, Boolean pretty) {
        return stringifyJson(objectProcessor.toElement(object), pretty);
    }

    public static byte[] byteifyJson(Object object, Boolean pretty) {
        return byteifyYaml(objectProcessor.toElement(object), pretty);
    }

    public static ParsedElement parseYaml(String data) {
        return yamlParser.toElement(data);
    }

    public static ParsedElement parseYaml(byte[] data) {
        return yamlParser.toElement(data);
    }

    public static <T> T parseYaml(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    public static <T> T parseYaml(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    public static Object parseYaml(String data, JavaType type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    public static Object parseYaml(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    public static String stringifyYaml(ParsedElement element) {
        return stringifyYaml(element, true);
    }

    public static byte[] byteifyYaml(ParsedElement element) {
        return byteifyYaml(element, true);
    }

    public static String stringifyYaml(ParsedElement element, Boolean pretty) {
        return yamlParser.toString(element);
    }

    public static byte[] byteifyYaml(ParsedElement element, Boolean pretty) {
        return yamlParser.toBytes(element);
    }

    public static String stringifyYaml(Object object) {
        return stringifyYaml(object, true);
    }

    public static byte[] byteifyYaml(Object object) {
        return byteifyYaml(object, true);
    }

    public static String stringifyYaml(Object object, Boolean pretty) {
        return stringifyYaml(objectProcessor.toElement(object), pretty);
    }

    public static byte[] byteifyYaml(Object object, Boolean pretty) {
        return byteifyYaml(objectProcessor.toElement(object), pretty);
    }

    public static ParsedElement parseToml(String data) {
        return tomlParser.toElement(data);
    }

    public static ParsedElement parseToml(byte[] data) {
        return tomlParser.toElement(data);
    }

    public static <T> T parseToml(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    public static <T> T parseToml(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    public static Object parseToml(String data, JavaType type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    public static Object parseToml(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    public static String stringifyToml(ParsedElement element) {
        return stringifyToml(element, true);
    }

    public static byte[] byteifyToml(ParsedElement element) {
        return byteifyToml(element, true);
    }

    public static String stringifyToml(ParsedElement element, Boolean pretty) {
        return tomlParser.toString(element);
    }

    public static byte[] byteifyToml(ParsedElement element, Boolean pretty) {
        return tomlParser.toBytes(element);
    }

    public static String stringifyToml(Object object) {
        return stringifyToml(object, true);
    }

    public static byte[] byteifyToml(Object object) {
        return byteifyToml(object, true);
    }

    public static String stringifyToml(Object object, Boolean pretty) {
        return stringifyToml(objectProcessor.toElement(object), pretty);
    }

    public static byte[] byteifyToml(Object object, Boolean pretty) {
        return byteifyToml(objectProcessor.toElement(object), pretty);
    }

    public static ParsedElement parseProperties(String data) {
        return propertiesParser.toElement(data);
    }

    public static ParsedElement parseProperties(byte[] data) {
        return propertiesParser.toElement(data);
    }

    public static <T> T parseProperties(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    public static <T> T parseProperties(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    public static Object parseProperties(String data, JavaType type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    public static Object parseProperties(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    public static String stringifyProperties(ParsedElement element) {
        return stringifyProperties(element, true);
    }

    public static byte[] byteifyProperties(ParsedElement element) {
        return byteifyProperties(element, true);
    }

    public static String stringifyProperties(ParsedElement element, Boolean pretty) {
        return propertiesParser.toString(element);
    }

    public static byte[] byteifyProperties(ParsedElement element, Boolean pretty) {
        return propertiesParser.toBytes(element);
    }

    public static String stringifyProperties(Object object) {
        return stringifyProperties(object, true);
    }

    public static byte[] byteifyProperties(Object object) {
        return byteifyProperties(object, true);
    }

    public static String stringifyProperties(Object object, Boolean pretty) {
        return stringifyProperties(objectProcessor.toElement(object), pretty);
    }

    public static byte[] byteifyProperties(Object object, Boolean pretty) {
        return byteifyProperties(objectProcessor.toElement(object), pretty);
    }

    public static ParsedElement parseSmile(byte[] data) {
        return smileParser.toElement(data);
    }

    public static <T> T parseSmile(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseSmile(data), clazz);
    }

    public static Object parseSmile(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseSmile(data), type);
    }

    public static byte[] byteifySmile(ParsedElement element) {
        return smileParser.toBytes(element);
    }

    public static byte[] byteifySmile(Object object) {
        return byteifySmile(objectProcessor.toElement(object));
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger value) {
        logger = value;
    }
}