package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.elements.ParsedObject;

public class Test2 extends Test {
    public Test2() {
        super("1 layer string to element");
    }

    @Override
    public Object run() {
        ParsedObject result = BJSL.parseJson("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279\n}").asObject();

        if (
            result.has("pubString") && result.get("pubString").isPrimitive() && result.get("pubString").asPrimitive().isString() && result.get("pubString").asPrimitive().asString().equals("aCoolString") &&
            result.has("privString") && result.get("privString").isPrimitive() && result.get("privString").asPrimitive().isString() && result.get("privString").asPrimitive().asString().equals("aCoolerString") &&
            result.has("anInt") && result.get("anInt").isPrimitive() && result.get("anInt").asPrimitive().isLong() && result.get("anInt").asPrimitive().asLong() == 57 &&
            result.has("aLong") && result.get("aLong").isPrimitive() && result.get("aLong").asPrimitive().isLong() && result.get("aLong").asPrimitive().asLong() == 38689269265279L
        ) {
            return true;
        } else {
            return false;
        }
    }
}