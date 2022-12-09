package io.github.kale_ko.bjsl.parsers;

import io.github.kale_ko.bjsl.elements.ParsedElement;

public class JsonParser {
    public static ParsedElement parse(String data) {
        if (data == null) {
            throw new NullPointerException("\"data\" can not be null");
        }

        data = data.trim();

        if (data.startsWith("{") && data.endsWith("}")) {

        } else if (data.startsWith("[") && data.endsWith("]")) {

        }

        return null;
    }

    public static String stringify(ParsedElement element) {
        if (element == null) {
            throw new NullPointerException("\"element\" can not be null");
        }

        return null;
    }
}