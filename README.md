# BJSL

A semi-advanced Java data serialization library with features for reducing file sizes.

BJSL is fully documented at [bjsl.kaleko.dev/docs](https://bjsl.kaleko.dev/docs/)

## Parsers

Parsers are how you transform text or binary data in a specific format into workable object trees.\
There are currently 3 supported formats.

- [Json](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/parsers/JsonParser.Builder.html)
- [Yaml](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/parsers/YamlParser.Builder.html)
- [Smile](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/parsers/SmileParser.Builder.html) (Binary Json)

These can be accessed using [`{format}Parser.Builder`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/parsers/package-summary.html) and can be configured if needed.

It is also entirely possible to write your own parser that fits your needs.

## Object Processor

The [`ObjectProcessor`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html) is the heart of BJSL.\
It takes in object trees and transforms them into standard java objects and then transforms them back into object trees.

### Creation

To create an object processor create a new [`ObjectProcessor.Builder`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html). This will allow you to access a few options before building the processor.

- [ignoreNulls](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#setIgnoreNulls(boolean)) -
Ignore nulls allows you to not have null values output into the object tree when serializing maps and objects.

- [ignoreArrayNulls](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#setIgnoreArrayNulls(boolean)) -
Ignore array nulls allows you to not have null values output into the object tree when serializing lists and arrays.

- [ignoreEmptyObjects](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#setIgnoreEmptyObjects(boolean)) -
Ignore empty objects allow you to not have objects with a size of zero (maps, lists, arrays) output into the object tree.

- [ignoreDefaults](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#setIgnoreDefaults(boolean)) -
Ignore defaults allows you to not have default values output into the object tree.\
This works by creating a new instance of the config type to read from. This requires a 0-args constructor to be present on the type.

- [caseSensitiveEnums](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#setCaseSensitiveEnums(boolean)) -
Case sensitive enums allows you to enable enums to be case-sensitive.

When you are done call [`#build()`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#build()) and you will have your [`ObjectProcessor`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html).

### Use

You start with a [`ParsedElement`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/elements/ParsedElement.html) of some kind ([`ParsedObject`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/elements/ParsedObject.html), [`ParsedArray`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/elements/ParsedArray.html)), likely attained from a [`Parser`](#parsers), and a class you would like to deserialize it to.

An example class could be something like
```java
public class User {
    private double foo;
    private double bar = 17.8;

    public double getFoo() {
        return foo;
    }

    public double getBar() {
        return bar;
    }
}
```

Once you have both just call [`#toObject(element, class)`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html#toObject(io.github.kale_ko.bjsl.elements.ParsedElement,java.lang.Class))\
You are then free to modify the returned value.\
When you would like to re-serialize it call [`#toElement(object)`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html#toElement(java.lang.Object))

## Type Processors

TODO

## Full example

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.JsonParser;
import io.github.kale_ko.bjsl.processor.ObjectProcessor;

public class Data {
    public static class User {
        private double foo; // Fields can be any visibility
        private double bar = 17.8;

        public double getFoo() {
            return foo;
        }

        public double getBar() {
            return bar;
        }
    }

    public Map<UUID, User> exampleUsers = new HashMap<>(); // Maps, Lists, and other Collections are supported

    protected String exampleString = "hello world!"; // All primitive types including arrays are supported

    public static void main(String[] args) throws IOException {
        JsonParser parser = new JsonParser.Builder().build(); // Create the parser with default options
        ObjectProcessor processor = new ObjectProcessor.Builder().build(); // Create the processor with default options

        Path inputFile = Path.of("input.json"); // Define input and output files
        Path outputFile = Path.of("output.json");

        String inputData;
        if (Files.exists(inputFile)) {
            inputData = Files.readString(inputFile); // Read in the data
        } else {
            inputData = parser.emptyString(); // Get a parser specific empty data string (e.g. {} for json)
        }

        ParsedElement inputElement = parser.toElement(inputData); // Turn the data into an element tree

        Data data = processor.toObject(inputElement, Data.class); // Turn the element tree into a Data object

        // Modify data

        ParsedElement outputElement = processor.toElement(data); // Turn the Data object into an element tree

        String outputData = parser.toString(outputElement); // Turn the element tree into a string

        Files.writeString(outputFile, outputData); // Write out the data
    }
}
```