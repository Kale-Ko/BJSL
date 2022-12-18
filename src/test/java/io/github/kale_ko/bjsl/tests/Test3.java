package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;

public class Test3 extends Test {
    @SuppressWarnings("unused")
    protected static class TestClass {
        public String pubString = "aCoolString";
        private String privString = "aCoolerString";

        public int anInt = 57;
        public Long aLong = 38689269265279L;
    }

    public Test3() {
        super("1 layer static object to string");
    }

    @Override
    public Object run() {
        String result = BJSL.stringifyJson(new TestClass());

        if (result.equals("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279\n}")) {
            return true;
        } else {
            return result;
        }
    }
}