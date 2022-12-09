package io.github.kale_ko.bjsl;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.parsers.JsonParser;

public class BJSL {
    public static ParsedElement parseJson(String json) {
        return JsonParser.parse(json);
    }

    public static String stringifyJson(ParsedElement json) {
        return JsonParser.stringify(json);
    }
}