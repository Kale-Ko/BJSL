package io.github.kale_ko.bjsl.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TokenStreamFactory;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;

/**
 * An abstract class that all parsers extend from
 * <p>
 * Contains all the logic for parsing, factories are just passed for values
 * <p>
 * Basically just a wrapper for Jackson parsers
 *
 * @param <T>
 *        The type of the factory used for converting to/from trees/strings
 * @param <V>
 *        The type of the codec used for converting to/from trees/strings
 * @version 1.3.0
 * @since 1.0.0
 */
public abstract class Parser<T extends TokenStreamFactory, V extends ObjectCodec> {
    /**
     * The factory used for converting to/from trees/strings
     *
     * @since 1.0.0
     */
    protected T factory;

    /**
     * The codec used for converting to/from trees/strings
     *
     * @since 1.0.0
     */
    protected V codec;

    /**
     * The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected PrettyPrinter prettyPrinter;

    /**
     * Create a new Parser using certain factories
     *
     * @param factory
     *        The factory used for converting to/from trees/strings
     * @param codec
     *        The codec used for converting to/from trees/strings
     * @param prettyPrinter
     *        The prettyPrinter used for converting to strings
     * @since 1.0.0
     */
    protected Parser(T factory, V codec, PrettyPrinter prettyPrinter) {
        this.factory = factory;
        this.codec = codec;

        this.prettyPrinter = prettyPrinter;
    }

    /**
     * Parse this string into a {@link ParsedElement}
     * <p>
     * Calls {@link #toElement(byte[])} with the bytes of the passed string ({@link String#getBytes()})
     *
     * @param data
     *        The string to parse
     * @return The string passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public ParsedElement toElement(String data) {
        if (data == null || data.length() == 0) {
            throw new NullPointerException("Data can not be null");
        }

        return toElement(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Parse these bytes into a {@link ParsedElement}
     * <p>
     * Calls Jackson functions to get a parsed node and then converts it into BJSL's format
     *
     * @param data
     *        The bytes to parse
     * @return The bytes passed parsed to a {@link ParsedElement}
     * @since 1.0.0
     */
    public ParsedElement toElement(byte[] data) {
        if (data == null || data.length == 0) {
            throw new NullPointerException("Data can not be null");
        }

        try {
            com.fasterxml.jackson.core.JsonParser parser = this.factory.createParser(data);
            parser.setCodec(this.codec);
            TreeNode tree = parser.readValueAsTree();
            parser.close();

            if (tree instanceof JsonNode node) {
                if (node instanceof ObjectNode objectNode) {
                    ParsedObject objectElement = ParsedObject.create();

                    objectNode.fieldNames().forEachRemaining((String subKey) -> {
                        toElements(objectElement, subKey, objectNode.get(subKey));
                    });

                    return objectElement;
                } else if (node instanceof ArrayNode arrayNode) {
                    ParsedArray arrayElement = ParsedArray.create();

                    arrayNode.elements().forEachRemaining((JsonNode subNode) -> {
                        toElements(arrayElement, "root", subNode);
                    });

                    return arrayElement;
                } else if (node instanceof TextNode) {
                    return ParsedPrimitive.fromString(node.asText());
                } else if (node instanceof ShortNode) {
                    return ParsedPrimitive.fromShort((short) node.asInt());
                } else if (node instanceof IntNode) {
                    return ParsedPrimitive.fromInteger(node.asInt());
                } else if (node instanceof BigIntegerNode) {
                    return ParsedPrimitive.fromBigInteger(node.bigIntegerValue());
                } else if (node instanceof LongNode) {
                    return ParsedPrimitive.fromLong(node.asLong());
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
                    throw new RuntimeException("\"" + node.getClass().getSimpleName() + "\" is not a parsable type");
                }
            } else {
                throw new RuntimeException("\"" + tree.getClass().getSimpleName() + "\" is not a parsable type");
            }
        } catch (RuntimeException | IOException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());

                return null;
            } else {
                throw new RuntimeException("Error while parsing:", e);
            }
        }
    }

    /**
     * Serializes this element into a String
     * <p>
     * Calls {@link #toString(ParsedElement)} and uses {@link String#String(byte[])} to create a string from that
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to a String
     * @since 1.0.0
     */
    public String toString(ParsedElement element) {
        if (element == null) {
            throw new NullPointerException("Element can not be null");
        }

        return new String(toBytes(element), StandardCharsets.UTF_8);
    }

    /**
     * Serializes this element into bytes
     * <p>
     * Converts it from BJSL's format and calls Jackson functions to get bytes
     *
     * @param element
     *        The element to serialize
     * @return The element passed serialized to bytes
     * @since 1.0.0
     */
    public byte[] toBytes(ParsedElement element) {
        if (element == null) {
            throw new NullPointerException("Element can not be null");
        }

        try {
            if (element instanceof ParsedObject) {
                ParsedObject objectElement = element.asObject();

                ObjectNode node = JsonNodeFactory.instance.objectNode();
                for (String subKey : objectElement.getKeys()) {
                    toNodes(node, subKey, objectElement.get(subKey));
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter).setCodec(this.codec);
                generator.writeTree(node);
                generator.close();
                outputStream.close();

                return outputStream.toByteArray();
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = element.asArray();

                ArrayNode node = JsonNodeFactory.instance.arrayNode();
                for (ParsedElement subElement : arrayElement.getValues()) {
                    toNodes(node, "root", subElement);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter).setCodec(this.codec);
                generator.writeTree(node);
                generator.close();
                outputStream.close();

                return outputStream.toByteArray();
            } else if (element instanceof ParsedPrimitive) {
                if (!element.asPrimitive().isNull()) {
                    return element.asPrimitive().get().toString().getBytes(StandardCharsets.UTF_8);
                } else {
                    return "null".getBytes(StandardCharsets.UTF_8);
                }
            } else {
                throw new RuntimeException("\"" + element.getClass().getSimpleName() + "\" is not a parsable type");
            }
        } catch (RuntimeException | IOException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());

                return null;
            } else {
                throw new RuntimeException("Error while parsing:", e);
            }
        }
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     * @since 1.3.0
     */
    public String emptyString() {
        return new String(emptyBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Serializes an empty element into a string
     *
     * @return A string for a new/empty object
     * @since 1.4.0
     */
    public String emptyArrayString() {
        return new String(emptyArrayBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty object
     * @since 1.3.0
     */
    public byte[] emptyBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter).setCodec(this.codec);
            generator.writeTree(JsonNodeFactory.instance.objectNode());
            generator.close();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (RuntimeException | IOException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());

                return null;
            } else {
                throw new RuntimeException("Error while parsing:", e);
            }
        }
    }

    /**
     * Serializes an empty element into bytes
     *
     * @return The bytes for a new/empty array
     * @since 1.4.0
     */
    public byte[] emptyArrayBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            com.fasterxml.jackson.core.JsonGenerator generator = this.factory.createGenerator(outputStream).setPrettyPrinter(this.prettyPrinter).setCodec(this.codec);
            generator.writeTree(JsonNodeFactory.instance.arrayNode());
            generator.close();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (RuntimeException | IOException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());
            }

            throw new RuntimeException("Error while parsing:", e);
        }
    }

    /**
     * Converts Jackson's format to BJSL's format
     *
     * @param element
     *        The element to add too
     * @param key
     *        The key of the element
     * @param node
     *        The node to convert
     * @since 1.0.0
     */
    protected void toElements(ParsedElement element, String key, JsonNode node) {
        if (node instanceof ObjectNode objectNode) {
            ParsedObject subElement = ParsedObject.create();
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, subElement);
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(subElement);
            }

            objectNode.fieldNames().forEachRemaining((String subKey) -> {
                toElements(subElement, subKey, objectNode.get(subKey));
            });
        } else if (node instanceof ArrayNode arrayNode) {
            ParsedArray subElement = ParsedArray.create();
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, subElement);
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(subElement);
            }

            arrayNode.elements().forEachRemaining((JsonNode subNode) -> {
                toElements(subElement, key, subNode);
            });
        } else if (node instanceof TextNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromString(node.asText()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromString(node.asText()));
            }
        } else if (node instanceof ShortNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromShort((short) node.asInt()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromShort((short) node.asInt()));
            }
        } else if (node instanceof IntNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromInteger(node.asInt()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromInteger(node.asInt()));
            }
        } else if (node instanceof BigIntegerNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromBigInteger(node.bigIntegerValue()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromBigInteger(node.bigIntegerValue()));
            }
        } else if (node instanceof LongNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromLong(node.asLong()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromLong(node.asLong()));
            }
        } else if (node instanceof FloatNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromFloat((float) node.asDouble()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromFloat((float) node.asDouble()));
            }
        } else if (node instanceof DoubleNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromDouble(node.asDouble()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromDouble(node.asDouble()));
            }
        } else if (node instanceof DecimalNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromBigDecimal(node.decimalValue()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromBigDecimal(node.decimalValue()));
            }
        } else if (node instanceof BooleanNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromBoolean(node.asBoolean()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromBoolean(node.asBoolean()));
            }
        } else if (node instanceof NullNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromNull());
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromNull());
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
     * @param element
     *        The element to add too
     * @param key
     *        The key of the element
     * @param node
     *        The node to convert
     * @since 1.0.0
     */
    protected void toNodes(TreeNode node, String key, ParsedElement element) {
        if (element instanceof ParsedObject objectElement) {
            ObjectNode subNode = JsonNodeFactory.instance.objectNode();
            if (node instanceof ObjectNode objectNode) {
                objectNode.set(key, subNode);
            } else if (node instanceof ArrayNode arrayNode) {
                arrayNode.add(subNode);
            }

            for (String subkey : objectElement.getKeys()) {
                toNodes(subNode, subkey, objectElement.get(subkey));
            }
        } else if (element instanceof ParsedArray arrayElement) {
            ArrayNode subNode = JsonNodeFactory.instance.arrayNode();
            if (node instanceof ObjectNode objectNode) {
                objectNode.set(key, subNode);
            } else if (node instanceof ArrayNode objectNode) {
                objectNode.add(subNode);
            }

            for (ParsedElement subElement : arrayElement.getValues()) {
                toNodes(subNode, key, subElement);
            }
        } else if (element instanceof ParsedPrimitive primitiveElement) {
            if (primitiveElement.isString()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asString());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asString());
                }
            } else if (primitiveElement.isByte()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asByte());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asByte());
                }
            } else if (primitiveElement.isChar()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asChar());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asChar());
                }
            } else if (primitiveElement.isShort()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asShort());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asShort());
                }
            } else if (primitiveElement.isInteger()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asInteger());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asInteger());
                }
            } else if (primitiveElement.isBigInteger()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asBigInteger());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asBigInteger());
                }
            } else if (primitiveElement.isLong()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asLong());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asLong());
                }
            } else if (primitiveElement.isFloat()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asFloat());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asFloat());
                }
            } else if (primitiveElement.isDouble()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asDouble());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asDouble());
                }
            } else if (primitiveElement.isBigDecimal()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asBigDecimal());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asBigDecimal());
                }
            } else if (primitiveElement.isBoolean()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asBoolean());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asBoolean());
                }
            } else if (primitiveElement.isNull()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.putNull(key);
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.addNull();
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