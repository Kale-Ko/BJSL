package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
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
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;

public abstract class Parser {
    protected static void toElements(ParsedElement element, String key, JsonNode node) {
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
        }
    }

    protected static void toNodes(TreeNode node, String key, ParsedElement element) {
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
            }
        }
    }
}