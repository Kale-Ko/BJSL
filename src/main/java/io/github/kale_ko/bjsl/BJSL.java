package io.github.kale_ko.bjsl;

import java.lang.reflect.Type;
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

/**
 * The main class for interfacing with BJSL
 * <p>
 * Static calls use default instances of each parser and processor
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class BJSL {
    /**
     * The JSON parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static JsonParser jsonParser = new JsonParser.Builder().setPrettyPrint(false).build();

    /**
     * The pretty JSON parser used by static calls
     * <p>
     * Just uses a default builder with pretty printing
     *
     * @since 1.0.0
     */
    private static JsonParser prettyJsonParser = new JsonParser.Builder().setPrettyPrint(true).build();

    /**
     * The YAML parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static YamlParser yamlParser = new YamlParser.Builder().build();

    /**
     * The TOML parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static TomlParser tomlParser = new TomlParser.Builder().build();

    /**
     * The java properties parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static PropertiesParser propertiesParser = new PropertiesParser.Builder().build();

    /**
     * The Smile parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static SmileParser smileParser = new SmileParser.Builder().build();

    /**
     * The object processor used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static ObjectProcessor objectProcessor = new ObjectProcessor.Builder().build();

    /**
     * The main logger used by BJSL
     * <p>
     * Uses the default format set
     *
     * @since 1.0.0
     */
    private static Logger logger = Logger.getLogger("BJSL");

    /**
     * The parser to use
     *
     * @since 1.0.0
     */
    protected Parser parser;

    /**
     * The object processor to use
     *
     * @since 1.0.0
     */
    protected ObjectProcessor processor;

    /**
     * Create a new instance of BJSL
     *
     * @param parser
     *        The parser to use
     * @since 1.0.0
     */
    public BJSL(Parser parser) {
        this(parser, new ObjectProcessor.Builder().build());
    }

    /**
     * Create a new instance of BJSL
     *
     * @param parser
     *        The parser to use
     * @param processor
     *        The object processor to use
     * @since 1.0.0
     */
    public BJSL(Parser parser, ObjectProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    /**
     * Get the parser being used
     *
     * @return The parser being used
     * @since 1.0.0
     */
    public Parser getParser() {
        return this.parser;
    }

    /**
     * Get the object processor being used
     *
     * @return The object processor being used
     * @since 1.0.0
     */
    public ObjectProcessor getProcessor() {
        return this.processor;
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to parse
     * @return The string passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public ParsedElement parse(String data) {
        return this.parser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public ParsedElement parse(byte[] data) {
        return this.parser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public <T> T parse(String data, Class<T> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public <T> T parse(byte[] data, Class<T> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public Object parse(String data, Type type) {
        return this.processor.toObject(this.parse(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public Object parse(byte[] data, Type type) {
        return this.processor.toObject(this.parse(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public Object parse(String data, JavaType type) {
        return this.processor.toObject(this.parse(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public Object parse(byte[] data, JavaType type) {
        return this.processor.toObject(this.parse(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public String stringify(ParsedElement element) {
        return this.parser.toString(element);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public byte[] byteify(ParsedElement element) {
        return this.parser.toBytes(element);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to a String
     * @since 1.0.0
     */
    public String stringify(Object object) {
        return this.stringify(this.processor.toElement(object));
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public byte[] byteify(Object object) {
        return this.byteify(this.processor.toElement(object));
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to parse
     * @return The string passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseJson(String data) {
        return jsonParser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseJson(byte[] data) {
        return jsonParser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseJson(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseJson(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseJson(String data, Type type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseJson(byte[] data, Type type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseJson(String data, JavaType type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseJson(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseJson(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyJson(ParsedElement element) {
        return stringifyJson(element, true);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @param pretty
     *        Weather to pretty print the data
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyJson(ParsedElement element, Boolean pretty) {
        if (pretty) {
            return prettyJsonParser.toString(element);
        } else {
            return jsonParser.toString(element);
        }
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyJson(ParsedElement element) {
        return byteifyJson(element, true);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @param pretty
     *        Weather to pretty print the data
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyJson(ParsedElement element, Boolean pretty) {
        if (pretty) {
            return prettyJsonParser.toBytes(element);
        } else {
            return jsonParser.toBytes(element);
        }
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyJson(Object object) {
        return stringifyJson(object, true);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @param pretty
     *        Weather to pretty print the data
     * @return The object passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyJson(Object object, Boolean pretty) {
        return stringifyJson(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyJson(Object object) {
        return byteifyJson(object, true);
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @param pretty
     *        Weather to pretty print the data
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyJson(Object object, Boolean pretty) {
        return byteifyJson(objectProcessor.toElement(object), pretty);
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to parse
     * @return The string passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseYaml(String data) {
        return yamlParser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseYaml(byte[] data) {
        return yamlParser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseYaml(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseYaml(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseYaml(String data, Type type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseYaml(byte[] data, Type type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseYaml(String data, JavaType type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseYaml(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseYaml(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyYaml(ParsedElement element) {
        return yamlParser.toString(element);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyYaml(ParsedElement element) {
        return yamlParser.toBytes(element);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyYaml(Object object) {
        return stringifyYaml(objectProcessor.toElement(object));
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyYaml(Object object) {
        return byteifyYaml(objectProcessor.toElement(object));
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to parse
     * @return The string passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseToml(String data) {
        return tomlParser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseToml(byte[] data) {
        return tomlParser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseToml(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseToml(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseToml(String data, Type type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseToml(byte[] data, Type type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseToml(String data, JavaType type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseToml(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseToml(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyToml(ParsedElement element) {
        return tomlParser.toString(element);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyToml(ParsedElement element) {
        return tomlParser.toBytes(element);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyToml(Object object) {
        return stringifyToml(objectProcessor.toElement(object));
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyToml(Object object) {
        return byteifyToml(objectProcessor.toElement(object));
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to parse
     * @return The string passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseProperties(String data) {
        return propertiesParser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseProperties(byte[] data) {
        return propertiesParser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseProperties(String data, Class<T> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseProperties(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseProperties(String data, Type type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseProperties(byte[] data, Type type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data
     *        The string to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseProperties(String data, JavaType type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseProperties(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseProperties(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyProperties(ParsedElement element) {
        return propertiesParser.toString(element);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyProperties(ParsedElement element) {
        return propertiesParser.toBytes(element);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to a String
     * @since 1.0.0
     */
    public static String stringifyProperties(Object object) {
        return stringifyProperties(objectProcessor.toElement(object));
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifyProperties(Object object) {
        return byteifyProperties(objectProcessor.toElement(object));
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public static ParsedElement parseSmile(byte[] data) {
        return smileParser.toElement(data);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of data
     * @since 1.0.0
     */
    public static <T> T parseSmile(byte[] data, Class<T> clazz) {
        return objectProcessor.toObject(parseSmile(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseSmile(byte[] data, Type type) {
        return objectProcessor.toObject(parseSmile(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data
     *        The bytes to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of data
     * @since 1.0.0
     */
    public static Object parseSmile(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseSmile(data), type);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifySmile(ParsedElement element) {
        return smileParser.toBytes(element);
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object
     *        The object to serialize
     * @return The object passed serialized to bytes
     * @since 1.0.0
     */
    public static byte[] byteifySmile(Object object) {
        return byteifySmile(objectProcessor.toElement(object));
    }

    /**
     * Get the main logger used by BJSL
     *
     * @return The main logger used by BJSL
     * @since 1.0.0
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Set the main logger to be used by BJSL
     *
     * @param value
     *        The new logger
     * @since 1.0.0
     */
    public static void setLogger(Logger value) {
        logger = value;
    }
}