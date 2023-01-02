package io.github.kale_ko.bjsl.tests;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.TestResult;

public class Test10 extends Test {
    public static class TestClass {
        public static class SubTestClass {
            public static class SubSubTestClass {
                public int x = 8;

                public SubSubTestClass(int x) {
                    this.x = x;
                }

                @Override
                public boolean equals(Object y) {
                    return y instanceof SubSubTestClass z && this.x == z.x;
                }
            }

            public float subFloat = 85.275f;

            public String[] stringArray = new String[] { "element 5", "element 17", "element 9", "element 16" };

            public List<Long> longList = Arrays.asList(16466L, 164L, 164802642L, 18649826982895L);

            public List<SubSubTestClass> subSubObjectList = Arrays.asList(new SubSubTestClass(7), new SubSubTestClass(17), new SubSubTestClass(32));

            public Map<String, Boolean> booleanMap = new LinkedHashMap<String, Boolean>();

            public Map<String, SubSubTestClass> subSubObjectMap = new LinkedHashMap<String, SubSubTestClass>();

            public SubTestClass() {
                this.booleanMap.put("element1", true);
                this.booleanMap.put("element2", true);
                this.booleanMap.put("element3", false);
                this.booleanMap.put("element4", false);

                this.subSubObjectMap.put("element1", new SubSubTestClass(19));
                this.subSubObjectMap.put("element2", new SubSubTestClass(21));
                this.subSubObjectMap.put("element3", new SubSubTestClass(35));
                this.subSubObjectMap.put("element4", new SubSubTestClass(83));
            }
        }

        public String pubString = "anEpicString";
        private String privString = "aMoreEpicString";

        public int anInt = 35;
        public Long aLong = 486295979489289L;

        public SubTestClass subObject = new SubTestClass();
    }

    public Test10() {
        super("Multi layer string to object w/ lists & maps");
    }

    @Override
    public TestResult run() {
        TestClass result = BJSL.parseJson("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279,\n  \"subObject\" : {\n    \"subFloat\" : 31.5,\n    \"stringArray\" : [\n      \"element 1\",\n      \"element 2\",\n      \"element 3\",\n      \"element 4\"\n    ],\n    \"longList\" : [\n      25153,\n      351,\n      36886942642,\n      293579269246,\n      3926426\n    ],\n    \"subSubObjectList\" : [\n      {\n        \"x\" : 5\n      },\n      {\n        \"x\" : 13\n      },\n      {\n        \"x\" : 24\n      }\n    ],\n    \"booleanMap\" : {\n      \"element1\" : false,\n      \"element2\" : true,\n      \"element3\" : true,\n      \"element4\" : true\n    },\n    \"subSubObjectMap\" : {\n      \"element1\" : {\n        \"x\" : 13\n      },\n      \"element2\" : {\n        \"x\" : 27\n      },\n      \"element3\" : {\n        \"x\" : 82\n      },\n      \"element4\" : {\n        \"x\" : 97\n      }\n    }\n  }\n}", TestClass.class);

        LinkedHashMap<String, Boolean> booleanMap = new LinkedHashMap<String, Boolean>();
        booleanMap.put("element1", false);
        booleanMap.put("element2", true);
        booleanMap.put("element3", true);
        booleanMap.put("element4", true);

        LinkedHashMap<String, TestClass.SubTestClass.SubSubTestClass> subSubObjectMap = new LinkedHashMap<String, TestClass.SubTestClass.SubSubTestClass>();
        subSubObjectMap.put("element1", new TestClass.SubTestClass.SubSubTestClass(13));
        subSubObjectMap.put("element2", new TestClass.SubTestClass.SubSubTestClass(27));
        subSubObjectMap.put("element3", new TestClass.SubTestClass.SubSubTestClass(82));
        subSubObjectMap.put("element4", new TestClass.SubTestClass.SubSubTestClass(97));

        if (result != null && result.pubString.equals("aCoolString") && result.privString.equals("aCoolerString") && result.anInt == 57 && result.aLong == 38689269265279L && result.subObject != null && result.subObject.subFloat == 31.5f && Arrays.equals(result.subObject.stringArray, new String[] { "element 1", "element 2", "element 3", "element 4" }) && result.subObject.longList.equals(Arrays.asList(25153L, 351L, 36886942642L, 293579269246L, 3926426L)) && result.subObject.subSubObjectList.equals(Arrays.asList(new TestClass.SubTestClass.SubSubTestClass(5), new TestClass.SubTestClass.SubSubTestClass(13), new TestClass.SubTestClass.SubSubTestClass(24))) && result.subObject.booleanMap.equals(booleanMap) && result.subObject.subSubObjectMap.equals(subSubObjectMap)) {
            return new TestResult(TestResult.Status.SUCCEEDED, result);
        } else {
            return new TestResult(TestResult.Status.FAILED, result);
        }
    }
}