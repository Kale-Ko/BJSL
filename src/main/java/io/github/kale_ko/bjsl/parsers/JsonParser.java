package io.github.kale_ko.bjsl.parsers;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;

public class JsonParser {
    public static ParsedElement parse(String data) {
        if (data == null) {
            throw new NullPointerException("\"data\" can not be null");
        }

        data = data.trim();

        if (data.startsWith("{") && data.endsWith("}")) {

        } else if (data.startsWith("[") && data.endsWith("]")) {

        }

        return null;
    }

    public static String stringify(ParsedElement element) {
        if (element == null) {
            throw new NullPointerException("\"element\" can not be null");
        }

        try {
            if (element instanceof ParsedObject) {
                ParsedObject objectElement = element.asJsonObject();

                ObjectNode tree = JsonNodeFactory.instance.objectNode();
                for (String subkey : objectElement.getKeys()) {
                    nodeify(tree, subkey, objectElement.get(subkey));
                }

                PipedWriter writer = new PipedWriter();
                PipedReader reader = new PipedReader(writer);
                JsonGenerator generator = JsonFactory.builder().build().createGenerator(writer);
                generator.setCodec(new ObjectMapper());
                generator.writeTree(tree);
                generator.close();

                StringBuilder output = new StringBuilder();
                int read = -1;
                while ((read = reader.read()) != -1) {
                    output.appendCodePoint(read);
                }
                writer.close();
                reader.close();

                return output.toString();
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = element.asJsonArray();

                ArrayNode tree = JsonNodeFactory.instance.arrayNode();
                for (ParsedElement subElement : arrayElement.getValues()) {
                    nodeify(tree, "root", subElement);
                }

                PipedWriter writer = new PipedWriter();
                PipedReader reader = new PipedReader(writer);
                JsonGenerator generator = JsonFactory.builder().build().createGenerator(writer);
                generator.setCodec(new ObjectMapper());
                generator.writeTree(tree);
                generator.close();

                StringBuilder output = new StringBuilder();
                int read = -1;
                while ((read = reader.read()) != -1) {
                    output.appendCodePoint(read);
                }
                writer.close();
                reader.close();

                return output.toString();
            } else {
                return element.asJsonPrimitive().asObject().toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected static void nodeify(TreeNode tree, String key, ParsedElement element) {
        if (element instanceof ParsedObject objectElement) {
            ObjectNode subtree = JsonNodeFactory.instance.objectNode();
            if (tree instanceof ObjectNode objectTree) {
                objectTree.set(key, subtree);
            } else if (tree instanceof ArrayNode objectTree) {
                objectTree.add(subtree);
            }

            for (String subkey : objectElement.getKeys()) {
                nodeify(subtree, subkey, objectElement.get(subkey));
            }
        } else if (element instanceof ParsedArray arrayElement) {
            ArrayNode subtree = JsonNodeFactory.instance.arrayNode();
            if (tree instanceof ObjectNode objectTree) {
                objectTree.set(key, subtree);
            } else if (tree instanceof ArrayNode objectTree) {
                objectTree.add(subtree);
            }

            for (ParsedElement subElement : arrayElement.getValues()) {
                nodeify(subtree, key, subElement);
            }
        } else if (element instanceof ParsedPrimitive primitiveElement) {
            if (primitiveElement.isString()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asString());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asString());
                }
            } else if (primitiveElement.isByte()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asByte());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asByte());
                }
            } else if (primitiveElement.isChar()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asChar());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asChar());
                }
            } else if (primitiveElement.isShort()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asShort());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asShort());
                }
            } else if (primitiveElement.isInteger()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asInteger());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asInteger());
                }
            } else if (primitiveElement.isLong()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asLong());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asLong());
                }
            } else if (primitiveElement.isFloat()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asFloat());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asFloat());
                }
            } else if (primitiveElement.isDouble()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asDouble());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asDouble());
                }
            } else if (primitiveElement.isBoolean()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.put(key, primitiveElement.asBoolean());
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.add(primitiveElement.asBoolean());
                }
            } else if (primitiveElement.isNull()) {
                if (tree instanceof ObjectNode objectTree) {
                    objectTree.putNull(key);
                } else if (tree instanceof ArrayNode objectTree) {
                    objectTree.addNull();
                }
            }
        }
    }
}