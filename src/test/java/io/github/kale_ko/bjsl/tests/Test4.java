package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.TestResult;

public class Test4 extends Test {
    @SuppressWarnings("unused")
    public class TestClass {
        public String pubString = "aCoolString";
        private String privString = "aCoolerString";

        public int anInt = 57;
        public Long aLong = 38689269265279L;
    }

    public Test4() {
        super("Single layer object to string");
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