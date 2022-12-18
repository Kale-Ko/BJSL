package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;

public class Test5 extends Test {
    public Test5() {
        super("Multi layer element to string w/ lists & maps");
    }

    @Override
    public Object run() {
        ParsedObject element = ParsedObject.create();
        element.set("pubString", ParsedPrimitive.fromString("aCoolString"));
        element.set("privString", ParsedPrimitive.from("aCoolerString"));
        element.set("anInt", ParsedPrimitive.fromInteger(57));
        element.set("aLong", ParsedPrimitive.fromLong(38689269265279L));
        ParsedObject element2 = ParsedObject.create();
        element2.set("subFloat", ParsedPrimitive.fromFloat(31.5f));
        ParsedArray array1 = ParsedArray.create();
        array1.add(ParsedPrimitive.fromString("element 1"));
        array1.add(ParsedPrimitive.fromString("element 2"));
        array1.add(ParsedPrimitive.fromString("element 3"));
        array1.add(ParsedPrimitive.fromString("element 4"));
        element2.set("stringArray", array1);
        ParsedArray array2 = ParsedArray.create();
        array2.add(ParsedPrimitive.fromLong(25153L));
        array2.add(ParsedPrimitive.fromLong(351L));
        array2.add(ParsedPrimitive.fromLong(36886942642L));
        array2.add(ParsedPrimitive.fromLong(293579269246L));
        array2.add(ParsedPrimitive.fromLong(3926426L));
        element2.set("longList", array2);
        ParsedArray array3 = ParsedArray.create();
        ParsedObject element3 = ParsedObject.create();
        element3.set("x", ParsedPrimitive.fromInteger(5));
        array3.add(element3);
        ParsedObject element4 = ParsedObject.create();
        element4.set("x", ParsedPrimitive.fromInteger(13));
        array3.add(element4);
        ParsedObject element5 = ParsedObject.create();
        element5.set("x", ParsedPrimitive.fromInteger(24));
        array3.add(element5);
        element2.set("subSubObjectList", array3);
        ParsedObject element6 = ParsedObject.create();
        element6.set("element1", ParsedPrimitive.fromBoolean(false));
        element6.set("element2", ParsedPrimitive.fromBoolean(true));
        element6.set("element3", ParsedPrimitive.fromBoolean(true));
        element6.set("element4", ParsedPrimitive.fromBoolean(true));
        element2.set("booleanMap", element6);
        ParsedObject element7 = ParsedObject.create();
        ParsedObject element8 = ParsedObject.create();
        element8.set("x", ParsedPrimitive.fromInteger(13));
        element7.set("element1", element8);
        ParsedObject element9 = ParsedObject.create();
        element9.set("x", ParsedPrimitive.fromInteger(27));
        element7.set("element2", element9);
        ParsedObject element10 = ParsedObject.create();
        element10.set("x", ParsedPrimitive.fromInteger(82));
        element7.set("element3", element10);
        ParsedObject element11 = ParsedObject.create();
        element11.set("x", ParsedPrimitive.fromInteger(97));
        element7.set("element4", element11);
        element2.set("subSubObjectMap", element7);
        element.set("subObject", element2);

        String result = BJSL.stringifyJson(element);

        if (result.equals("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279,\n  \"subObject\" : {\n    \"subFloat\" : 31.5,\n    \"stringArray\" : [\n      \"element 1\",\n      \"element 2\",\n      \"element 3\",\n      \"element 4\"\n    ],\n    \"longList\" : [\n      25153,\n      351,\n      36886942642,\n      293579269246,\n      3926426\n    ],\n    \"subSubObjectList\" : [\n      {\n        \"x\" : 5\n      },\n      {\n        \"x\" : 13\n      },\n      {\n        \"x\" : 24\n      }\n    ],\n    \"booleanMap\" : {\n      \"element1\" : false,\n      \"element2\" : true,\n      \"element3\" : true,\n      \"element4\" : true\n    },\n    \"subSubObjectMap\" : {\n      \"element1\" : {\n        \"x\" : 13\n      },\n      \"element2\" : {\n        \"x\" : 27\n      },\n      \"element3\" : {\n        \"x\" : 82\n      },\n      \"element4\" : {\n        \"x\" : 97\n      }\n    }\n  }\n}")) {
            return true;
        } else {
            return result;
        }
    }
}