package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;

public class Test1 extends Test {
    public Test1() {
        super("Single layer element to string");
    }

    @Override
    public Object run() {
        ParsedObject element = ParsedObject.create();
        element.set("pubString", ParsedPrimitive.fromString("aCoolString"));
        element.set("privString", ParsedPrimitive.from("aCoolerString"));
        element.set("anInt", ParsedPrimitive.fromInteger(57));
        element.set("aLong", ParsedPrimitive.fromLong(38689269265279L));

        String result = BJSL.stringifyJson(element);

        if (result.equals("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279\n}")) {
            return true;
        } else {
            return result;
        }
    }
}