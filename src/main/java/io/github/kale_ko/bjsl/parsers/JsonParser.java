package io.github.kale_ko.bjsl.parsers;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;

public class JsonParser extends Parser {
    public static ParsedElement toElement(String data) {
        if (data == null) {
            throw new NullPointerException("\"data\" can not be null");
        }

        data = data.trim();

        try {
            com.fasterxml.jackson.core.JsonParser parser = JsonFactory.builder().build().createParser(data);
            parser.setCodec(new ObjectMapper());
            TreeNode tree = parser.readValueAsTree();
            parser.close();

            if (tree instanceof ObjectNode objectNode) {
                ParsedObject objectElement = ParsedObject.create();

                objectNode.fieldNames().forEachRemaining((String subKey) -> {
                    toElements(objectElement, subKey, objectNode.get(subKey));
                });

                return objectElement;
            } else if (tree instanceof ArrayNode arrayNode) {
                ParsedArray objectElement = ParsedArray.create();

                arrayNode.elements().forEachRemaining((JsonNode subNode) -> {
                    toElements(objectElement, "root", subNode);
                });

                return objectElement;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toString(ParsedElement element) {
        if (element == null) {
            throw new NullPointerException("\"element\" can not be null");
        }

        try {
            if (element instanceof ParsedObject) {
                ParsedObject objectElement = element.asObject();

                ObjectNode tree = JsonNodeFactory.instance.objectNode();
                for (String subKey : objectElement.getKeys()) {
                    toNodes(tree, subKey, objectElement.get(subKey));
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

                return output.toString().trim();
            } else if (element instanceof ParsedArray) {
                ParsedArray arrayElement = element.asArray();

                ArrayNode tree = JsonNodeFactory.instance.arrayNode();
                for (ParsedElement subElement : arrayElement.getValues()) {
                    toNodes(tree, "root", subElement);
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

                return output.toString().trim();
            } else {
                return element.asPrimitive().asObject().toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}