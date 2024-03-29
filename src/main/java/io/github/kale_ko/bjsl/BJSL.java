package io.github.kale_ko.bjsl;

import com.fasterxml.jackson.databind.JavaType;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.*;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;
import java.lang.reflect.Type;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull JsonParser jsonParser = new JsonParser.Builder().setPrettyPrint(false).build();

    /**
     * The pretty JSON parser used by static calls
     * <p>
     * Just uses a default instance with pretty printing
     *
     * @since 1.0.0
     */
    private static final @NotNull JsonParser prettyJsonParser = new JsonParser.Builder().setPrettyPrint(true).build();

    /**
     * The YAML parser used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull YamlParser yamlParser = new YamlParser.Builder().build();

    /**
     * The TOML parser used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull TomlParser tomlParser = new TomlParser.Builder().build();

    /**
     * The XML parser used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.4.0
     */
    private static final @NotNull XmlParser xmlParser = new XmlParser.Builder().setPrettyPrint(false).build();

    /**
     * The pretty XML parser used by static calls
     * <p>
     * Just uses a default instance with pretty printing
     *
     * @since 1.4.0
     */
    private static final @NotNull XmlParser prettyXmlParser = new XmlParser.Builder().setPrettyPrint(true).build();

    /**
     * The csv parser used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull CsvParser csvParser = new CsvParser.Builder().build();

    /**
     * The java properties parser used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull PropertiesParser propertiesParser = new PropertiesParser.Builder().build();

    /**
     * The Smile parser used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull SmileParser smileParser = new SmileParser.Builder().build();

    /**
     * The object processor used by static calls
     * <p>
     * Just uses a default instance
     *
     * @since 1.0.0
     */
    private static final @NotNull ObjectProcessor objectProcessor = new ObjectProcessor.Builder().build();

    /**
     * The main logger used by BJSL
     * <p>
     * Uses the default format set
     *
     * @since 1.0.0
     */
    private static @Nullable Logger logger = Logger.getLogger("BJSL");

    /**
     * The parser to use for method calls
     *
     * @since 1.0.0
     */
    protected final @NotNull T parser;

    /**
     * The object processor to use for method calls
     *
     * @since 1.0.0
     */
    protected final @NotNull ObjectProcessor processor;

    /**
     * Create a new instance of BJSL
     *
     * @param parser The parser to use for method calls
     *
     * @since 1.0.0
     */
    public BJSL(@NotNull T parser) {
        this(parser, new ObjectProcessor.Builder().build());
    }

    /**
     * Create a new instance of BJSL
     *
     * @param parser    The parser to use for method calls
     * @param processor The object processor to use for method calls
     *
     * @since 1.0.0
     */
    public BJSL(@NotNull T parser, @NotNull ObjectProcessor processor) {
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
    public @NotNull T getParser() {
        return this.parser;
    }

    /**
     * Get the object processor being used
     *
     * @return The object processor being used
     *
     * @since 1.0.0
     */
    public @NotNull ObjectProcessor getProcessor() {
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
    public @NotNull ParsedElement parse(@NotNull String data) {
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
    public @NotNull ParsedElement parse(byte @NotNull [] data) {
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
    public <V> @Nullable V parse(@NotNull String data, @NotNull Class<V> clazz) {
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
    public <V> @Nullable V parse(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public @Nullable Object parse(@NotNull String data, @NotNull Type type) {
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
    public @Nullable Object parse(byte @NotNull [] data, @NotNull Type type) {
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
    public @Nullable Object parse(@NotNull String data, @NotNull JavaType type) {
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
    public @Nullable Object parse(byte @NotNull [] data, @NotNull JavaType type) {
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
    public @NotNull String stringify(@NotNull ParsedElement element) {
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
    public byte[] byteify(@NotNull ParsedElement element) {
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
    public @NotNull String stringify(@Nullable Object object) {
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
    public byte[] byteify(@Nullable Object object) {
        return this.byteify(this.processor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public @NotNull String emptyString() {
        return this.parser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public @NotNull String emptyArrayString() {
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
    public static <V> @Nullable V parse(@NotNull ParsedElement element, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parse(@NotNull ParsedElement element, @NotNull Type type) {
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
    public static @Nullable Object parse(@NotNull ParsedElement element, @NotNull JavaType type) {
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
    public static @NotNull ParsedElement elementify(@Nullable Object object) {
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
    public static @NotNull ParsedElement parseJson(@NotNull String data) {
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
    public static @NotNull ParsedElement parseJson(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseJson(@NotNull String data, @NotNull Class<V> clazz) {
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
    public static <V> @Nullable V parseJson(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseJson(@NotNull String data, @NotNull Type type) {
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
    public static @Nullable Object parseJson(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseJson(@NotNull String data, @NotNull JavaType type) {
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
    public static @Nullable Object parseJson(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static @NotNull String stringifyJson(@NotNull ParsedElement element) {
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
    public static @NotNull String stringifyJson(@NotNull ParsedElement element, boolean pretty) {
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
    public static byte[] byteifyJson(@NotNull ParsedElement element) {
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
    public static byte[] byteifyJson(@NotNull ParsedElement element, boolean pretty) {
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
    public static @NotNull String stringifyJson(@Nullable Object object) {
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
    public static @NotNull String stringifyJson(@Nullable Object object, boolean pretty) {
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
    public static byte[] byteifyJson(@Nullable Object object) {
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
    public static byte[] byteifyJson(@Nullable Object object, boolean pretty) {
        return byteifyJson(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static @NotNull String emptyJsonString() {
        return jsonParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static @NotNull String emptyJsonArrayString() {
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
    public static @NotNull ParsedElement parseYaml(@NotNull String data) {
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
    public static @NotNull ParsedElement parseYaml(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseYaml(@NotNull String data, @NotNull Class<V> clazz) {
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
    public static <V> @Nullable V parseYaml(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseYaml(@NotNull String data, @NotNull Type type) {
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
    public static @Nullable Object parseYaml(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseYaml(@NotNull String data, @NotNull JavaType type) {
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
    public static @Nullable Object parseYaml(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static @NotNull String stringifyYaml(@NotNull ParsedElement element) {
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
    public static byte[] byteifyYaml(@NotNull ParsedElement element) {
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
    public static @NotNull String stringifyYaml(@Nullable Object object) {
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
    public static byte[] byteifyYaml(@Nullable Object object) {
        return byteifyYaml(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static @NotNull String emptyYamlString() {
        return yamlParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static @NotNull String emptyYamlArrayString() {
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
    public static @NotNull ParsedElement parseToml(@NotNull String data) {
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
    public static @NotNull ParsedElement parseToml(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseToml(@NotNull String data, @NotNull Class<V> clazz) {
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
    public static <V> @Nullable V parseToml(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseToml(@NotNull String data, @NotNull Type type) {
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
    public static @Nullable Object parseToml(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseToml(@NotNull String data, @NotNull JavaType type) {
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
    public static @Nullable Object parseToml(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static @NotNull String stringifyToml(@NotNull ParsedElement element) {
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
    public static byte[] byteifyToml(@NotNull ParsedElement element) {
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
    public static @NotNull String stringifyToml(@Nullable Object object) {
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
    public static byte[] byteifyToml(@Nullable Object object) {
        return byteifyToml(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static @NotNull String emptyTomlString() {
        return tomlParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static @NotNull String emptyTomlArrayString() {
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
    public static @NotNull ParsedElement parseXml(@NotNull String data) {
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
    public static @NotNull ParsedElement parseXml(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseXml(@NotNull String data, @NotNull Class<V> clazz) {
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
    public static <V> @Nullable V parseXml(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseXml(@NotNull String data, @NotNull Type type) {
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
    public static @Nullable Object parseXml(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseXml(@NotNull String data, @NotNull JavaType type) {
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
    public static @Nullable Object parseXml(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static @NotNull String stringifyXml(@NotNull ParsedElement element) {
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
    public static @NotNull String stringifyXml(@NotNull ParsedElement element, boolean pretty) {
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
    public static byte[] byteifyXml(@NotNull ParsedElement element) {
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
    public static byte[] byteifyXml(@NotNull ParsedElement element, boolean pretty) {
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
    public static @NotNull String stringifyXml(@Nullable Object object) {
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
    public static @NotNull String stringifyXml(@Nullable Object object, boolean pretty) {
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
    public static byte[] byteifyXml(@Nullable Object object) {
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
    public static byte[] byteifyXml(@Nullable Object object, boolean pretty) {
        return byteifyXml(objectProcessor.toElement(object), pretty);
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static @NotNull String emptyXmlString() {
        return xmlParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static @NotNull String emptyXmlArrayString() {
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
    public static @NotNull ParsedElement parseProperties(@NotNull String data) {
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
    public static @NotNull ParsedElement parseProperties(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseProperties(@NotNull String data, @NotNull Class<V> clazz) {
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
    public static <V> @Nullable V parseProperties(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseProperties(@NotNull String data, @NotNull Type type) {
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
    public static @Nullable Object parseProperties(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseProperties(@NotNull String data, @NotNull JavaType type) {
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
    public static @Nullable Object parseProperties(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static @NotNull String stringifyProperties(@NotNull ParsedElement element) {
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
    public static byte[] byteifyProperties(@NotNull ParsedElement element) {
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
    public static @NotNull String stringifyProperties(@Nullable Object object) {
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
    public static byte[] byteifyProperties(@Nullable Object object) {
        return byteifyProperties(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static @NotNull String emptyPropertiesString() {
        return propertiesParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static @NotNull String emptyPropertiesArrayString() {
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
    public static @NotNull ParsedElement parseCsv(@NotNull String data) {
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
    public static @NotNull ParsedElement parseCsv(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseCsv(@NotNull String data, @NotNull Class<V> clazz) {
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
    public static <V> @Nullable V parseCsv(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseCsv(@NotNull String data, @NotNull Type type) {
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
    public static @Nullable Object parseCsv(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseCsv(@NotNull String data, @NotNull JavaType type) {
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
    public static @Nullable Object parseCsv(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static @NotNull String stringifyCsv(@NotNull ParsedElement element) {
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
    public static byte[] byteifyCsv(@NotNull ParsedElement element) {
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
    public static @NotNull String stringifyCsv(@Nullable Object object) {
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
    public static byte[] byteifyCsv(@Nullable Object object) {
        return byteifyCsv(objectProcessor.toElement(object));
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     *
     * @since 1.3.0
     */
    public static @NotNull String emptyCsvString() {
        return csvParser.emptyString();
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty array
     *
     * @since 1.4.0
     */
    public static @NotNull String emptyCsvArrayString() {
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
    public static @NotNull ParsedElement parseSmile(byte @NotNull [] data) {
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
    public static <V> @Nullable V parseSmile(byte @NotNull [] data, @NotNull Class<V> clazz) {
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
    public static @Nullable Object parseSmile(byte @NotNull [] data, @NotNull Type type) {
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
    public static @Nullable Object parseSmile(byte @NotNull [] data, @NotNull JavaType type) {
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
    public static byte[] byteifySmile(@NotNull ParsedElement element) {
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
    public static byte[] byteifySmile(@Nullable Object object) {
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
    public static @Nullable Logger getLogger() {
        return logger;
    }

    /**
     * Set the main logger to be used by BJSL
     *
     * @param value The new logger
     *
     * @since 1.0.0
     */
    public static void setLogger(@Nullable Logger value) {
        logger = value;
    }
}