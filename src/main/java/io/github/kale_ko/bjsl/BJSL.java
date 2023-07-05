package io.github.kale_ko.bjsl;

import com.fasterxml.jackson.databind.JavaType;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.*;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;
import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * The main class for interfacing with BJSL
 * <p>
 * Static calls use default instances of each parser and processor
 *
 * @param <T> The parser to use
 *
 * @version 1.0.0
 * @since 1.1.0
 */
public class BJSL<T extends Parser<?, ?>> {
    /**
     * The JSON parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final JsonParser jsonParser = new JsonParser.Builder().setPrettyPrint(false).build();

    /**
     * The pretty JSON parser used by static calls
     * <p>
     * Just uses a default builder with pretty printing
     *
     * @since 1.0.0
     */
    private static final JsonParser prettyJsonParser = new JsonParser.Builder().setPrettyPrint(true).build();

    /**
     * The YAML parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final YamlParser yamlParser = new YamlParser.Builder().build();

    /**
     * The TOML parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final TomlParser tomlParser = new TomlParser.Builder().build();

    /**
     * The XML parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.4.0
     */
    private static final XmlParser xmlParser = new XmlParser.Builder().setPrettyPrint(false).build();

    /**
     * The pretty XML parser used by static calls
     * <p>
     * Just uses a default builder with pretty printing
     *
     * @since 1.4.0
     */
    private static final XmlParser prettyXmlParser = new XmlParser.Builder().setPrettyPrint(true).build();

    /**
     * The csv parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final CsvParser csvParser = new CsvParser.Builder().build();

    /**
     * The java properties parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final PropertiesParser propertiesParser = new PropertiesParser.Builder().build();

    /**
     * The Smile parser used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final SmileParser smileParser = new SmileParser.Builder().build();

    /**
     * The object processor used by static calls
     * <p>
     * Just uses a default builder
     *
     * @since 1.0.0
     */
    private static final ObjectProcessor objectProcessor = new ObjectProcessor.Builder().build();

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
    protected T parser;

    /**
     * The object processor to use
     *
     * @since 1.0.0
     */
    protected ObjectProcessor processor;

    /**
     * Create a new instance of BJSL
     *
     * @param parser The parser to use
     *
     * @since 1.0.0
     */
    public BJSL(T parser) {
        this(parser, new ObjectProcessor.Builder().build());
    }

    /**
     * Create a new instance of BJSL
     *
     * @param parser    The parser to use
     * @param processor The object processor to use
     *
     * @since 1.0.0
     */
    public BJSL(T parser, ObjectProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    /**
     * Get the parser being used
     *
     * @return The parser being used
     *
     * @since 1.0.0
     */
    public T getParser() {
        return this.parser;
    }

    /**
     * Get the object processor being used
     *
     * @return The object processor being used
     *
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
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
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
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
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
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public <V> V parse(String data, Class<V> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public <V> V parse(byte[] data, Class<V> clazz) {
        return this.processor.toObject(this.parse(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public byte[] byteify(Object object) {
        return this.byteify(this.processor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public String emptyString() {
        return this.parser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public String emptyArrayString() {
        return this.parser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public byte[] emptyBytes() {
        return this.parser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public byte[] emptyArrayBytes() {
        return this.parser.emptyArrayBytes();
    }

    /**
     * Parses and maps this element into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)}
     *
     * @param element The element to map
     * @param clazz   The object type to map to
     * @param <V>     The object type to map to
     *
     * @return A new Object of the passed type with the values of element
     *
     * @since 1.0.0
     */
    public static <V> V parse(ParsedElement element, Class<V> clazz) {
        return objectProcessor.toObject(element, clazz);
    }

    /**
     * Parses and maps this element into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)}
     *
     * @param element The element to map
     * @param type    The object type to map to
     *
     * @return A new Object of the passed type with the values of element
     *
     * @since 1.0.0
     */
    public static Object parse(ParsedElement element, Type type) {
        return objectProcessor.toObject(element, type);
    }

    /**
     * Parses and maps this element into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)}
     *
     * @param element The element to map
     * @param type    The object type to map to
     *
     * @return A new Object of the passed type with the values of element
     *
     * @since 1.0.0
     */
    public static Object parse(ParsedElement element, JavaType type) {
        return objectProcessor.toObject(element, type);
    }

    /**
     * Serializes this object into a {@link ParsedElement}
     * <p>
     * Calls {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     *
     * @return The object passed serialized to a {@link ParsedElement}
     *
     * @since 1.0.0
     */
    public static ParsedElement elementify(Object object) {
        return objectProcessor.toElement(object);
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
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
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
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
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseJson(String data, Class<V> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseJson(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseJson(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
     * @since 1.0.0
     */
    public static String stringifyJson(ParsedElement element) {
        return stringifyJson(element, false);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element The element to serialize
     * @param pretty  Weather to pretty print the data
     *
     * @return The element passed serialized to a String
     *
     * @since 1.0.0
     */
    public static String stringifyJson(ParsedElement element, boolean pretty) {
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
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
     * @param element The element to serialize
     * @param pretty  Weather to pretty print the data
     *
     * @return The element passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyJson(ParsedElement element, boolean pretty) {
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
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
     * @param object The object to serialize
     * @param pretty Weather to pretty print the data
     *
     * @return The object passed serialized to a String
     *
     * @since 1.0.0
     */
    public static String stringifyJson(Object object, boolean pretty) {
        return stringifyJson(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
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
     * @param object The object to serialize
     * @param pretty Weather to pretty print the data
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyJson(Object object, boolean pretty) {
        return byteifyJson(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static String emptyJsonString() {
        return jsonParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static String emptyJsonArrayString() {
        return jsonParser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptyJsonBytes() {
        return jsonParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptyJsonArrayBytes() {
        return jsonParser.emptyArrayBytes();
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
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
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
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
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseYaml(String data, Class<V> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseYaml(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseYaml(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyYaml(Object object) {
        return byteifyYaml(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static String emptyYamlString() {
        return yamlParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static String emptyYamlArrayString() {
        return yamlParser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptyYamlBytes() {
        return yamlParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptyYamlArrayBytes() {
        return yamlParser.emptyArrayBytes();
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
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
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
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
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseToml(String data, Class<V> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseToml(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseToml(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyToml(Object object) {
        return byteifyToml(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static String emptyTomlString() {
        return tomlParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static String emptyTomlArrayString() {
        return tomlParser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptyTomlBytes() {
        return tomlParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptyTomlArrayBytes() {
        return tomlParser.emptyArrayBytes();
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
     * @since 1.4.0
     */
    public static ParsedElement parseXml(String data) {
        return xmlParser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
     * @since 1.4.0
     */
    public static ParsedElement parseXml(byte[] data) {
        return xmlParser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.4.0
     */
    public static <V> V parseXml(String data, Class<V> clazz) {
        return objectProcessor.toObject(parseXml(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.4.0
     */
    public static <V> V parseXml(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseXml(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.4.0
     */
    public static Object parseXml(String data, Type type) {
        return objectProcessor.toObject(parseXml(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.4.0
     */
    public static Object parseXml(byte[] data, Type type) {
        return objectProcessor.toObject(parseXml(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.4.0
     */
    public static Object parseXml(String data, JavaType type) {
        return objectProcessor.toObject(parseXml(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.4.0
     */
    public static Object parseXml(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseXml(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
     * @since 1.4.0
     */
    public static String stringifyXml(ParsedElement element) {
        return stringifyXml(element, false);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element The element to serialize
     * @param pretty  Weather to pretty print the data
     *
     * @return The element passed serialized to a String
     *
     * @since 1.4.0
     */
    public static String stringifyXml(ParsedElement element, boolean pretty) {
        if (pretty) {
            return prettyXmlParser.toString(element);
        } else {
            return xmlParser.toString(element);
        }
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
     * @since 1.4.0
     */
    public static byte[] byteifyXml(ParsedElement element) {
        return byteifyXml(element, true);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element The element to serialize
     * @param pretty  Weather to pretty print the data
     *
     * @return The element passed serialized to bytes
     *
     * @since 1.4.0
     */
    public static byte[] byteifyXml(ParsedElement element, boolean pretty) {
        if (pretty) {
            return prettyXmlParser.toBytes(element);
        } else {
            return xmlParser.toBytes(element);
        }
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
     * @since 1.4.0
     */
    public static String stringifyXml(Object object) {
        return stringifyXml(object, true);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     * @param pretty Weather to pretty print the data
     *
     * @return The object passed serialized to a String
     *
     * @since 1.4.0
     */
    public static String stringifyXml(Object object, boolean pretty) {
        return stringifyXml(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.4.0
     */
    public static byte[] byteifyXml(Object object) {
        return byteifyXml(object, true);
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     * @param pretty Weather to pretty print the data
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.4.0
     */
    public static byte[] byteifyXml(Object object, boolean pretty) {
        return byteifyXml(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static String emptyXmlString() {
        return xmlParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static String emptyXmlArrayString() {
        return xmlParser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptyXmlBytes() {
        return xmlParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptyXmlArrayBytes() {
        return xmlParser.emptyArrayBytes();
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
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
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
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
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseProperties(String data, Class<V> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseProperties(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseProperties(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyProperties(Object object) {
        return byteifyProperties(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static String emptyPropertiesString() {
        return propertiesParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static String emptyPropertiesArrayString() {
        return propertiesParser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptyPropertiesBytes() {
        return propertiesParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptyPropertiesArrayBytes() {
        return propertiesParser.emptyArrayBytes();
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(String)}
     *
     * @param data The string to parse
     *
     * @return The string passed parsed to a {@link ParsedElement}
     *
     * @since 1.0.0
     */
    public static ParsedElement parseCsv(String data) {
        return csvParser.toElement(data);
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
     * @since 1.0.0
     */
    public static ParsedElement parseCsv(byte[] data) {
        return csvParser.toElement(data);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(String)}
     *
     * @param data  The string to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseCsv(String data, Class<V> clazz) {
        return objectProcessor.toObject(parseCsv(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Class)} on {@link Parser#toElement(byte[])}
     *
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseCsv(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseCsv(data), clazz);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static Object parseCsv(String data, Type type) {
        return objectProcessor.toObject(parseCsv(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static Object parseCsv(byte[] data, Type type) {
        return objectProcessor.toObject(parseCsv(data), type);
    }

    /**
     * Parses and maps this string into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(String)}
     *
     * @param data The string to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static Object parseCsv(String data, JavaType type) {
        return objectProcessor.toObject(parseCsv(data), type);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, JavaType)} on {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static Object parseCsv(byte[] data, JavaType type) {
        return objectProcessor.toObject(parseCsv(data), type);
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)}
     *
     * @param element The element to serialize
     *
     * @return The element passed serialized to a String
     *
     * @since 1.0.0
     */
    public static String stringifyCsv(ParsedElement element) {
        return csvParser.toString(element);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)}
     *
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyCsv(ParsedElement element) {
        return csvParser.toBytes(element);
    }

    /**
     * Serializes this object into a String
     * <p>
     * Calls {@link Parser#toString(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     *
     * @return The object passed serialized to a String
     *
     * @since 1.0.0
     */
    public static String stringifyCsv(Object object) {
        return stringifyCsv(objectProcessor.toElement(object));
    }

    /**
     * Serializes this object into bytes
     * <p>
     * Calls {@link Parser#toBytes(ParsedElement)} on {@link ObjectProcessor#toElement(Object)}
     *
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifyCsv(Object object) {
        return byteifyCsv(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static String emptyCsvString() {
        return csvParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static String emptyCsvArrayString() {
        return csvParser.emptyArrayString();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptyCsvBytes() {
        return csvParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptyCsvArrayBytes() {
        return csvParser.emptyArrayBytes();
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to parse
     *
     * @return The bytes passed parsed to a {@link ParsedElement}
     *
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
     * @param data  The bytes to map
     * @param clazz The object type to map to
     * @param <V>   The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
     * @since 1.0.0
     */
    public static <V> V parseSmile(byte[] data, Class<V> clazz) {
        return objectProcessor.toObject(parseSmile(data), clazz);
    }

    /**
     * Parses and maps these bytes into an Object
     * <p>
     * Calls {@link ObjectProcessor#toObject(ParsedElement, Type)} on {@link Parser#toElement(byte[])}
     *
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param data The bytes to map
     * @param type The object type to map to
     *
     * @return A new Object of the passed type with the values of data
     *
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
     * @param element The element to serialize
     *
     * @return The element passed serialized to bytes
     *
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
     * @param object The object to serialize
     *
     * @return The object passed serialized to bytes
     *
     * @since 1.0.0
     */
    public static byte[] byteifySmile(Object object) {
        return byteifySmile(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public static byte[] emptySmileBytes() {
        return smileParser.emptyBytes();
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public static byte[] emptySmileArrayBytes() {
        return smileParser.emptyArrayBytes();
    }

    /**
     * Get the main logger used by BJSL
     *
     * @return The main logger used by BJSL
     *
     * @since 1.0.0
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Set the main logger to be used by BJSL
     *
     * @param value The new logger
     *
     * @since 1.0.0
     */
    public static void setLogger(Logger value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        logger = value;
    }
}