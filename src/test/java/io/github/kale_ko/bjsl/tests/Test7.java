package io.github.kale_ko.bjsl.tests;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;

public class Test7 extends Test {
    @SuppressWarnings("unused")
    public static class TestClass {
        public static class SubTestClass {
            public static class SubSubTestClass {
                public int x = 5;

                public SubSubTestClass(int x) {
                    this.x = x;
                }
            }

            public float subFloat = 31.5f;

            public String[] stringArray = new String[] { "element 1", "element 2", "element 3", "element 4" };

            public List<Long> longList = Arrays.asList(25153L, 351L, 36886942642L, 293579269246L, 3926426L);

            public List<SubSubTestClass> subSubObjectList = Arrays.asList(new SubSubTestClass(5), new SubSubTestClass(13), new SubSubTestClass(24));

            public Map<String, Boolean> booleanMap = new LinkedHashMap<String, Boolean>();

            public Map<String, SubSubTestClass> subSubObjectMap = new LinkedHashMap<String, SubSubTestClass>();

            public SubTestClass() {
                this.booleanMap.put("element1", false);
                this.booleanMap.put("element2", true);
                this.booleanMap.put("element3", true);
                this.booleanMap.put("element4", true);

                this.subSubObjectMap.put("element1", new SubSubTestClass(13));
                this.subSubObjectMap.put("element2", new SubSubTestClass(27));
                this.subSubObjectMap.put("element3", new SubSubTestClass(82));
                this.subSubObjectMap.put("element4", new SubSubTestClass(97));
            }
        }

        public String pubString = "aCoolString";
        private String privString = "aCoolerString";

        public int anInt = 57;
        public Long aLong = 38689269265279L;

        public SubTestClass subObject = new SubTestClass();
    }

    public Test7() {
        super("Multi layer object to string w/ lists & maps");
    }

    @Override
    public Object run() {
        String result = BJSL.stringifyJson(new TestClass());

        if (result.equals("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279,\n  \"subObject\" : {\n    \"subFloat\" : 31.5,\n    \"stringArray\" : [\n      \"element 1\",\n      \"element 2\",\n      \"element 3\",\n      \"element 4\"\n    ],\n    \"longList\" : [\n      25153,\n      351,\n      36886942642,\n      293579269246,\n      3926426\n    ],\n    \"subSubObjectList\" : [\n      {\n        \"x\" : 5\n      },\n      {\n        \"x\" : 13\n      },\n      {\n        \"x\" : 24\n      }\n    ],\n    \"booleanMap\" : {\n      \"element1\" : false,\n      \"element2\" : true,\n      \"element3\" : true,\n      \"element4\" : true\n    },\n    \"subSubObjectMap\" : {\n      \"element1\" : {\n        \"x\" : 13\n      },\n      \"element2\" : {\n        \"x\" : 27\n      },\n      \"element3\" : {\n        \"x\" : 82\n      },\n      \"element4\" : {\n        \"x\" : 97\n      }\n    }\n  }\n}")) {
            return true;
        } else {
            return result;
        }
    }
}