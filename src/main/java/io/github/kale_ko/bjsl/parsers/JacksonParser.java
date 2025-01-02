package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TokenStreamFactory;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.parsers.exception.InvalidTypeException;
import io.github.kale_ko.bjsl.parsers.exception.ParserException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract class that all Jackson parsers extend from
 * <p>
 * Contains all the logic for parsing, factories are just passed for values
 *
 * @param <T> The type of the factory used for converting to/from trees/strings
 * @param <V> The type of the codec used for converting to/from trees/strings
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public abstract class JacksonParser<T extends TokenStreamFactory, V extends ObjectCodec> extends Parser {
    /**
     * The factory used for converting to/from trees/strings
     *
     * @since 1.0.0
     */
    protected final @NotNull T factory;

    /**
     * The codec used for converting to/from trees/strings
     *
     * @since 1.0.0
     */
    protected final @NotNull V codec;

    /**
     * The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected final @Nullable PrettyPrinter prettyPrinter;

    /**
     * Create a new JacksonParser using certain factories
     *
     * @param factory       The factory used for converting to/from trees/strings
     * @param codec         The codec used for converting to/from trees/strings
     * @param prettyPrinter The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected JacksonParser(@NotNull T factory, @NotNull V codec, @Nullable PrettyPrinter prettyPrinter) {
        this.factory = factory;
        this.codec = codec;

        this.prettyPrinter = prettyPrinter;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Calls {@link #toElement(byte[])} with the bytes of the passed string ({@link String#getBytes()})
     */
    @Override
    public @NotNull ParsedElement toElement(@NotNull String data) {
        return toElement(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ParsedElement toElement(byte @NotNull [] data) {
        try {
            TreeNode tree;
            try (com.fasterxml.jackson.core.JsonParser parser = this.factory.createParser(data)) {
                parser.setCodec(this.codec);
                tree = parser.readValueAsTree();
            }

            if (tree instanceof JsonNode node) {
                switch (node.getNodeType()) {
                    case OBJECT -> {
                        ObjectNode objectNode = (ObjectNode) node;
                        ParsedObject objectElement = ParsedObject.create();

                        objectNode.fields().forEachRemaining(subEntry -> {
                            toElements(objectElement, subEntry.getKey(), subEntry.getValue());
                        });

                        return objectElement;
                    }
                    case ARRAY -> {
                        ArrayNode arrayNode = (ArrayNode) node;
                        ParsedArray arrayElement = ParsedArray.create();

                        arrayNode.elements().forEachRemaining(subNode -> {
                            toElements(arrayElement, "root", subNode);
                        });

                        return arrayElement;
                    }
                    case STRING -> {
                        return ParsedPrimitive.fromString(node.asText());
                    }
                    case NUMBER -> {
                        switch (node.numberType()) {
                            case INT -> {
                                return ParsedPrimitive.fromInteger(node.asInt());
                            }
                            case LONG -> {
                                return ParsedPrimitive.fromLong(node.asLong());
                            }
                            case BIG_INTEGER -> {
                                return ParsedPrimitive.fromBigInteger(node.bigIntegerValue());
                            }
                            case FLOAT -> {
                                return ParsedPrimitive.fromFloat((float) node.asDouble());
                            }
                            case DOUBLE -> {
                                return ParsedPrimitive.fromDouble(node.asDouble());
                            }
                            case BIG_DECIMAL -> {
                                return ParsedPrimitive.fromBigDecimal(node.decimalValue());
                            }
                            default -> {
                                throw new InvalidTypeException(node.getClass());
                            }
                        }
                    }
                    case BOOLEAN -> {
                        return ParsedPrimitive.fromBoolean(node.asBoolean());
                    }
                    case NULL -> {
                        return ParsedPrimitive.fromNull();
                    }
                    default -> {
                        throw new InvalidTypeException(node.getClass());
                    }
                }
            } else {
                throw new InvalidTypeException(tree.getClass());
            }
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Calls {@link #toBytes(ParsedElement)} and uses {@link String#String(byte[])} to create a string from that
     */
    @Override
    public @NotNull String toString(@NotNull ParsedElement element) {
        return new String(toBytes(element), StandardCharsets.UTF_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte @NotNull [] toBytes(@NotNull ParsedElement element) {
        try {
            switch (element) {
                case ParsedObject objectElement -> {
                    ObjectNode node = JsonNodeFactory.instance.objectNode();
                    for (Map.Entry<String, ParsedElement> subElement : objectElement.getEntries()) {
                        toNodes(node, subElement.getKey(), subElement.getValue());
                    }

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try (com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter)) {
                        generator.setCodec(this.codec);
                        generator.writeTree(node);
                    }
                    outputStream.close();

                    return outputStream.toByteArray();
                }
                case ParsedArray arrayElement -> {
                    ArrayNode node = JsonNodeFactory.instance.arrayNode();
                    for (ParsedElement subElement : arrayElement.getValues()) {
                        toNodes(node, "root", subElement);
                    }

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try (com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter)) {
                        generator.setCodec(this.codec);
                        generator.writeTree(node);
                    }
                    outputStream.close();

                    return outputStream.toByteArray();
                }
                case ParsedPrimitive primitiveElement -> {
                    Object value = primitiveElement.get();
                    return (value != null ? value.toString() : "null").getBytes(StandardCharsets.UTF_8);
                }
                default -> {
                    throw new InvalidTypeException(element.getClass());
                }
            }
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    /**
     * Serializes an empty object element into a string
     *
     * @return A string for a new/empty object
     *
     * @implNote Calls {@link #emptyBytes()} and uses {@link String#String(byte[])} to create a string from that
     * @since 1.3.0
     */
    public @NotNull String emptyString() {
        return new String(emptyBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Serializes an empty array element into a string
     *
     * @return A string for a new/empty array
     *
     * @implNote Calls {@link #emptyArrayBytes()} and uses {@link String#String(byte[])} to create a string from that
     * @since 1.4.0
     */
    public @NotNull String emptyArrayString() {
        return new String(emptyArrayBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Serializes an empty object element into bytes
     *
     * @return The bytes for a new/empty object
     *
     * @since 1.3.0
     */
    public byte @NotNull [] emptyBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter)) {
                generator.setCodec(this.codec);
                generator.writeTree(JsonNodeFactory.instance.objectNode());
            }
            outputStream.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    /**
     * Serializes an empty array element into bytes
     *
     * @return The bytes for a new/empty array
     *
     * @since 1.4.0
     */
    public byte @NotNull [] emptyArrayBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter)) {
                generator.setCodec(this.codec);
                generator.writeTree(JsonNodeFactory.instance.arrayNode());
            }
            outputStream.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    /**
     * Converts Jackson's format to BJSL's format
     *
     * @param element The element to add too
     * @param key     The key of the element
     * @param node    The node to convert
     *
     * @since 1.0.0
     */
    protected void toElements(@NotNull ParsedElement element, @NotNull String key, @NotNull JsonNode node) {
        switch (node.getNodeType()) {
            case OBJECT -> {
                ObjectNode objectNode = (ObjectNode) node;
                ParsedObject subElement = ParsedObject.create();

                switch (element) {
                    case ParsedObject parsedObject -> {
                        parsedObject.set(key, subElement);
                    }
                    case ParsedArray parsedArray -> {
                        parsedArray.add(subElement);
                    }
                    default -> {
                    }
                }

                objectNode.fields().forEachRemaining(subEntry -> {
                    toElements(subElement, subEntry.getKey(), subEntry.getValue());
                });
            }
            case ARRAY -> {
                ArrayNode arrayNode = (ArrayNode) node;
                ParsedArray subElement = ParsedArray.create();

                switch (element) {
                    case ParsedObject parsedObject -> {
                        parsedObject.set(key, subElement);
                    }
                    case ParsedArray parsedArray -> {
                        parsedArray.add(subElement);
                    }
                    default -> {
                    }
                }

                arrayNode.elements().forEachRemaining(subNode -> {
                    toElements(subElement, key, subNode);
                });
            }
            case STRING -> {
                switch (element) {
                    case ParsedObject parsedObject -> {
                        parsedObject.set(key, ParsedPrimitive.fromString(node.asText()));
                    }
                    case ParsedArray parsedArray -> {
                        parsedArray.add(ParsedPrimitive.fromString(node.asText()));
                    }
                    default -> {
                    }
                }
            }
            case NUMBER -> {
                switch (node.numberType()) {
                    case INT -> {
                        switch (element) {
                            case ParsedObject parsedObject -> {
                                parsedObject.set(key, ParsedPrimitive.fromInteger(node.asInt()));
                            }
                            case ParsedArray parsedArray -> {
                                parsedArray.add(ParsedPrimitive.fromInteger(node.asInt()));
                            }
                            default -> {
                            }
                        }
                    }
                    case LONG -> {
                        switch (element) {
                            case ParsedObject parsedObject -> {
                                parsedObject.set(key, ParsedPrimitive.fromLong(node.asLong()));
                            }
                            case ParsedArray parsedArray -> {
                                parsedArray.add(ParsedPrimitive.fromLong(node.asLong()));
                            }
                            default -> {
                            }
                        }
                    }
                    case BIG_INTEGER -> {
                        switch (element) {
                            case ParsedObject parsedObject -> {
                                parsedObject.set(key, ParsedPrimitive.fromBigInteger(node.bigIntegerValue()));
                            }
                            case ParsedArray parsedArray -> {
                                parsedArray.add(ParsedPrimitive.fromBigInteger(node.bigIntegerValue()));
                            }
                            default -> {
                            }
                        }
                    }
                    case FLOAT -> {
                        switch (element) {
                            case ParsedObject parsedObject -> {
                                parsedObject.set(key, ParsedPrimitive.fromFloat((float) node.asDouble()));
                            }
                            case ParsedArray parsedArray -> {
                                parsedArray.add(ParsedPrimitive.fromFloat((float) node.asDouble()));
                            }
                            default -> {
                            }
                        }
                    }
                    case DOUBLE -> {
                        switch (element) {
                            case ParsedObject parsedObject -> {
                                parsedObject.set(key, ParsedPrimitive.fromDouble(node.asDouble()));
                            }
                            case ParsedArray parsedArray -> {
                                parsedArray.add(ParsedPrimitive.fromDouble(node.asDouble()));
                            }
                            default -> {
                            }
                        }
                    }
                    case BIG_DECIMAL -> {
                        switch (element) {
                            case ParsedObject parsedObject -> {
                                parsedObject.set(key, ParsedPrimitive.fromBigDecimal(node.decimalValue()));
                            }
                            case ParsedArray parsedArray -> {
                                parsedArray.add(ParsedPrimitive.fromBigDecimal(node.decimalValue()));
                            }
                            default -> {
                            }
                        }
                    }
                    default -> {
                        if (BJSL.getLogger() != null) {
                            BJSL.getLogger().warning("Warning while parsing: Node \"" + key + "\" of type \"" + node.getClass().getSimpleName() + "\" is not serializable");
                        }
                    }
                }
            }
            case BOOLEAN -> {
                switch (element) {
                    case ParsedObject parsedObject -> {
                        parsedObject.set(key, ParsedPrimitive.fromBoolean(node.asBoolean()));
                    }
                    case ParsedArray parsedArray -> {
                        parsedArray.add(ParsedPrimitive.fromBoolean(node.asBoolean()));
                    }
                    default -> {
                    }
                }
            }
            case NULL -> {
                switch (element) {
                    case ParsedObject parsedObject -> {
                        parsedObject.set(key, ParsedPrimitive.fromNull());
                    }
                    case ParsedArray parsedArray -> {
                        parsedArray.add(ParsedPrimitive.fromNull());
                    }
                    default -> {
                    }
                }
            }
            default -> {
                if (BJSL.getLogger() != null) {
                    BJSL.getLogger().warning("Warning while parsing: Node \"" + key + "\" of type \"" + node.getClass().getSimpleName() + "\" is not serializable");
                }
            }
        }
    }

    /**
     * Converts BJSL's format to Jackson's format
     *
     * @param element The element to add too
     * @param key     The key of the element
     * @param node    The node to convert
     *
     * @since 1.0.0
     */
    @SuppressWarnings("deprecation")
    protected void toNodes(@NotNull JsonNode node, @NotNull String key, @NotNull ParsedElement element) {
        switch (element) {
            case ParsedObject objectElement -> {
                ObjectNode subNode = JsonNodeFactory.instance.objectNode();

                switch (node) {
                    case ObjectNode objectNode -> {
                        objectNode.put(key, subNode);
                    }
                    case ArrayNode arrayNode -> {
                        arrayNode.add(subNode);
                    }
                    default -> {
                    }
                }

                for (Map.Entry<String, ParsedElement> subElement : objectElement.getEntries()) {
                    toNodes(subNode, subElement.getKey(), subElement.getValue());
                }
            }
            case ParsedArray arrayElement -> {
                ArrayNode subNode = JsonNodeFactory.instance.arrayNode();

                switch (node) {
                    case ObjectNode objectNode -> {
                        objectNode.put(key, subNode);
                    }
                    case ArrayNode arrayNode -> {
                        arrayNode.add(subNode);
                    }
                    default -> {
                    }
                }

                for (ParsedElement subElement : arrayElement.getValues()) {
                    toNodes(subNode, key, subElement);
                }
            }
            case ParsedPrimitive primitiveElement -> {
                switch (primitiveElement.getType()) {
                    case STRING -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asString());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asString());
                            }
                            default -> {
                            }
                        }
                    }
                    case BYTE -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asByte());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asByte());
                            }
                            default -> {
                            }
                        }
                    }
                    case CHAR -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asChar());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asChar());
                            }
                            default -> {
                            }
                        }
                    }
                    case SHORT -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asShort());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asShort());
                            }
                            default -> {
                            }
                        }
                    }
                    case INTEGER -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asInteger());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asInteger());
                            }
                            default -> {
                            }
                        }
                    }
                    case LONG -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asLong());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asLong());
                            }
                            default -> {
                            }
                        }
                    }
                    case BIGINTEGER -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asBigInteger());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asBigInteger());
                            }
                            default -> {
                            }
                        }
                    }
                    case FLOAT -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asFloat());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asFloat());
                            }
                            default -> {
                            }
                        }
                    }
                    case DOUBLE -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asDouble());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asDouble());
                            }
                            default -> {
                            }
                        }
                    }
                    case BIGDECIMAL -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asBigDecimal());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asBigDecimal());
                            }
                            default -> {
                            }
                        }
                    }
                    case BOOLEAN -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.put(key, primitiveElement.asBoolean());
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.add(primitiveElement.asBoolean());
                            }
                            default -> {
                            }
                        }
                    }
                    case NULL -> {
                        switch (node) {
                            case ObjectNode objectNode -> {
                                objectNode.putNull(key);
                            }
                            case ArrayNode arrayNode -> {
                                arrayNode.addNull();
                            }
                            default -> {
                            }
                        }
                    }
                }
            }
            default -> {
                if (BJSL.getLogger() != null) {
                    BJSL.getLogger().warning("Warning while parsing: Element \"" + key + "\" of type \"" + element.getClass().getSimpleName() + "\" is not serializable");
                }
            }
        }
    }
}