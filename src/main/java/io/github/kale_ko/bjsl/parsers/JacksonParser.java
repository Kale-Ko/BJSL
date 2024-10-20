package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TokenStreamFactory;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.parsers.exception.InvalidTypeException;
import io.github.kale_ko.bjsl.parsers.exception.ParserException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract class that all jackson parsers extend from
 * <p>
 * Contains all the logic for parsing, factories are just passed for values
 * <p>
 * Basically just a wrapper for Jackson parsers
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

            if (tree instanceof JsonNode) {
                JsonNode node = (JsonNode) tree;

                if (node instanceof ObjectNode) {
                    ObjectNode objectNode = (ObjectNode) node;
                    ParsedObject objectElement = ParsedObject.create();

                    objectNode.fieldNames().forEachRemaining(subKey -> {
                        toElements(objectElement, subKey, objectNode.get(subKey));
                    });

                    return objectElement;
                } else if (node instanceof ArrayNode) {
                    ArrayNode arrayNode = (ArrayNode) node;
                    ParsedArray arrayElement = ParsedArray.create();

                    arrayNode.elements().forEachRemaining(subNode -> {
                        toElements(arrayElement, "root", subNode);
                    });

                    return arrayElement;
                } else if (node instanceof TextNode) {
                    return ParsedPrimitive.fromString(node.asText());
                } else if (node instanceof ShortNode) {
                    return ParsedPrimitive.fromShort((short) node.asInt());
                } else if (node instanceof IntNode) {
                    return ParsedPrimitive.fromInteger(node.asInt());
                } else if (node instanceof LongNode) {
                    return ParsedPrimitive.fromLong(node.asLong());
                } else if (node instanceof BigIntegerNode) {
                    return ParsedPrimitive.fromBigInteger(node.bigIntegerValue());
                } else if (node instanceof FloatNode) {
                    return ParsedPrimitive.fromFloat((float) node.asDouble());
                } else if (node instanceof DoubleNode) {
                    return ParsedPrimitive.fromDouble(node.asDouble());
                } else if (node instanceof DecimalNode) {
                    return ParsedPrimitive.fromBigDecimal(node.decimalValue());
                } else if (node instanceof BooleanNode) {
                    return ParsedPrimitive.fromBoolean(node.asBoolean());
                } else if (node instanceof NullNode) {
                    return ParsedPrimitive.fromNull();
                } else {
                    throw new InvalidTypeException(node.getClass());
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
            if (element instanceof ParsedObject) {
                ParsedObject objectElement = element.asObject();

                ObjectNode node = JsonNodeFactory.instance.objectNode();
                for (String subKey : objectElement.getKeys()) {
                    toNodes(node, subKey, objectElement.get(subKey));
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try (com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter)) {
                    generator.setCodec(this.codec);
                    generator.writeTree(node);
                }
                outputStream.close();

                return outputStream.toByteArray();
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = element.asArray();

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
            } else if (element instanceof ParsedPrimitive) {
                if (!element.asPrimitive().isNull()) {
                    return element.asPrimitive().get().toString().getBytes(StandardCharsets.UTF_8);
                } else {
                    return "null".getBytes(StandardCharsets.UTF_8);
                }
            } else {
                throw new InvalidTypeException(element.getClass());
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
        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;
            ParsedObject subElement = ParsedObject.create();

            if (element instanceof ParsedObject) {
                ParsedObject objectElement = (ParsedObject) element;
                objectElement.set(key, subElement);
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = (ParsedArray) element;
                arrayElement.add(subElement);
            }

            objectNode.fieldNames().forEachRemaining(subKey -> {
                toElements(subElement, subKey, objectNode.get(subKey));
            });
        } else if (node instanceof ArrayNode) {
            ArrayNode arrayNode = (ArrayNode) node;
            ParsedArray subElement = ParsedArray.create();

            if (element instanceof ParsedObject) {
                ParsedObject objectElement = (ParsedObject) element;
                objectElement.set(key, subElement);
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = (ParsedArray) element;
                arrayElement.add(subElement);
            }

            arrayNode.elements().forEachRemaining(subNode -> {
                toElements(subElement, key, subNode);
            });
        } else if (node instanceof TextNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromString(node.asText()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromString(node.asText()));
            }
        } else if (node instanceof ShortNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromShort((short) node.asInt()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromShort((short) node.asInt()));
            }
        } else if (node instanceof IntNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromInteger(node.asInt()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromInteger(node.asInt()));
            }
        } else if (node instanceof LongNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromLong(node.asLong()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromLong(node.asLong()));
            }
        } else if (node instanceof BigIntegerNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromBigInteger(node.bigIntegerValue()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromBigInteger(node.bigIntegerValue()));
            }
        } else if (node instanceof FloatNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromFloat((float) node.asDouble()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromFloat((float) node.asDouble()));
            }
        } else if (node instanceof DoubleNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromDouble(node.asDouble()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromDouble(node.asDouble()));
            }
        } else if (node instanceof DecimalNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromBigDecimal(node.decimalValue()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromBigDecimal(node.decimalValue()));
            }
        } else if (node instanceof BooleanNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromBoolean(node.asBoolean()));
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromBoolean(node.asBoolean()));
            }
        } else if (node instanceof NullNode) {
            if (element instanceof ParsedObject) {
                ((ParsedObject) element).set(key, ParsedPrimitive.fromNull());
            } else if (element instanceof ParsedArray) {
                ((ParsedArray) element).add(ParsedPrimitive.fromNull());
            }
        } else {
            if (BJSL.getLogger() != null) {
                BJSL.getLogger().warning("Warning while parsing: Node \"" + key + "\" of type \"" + node.getClass().getSimpleName() + "\" is not serializable");
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
    protected void toNodes(@NotNull TreeNode node, @NotNull String key, @NotNull ParsedElement element) {
        if (element instanceof ParsedObject) {
            ParsedObject objectElement = (ParsedObject) element;
            ObjectNode subNode = JsonNodeFactory.instance.objectNode();

            if (node instanceof ObjectNode) {
                ((ObjectNode) node).set(key, subNode);
            } else if (node instanceof ArrayNode) {
                ((ArrayNode) node).add(subNode);
            }

            for (String subKey : objectElement.getKeys()) {
                toNodes(subNode, subKey, objectElement.get(subKey));
            }
        } else if (element instanceof ParsedArray) {
            ParsedArray arrayElement = (ParsedArray) element;
            ArrayNode subNode = JsonNodeFactory.instance.arrayNode();

            if (node instanceof ObjectNode) {
                ((ObjectNode) node).set(key, subNode);
            } else if (node instanceof ArrayNode) {
                ((ArrayNode) node).add(subNode);
            }

            for (ParsedElement subElement : arrayElement.getValues()) {
                toNodes(subNode, key, subElement);
            }
        } else if (element instanceof ParsedPrimitive) {
            ParsedPrimitive primitiveElement = (ParsedPrimitive) element;

            // TODO switch(primitiveElement.getType()) {}

            if (primitiveElement.isString()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asString());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asString());
                }
            } else if (primitiveElement.isByte()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asByte());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asByte());
                }
            } else if (primitiveElement.isChar()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asChar());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asChar());
                }
            } else if (primitiveElement.isShort()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asShort());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asShort());
                }
            } else if (primitiveElement.isInteger()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asInteger());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asInteger());
                }
            } else if (primitiveElement.isLong()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asLong());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asLong());
                }
            } else if (primitiveElement.isBigInteger()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asBigInteger());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asBigInteger());
                }
            } else if (primitiveElement.isFloat()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asFloat());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asFloat());
                }
            } else if (primitiveElement.isDouble()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asDouble());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asDouble());
                }
            } else if (primitiveElement.isBigDecimal()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asBigDecimal());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asBigDecimal());
                }
            } else if (primitiveElement.isBoolean()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).put(key, primitiveElement.asBoolean());
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).add(primitiveElement.asBoolean());
                }
            } else if (primitiveElement.isNull()) {
                if (node instanceof ObjectNode) {
                    ((ObjectNode) node).putNull(key);
                } else if (node instanceof ArrayNode) {
                    ((ArrayNode) node).addNull();
                }
            } else {
                if (BJSL.getLogger() != null) {
                    BJSL.getLogger().warning("Warning while parsing: Element \"" + key + "\" of type \"" + primitiveElement.getType() + "\" is not serializable");
                }
            }
        } else {
            if (BJSL.getLogger() != null) {
                BJSL.getLogger().warning("Warning while parsing: Element \"" + key + "\" of type \"" + element.getClass().getSimpleName() + "\" is not serializable");
            }
        }
    }
}