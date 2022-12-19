package io.github.kale_ko.bjsl.tests;

import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.Test;

public class Test8 extends Test {
    protected static class TestClass {
        public String pubString = "aCoolString";
        private String privString = "aCoolerString";

        public int anInt = 57;
        public Long aLong = 38689269265279L;
    }

    public Test8() {
        super("Single layer string to object");
    }

    @Override
    public Object run() {
        TestClass result = BJSL.parseJson("{\n  \"pubString\" : \"aCoolString\",\n  \"privString\" : \"aCoolerString\",\n  \"anInt\" : 57,\n  \"aLong\" : 38689269265279\n}", TestClass.class);

        if (result != null && result.pubString.equals("aCoolString") && result.privString.equals("aCoolerString") && result.anInt == 57 && result.aLong == 38689269265279L) {
            return true;
        } else {
            return false;
        }
    }
}