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

To create an object processor construct a new [`ObjectProcessor.Builder`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html). This will allow you to access a few options before building the processor.

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

An example class could be something like:
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

The only notable limitation of the [`ObjectProcessor`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html) is that Map keys must be translated to a [`ParsedPrimitive`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/elements/ParsedPrimitive.html) type. This can be done using [`TypeProcessor`](#type-processors)s, just output some type of [`ParsedPrimitive`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/elements/ParsedPrimitive.html). This is already done for all default types, see below.

## Type Processors

[`TypeProcessor`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/TypeProcessor.html)s are extensions of the [`ObjectProcessor`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html) that allow you to serialize and deserialize any class in whatever way you see fit.

An example used in the default processors is as follows:
```java
TypeProcessor uuidTypeProcessor = new TypeProcessor() {
    @Override
    public @NotNull ParsedElement toElement(@Nullable Object object) {
        if (object == null) {
            return ParsedPrimitive.fromNull();
        }

        if (object instanceof UUID) {
            return ParsedPrimitive.fromString(object.toString());
        } else {
            throw new InvalidParameterException("object must be UUID");
        }
    }

    @Override
    public @Nullable Object toObject(@NotNull ParsedElement element) {
        if (element.isPrimitive() && element.asPrimitive().isNull()) {
            return null;
        }

        if (element.isPrimitive() && element.asPrimitive().isString()) {
            return UUID.fromString(element.asPrimitive().asString());
        } else {
            throw new InvalidParameterException("object must be String");
        }
    }
};
```

These are registered when building an [`ObjectProcessor`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.html) with [`#createTypeProcessor(class, typeProcessor)`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#createTypeProcessor(java.lang.Class,io.github.kale_ko.bjsl.processor.TypeProcessor))

There is also a list of default type processors that can be toggled using [`#setEnableDefaultTypeProcessors(bool)`](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/ObjectProcessor.Builder.html#setEnableDefaultTypeProcessors(boolean))\
This includes the following:
- `java.util.UUID`
- `java.net.URI`
- `java.net.URL`
- `java.nio.Path`
- `java.io.File`
- `java.net.InetAddress`
- `java.net.InetSocketAddress`
- `java.util.Calendar`
- `java.util.Date`
- `java.time.Instant`

If you think of something not on this list that you think should be feel free to open an issue.

## Annotations and Conditions

There are a couple of annotation types that can be used on serialized fields.

- [@AlwaysSerialize](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/annotations/AlwaysSerialize.html) - Always serialize this field, even if it is marked transient or to be excluded by ignores.
- [@DontSerialize](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/annotations/DontSerialize.html) - Never serialize this field, does the same thing as marking the field as transient.
- [@Rename](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/annotations/Rename.html) - Rename a field to this value when outputting and from this when inputting. (This does not convert old data to match, intended use is for renaming a java field and not updating data)

There are also a few values that can be used to require certain conditions on deserialized values.

- [@ExpectNotNull](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/conditions/ExpectNotNull.html) - Expect that a value is never null
- [@ExpectGreaterThan](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/conditions/ExpectGreaterThan.html) - Expect that a value is greater than (or equal to) this
- [@ExpectLessThan](https://bjsl.kaleko.dev/docs/io/github/kale_ko/bjsl/processor/conditions/ExpectLessThan.html) - Expect that a value is less than (or equal to) this

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

    @NeverSerialize
    protected String excludedString = "goodbye world :("; // This won't get included in the output because it is marked to never serialize
    
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