package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.TestResult;
import io.github.kale_ko.bjsl.elements.ParsedObject;

public class Test6 extends Test {
    public Test6() {
        super("Multi layer string to element w/ lists & maps");
    }

    @Override
    public TestResult run() {
        ParsedObject result = BJSL.parseJson("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279,\n  \"subObject\" : {\n    \"subFloat\" : 31.5,\n    \"stringArray\" : [\n      \"element 1\",\n      \"element 2\",\n      \"element 3\",\n      \"element 4\"\n    ],\n    \"longList\" : [\n      25153,\n      351,\n      36886942642,\n      293579269246,\n      3926426\n    ],\n    \"subSubObjectList\" : [\n      {\n        \"x\" : 5\n      },\n      {\n        \"x\" : 13\n      },\n      {\n        \"x\" : 24\n      }\n    ],\n    \"booleanMap\" : {\n      \"element1\" : false,\n      \"element2\" : true,\n      \"element3\" : true,\n      \"element4\" : true\n    },\n    \"subSubObjectMap\" : {\n      \"element1\" : {\n        \"x\" : 13\n      },\n      \"element2\" : {\n        \"x\" : 27\n      },\n      \"element3\" : {\n        \"x\" : 82\n      },\n      \"element4\" : {\n        \"x\" : 97\n      }\n    }\n  }\n}").asObject();

        if (result.has("pubString") && result.get("pubString").isPrimitive() && result.get("pubString").asPrimitive().isString() && result.get("pubString").asPrimitive().asString().equals("aCoolString") && result.has("privString") && result.get("privString").isPrimitive() && result.get("privString").asPrimitive().isString() && result.get("privString").asPrimitive().asString().equals("aCoolerString") && result.has("anInt") && result.get("anInt").isPrimitive() && result.get("anInt").asPrimitive().isInteger() && result.get("anInt").asPrimitive().asInteger() == 57 && result.has("aLong") && result.get("aLong").isPrimitive() && result.get("aLong").asPrimitive().isLong() && result.get("aLong").asPrimitive().asLong() == 38689269265279L && result.has("subObject") && result.get("subObject").isObject() && result.get("subObject").asObject().has("subFloat") && result.get("subObject").asObject().get("subFloat").isPrimitive() && result.get("subObject").asObject().get("subFloat").asPrimitive().asDouble() == 31.5) {
            return new TestResult(TestResult.Status.SUCCEEDED, result);
        } else {
            return new TestResult(TestResult.Status.FAILED, result);
        }
    }
}