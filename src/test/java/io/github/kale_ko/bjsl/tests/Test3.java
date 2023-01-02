package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.TestResult;

public class Test3 extends Test {
    @SuppressWarnings("unused")
    public static class TestClass {
        public String pubString = "aCoolString";
        private String privString = "aCoolerString";

        public int anInt = 57;
        public Long aLong = 38689269265279L;
    }

    public Test3() {
        super("Single layer static object to string");
    }

    @Override
    public TestResult run() {
        String result = BJSL.stringifyJson(new TestClass());

        if (result.equals("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279\n}")) {
            return new TestResult(TestResult.Status.SUCCEEDED, result);
        } else {
            return new TestResult(TestResult.Status.FAILED, result);
        }
    }
}