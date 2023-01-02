package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;
import io.github.kale_ko.bjsl.TestResult;

public class Test8 extends Test {
    public static class TestClass {
        public String pubString = "anEpicString";
        private String privString = "aMoreEpicString";

        public int anInt = 35;
        public Long aLong = 486295979489289L;
    }

    public Test8() {
        super("Object initialization");
    }

    @Override
    public TestResult run() {
        TestClass result = BJSL.parseJson("{ }", TestClass.class);

        if (result != null && result.pubString.equals("anEpicString") && result.privString.equals("aMoreEpicString") && result.anInt == 35 && result.aLong == 486295979489289L) {
            return new TestResult(TestResult.Status.SUCCEEDED, result);
        } else {
            return new TestResult(TestResult.Status.FAILED, result);
        }
    }
}