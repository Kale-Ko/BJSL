package io.github.kale_ko.bjsl.parsers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
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

public abstract class Parser {
    protected JsonFactory jsonFactory;
    protected ObjectCodec codec;

    protected PrettyPrinter prettyPrinter;

    protected Parser(JsonFactory jsonFactory, PrettyPrinter prettyPrinter) {
        this.jsonFactory = jsonFactory;
        this.codec = new ObjectMapper();

        this.prettyPrinter = prettyPrinter;
    }

    public ParsedElement toElement(String data) {
        if (data == null) {
            throw new NullPointerException("Data can not be null");
        }

        data = data.trim();

        try {
            com.fasterxml.jackson.core.JsonParser parser = this.jsonFactory.createParser(data);
            parser.setCodec(this.codec);
            TreeNode tree = parser.readValueAsTree();
            parser.close();

            if (tree instanceof ObjectNode objectNode) {
                ParsedObject objectElement = ParsedObject.create();

                objectNode.fieldNames().forEachRemaining((String subKey) -> {
                    toElements(objectElement, subKey, objectNode.get(subKey));
                });

                return objectElement;
            } else if (tree instanceof ArrayNode arrayNode) {
                ParsedArray arrayElement = ParsedArray.create();

                arrayNode.elements().forEachRemaining((JsonNode subNode) -> {
                    toElements(arrayElement, "root", subNode);
                });

                return arrayElement;
            } else {
                // TODO Return the primitive type

                throw new RuntimeException("Data is not an object or array");
            }
        } catch (RuntimeException | IOException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());
            }

            throw new RuntimeException("Error while parsing:", e);
        }
    }

    public String toString(ParsedElement element) {
        if (element == null) {
            throw new NullPointerException("Element can not be null");
        }

        try {
            if (element instanceof ParsedObject) {
                ParsedObject objectElement = element.asObject();

                ObjectNode tree = JsonNodeFactory.instance.objectNode();
                for (String subKey : objectElement.getKeys()) {
                    toNodes(tree, subKey, objectElement.get(subKey));
                }

                StringWriter writer = new StringWriter();
                JsonGenerator generator = this.jsonFactory.createGenerator(writer);
                generator = generator.setPrettyPrinter(this.prettyPrinter);
                generator.setCodec(this.codec);
                generator.writeTree(tree);
                generator.close();
                writer.close();

                return writer.toString().trim();
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = element.asArray();

                ArrayNode tree = JsonNodeFactory.instance.arrayNode();
                for (ParsedElement subElement : arrayElement.getValues()) {
                    toNodes(tree, "root", subElement);
                }

                StringWriter writer = new StringWriter();
                JsonGenerator generator = this.jsonFactory.createGenerator(writer);
                generator = generator.setPrettyPrinter(this.prettyPrinter);
                generator.setCodec(this.codec);
                generator.writeTree(tree);
                generator.close();
                writer.close();

                return writer.toString().trim();
            } else if (element instanceof ParsedPrimitive) {
                if (!element.asPrimitive().isNull()) {
                    return element.asPrimitive().get().toString().trim();
                } else {
                    return "null";
                }
            } else {
                throw new RuntimeException("Element is not a parsable type");
            }
        } catch (RuntimeException | IOException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());
            }

            throw new RuntimeException("Error while parsing:", e);
        }
    }

    protected void toElements(ParsedElement element, String key, JsonNode node) {
        if (node instanceof ObjectNode objectNode) {
            ParsedObject subElement = ParsedObject.create();
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, subElement);
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(subElement);
            } else {
                throw new RuntimeException("Element is not an object or array");
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
            } else {
                throw new RuntimeException("Element is not an object or array");
            }

            arrayNode.elements().forEachRemaining((JsonNode subNode) -> {
                toElements(subElement, key, subNode);
            });
        } else if (node instanceof TextNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromString(node.asText()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromString(node.asText()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof ShortNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromShort((short) node.asInt()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromShort((short) node.asInt()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof IntNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromInteger(node.asInt()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromInteger(node.asInt()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof LongNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromLong(node.asLong()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromLong(node.asLong()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof FloatNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromFloat((float) node.asDouble()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromFloat((float) node.asDouble()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof DoubleNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromDouble(node.asDouble()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromDouble(node.asDouble()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof BooleanNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromBoolean(node.asBoolean()));
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromBoolean(node.asBoolean()));
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else if (node instanceof NullNode) {
            if (element instanceof ParsedObject objectElement) {
                objectElement.set(key, ParsedPrimitive.fromNull());
            } else if (element instanceof ParsedArray arrayElement) {
                arrayElement.add(ParsedPrimitive.fromNull());
            } else {
                throw new RuntimeException("Element is not an object or array");
            }
        } else {
            if (BJSL.getLogger() != null) {
                BJSL.getLogger().warning("Warning while parsing: Node \"" + key + "\" of type \"" + node.getClass().getSimpleName() + "\" is not serializable");
            }
        }
    }

    protected void toNodes(TreeNode node, String key, ParsedElement element) {
        if (element instanceof ParsedObject objectElement) {
            ObjectNode subNode = JsonNodeFactory.instance.objectNode();
            if (node instanceof ObjectNode objectNode) {
                objectNode.set(key, subNode);
            } else if (node instanceof ArrayNode arrayNode) {
                arrayNode.add(subNode);
            } else {
                throw new RuntimeException("Node is not an object or array");
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
            } else {
                throw new RuntimeException("Node is not an object or array");
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
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isByte()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asByte());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asByte());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isChar()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asChar());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asChar());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isShort()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asShort());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asShort());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isInteger()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asInteger());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asInteger());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isLong()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asLong());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asLong());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isFloat()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asFloat());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asFloat());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isDouble()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asDouble());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asDouble());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isBoolean()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.put(key, primitiveElement.asBoolean());
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.add(primitiveElement.asBoolean());
                } else {
                    throw new RuntimeException("Node is not an object or array");
                }
            } else if (primitiveElement.isNull()) {
                if (node instanceof ObjectNode objectNode) {
                    objectNode.putNull(key);
                } else if (node instanceof ArrayNode objectNode) {
                    objectNode.addNull();
                } else {
                    throw new RuntimeException("Node is not an object or array");
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