package io.github.kale_ko.bjsl;

import io.github.kale_ko.bjsl.json.JsonElement;

public class BJSL {
    public JsonElement parseJson(String json) {
        if (json == null) {
            throw new NullPointerException("\"json\" can not be null");
        }

        json = json.trim();

        if (json.startsWith("{") && json.endsWith("}")) {

        } else if (json.startsWith("[") && json.endsWith("]")) {

        }

        return null;
    }
}