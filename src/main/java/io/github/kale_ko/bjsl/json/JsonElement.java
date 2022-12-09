package io.github.kale_ko.bjsl.json;

import java.util.List;
import java.util.SortedMap;

public class JsonElement {
    protected SortedMap<String, JsonElement> object;
    protected List<JsonElement> array;
    protected Object primitive;

    protected JsonElement(SortedMap<String, JsonElement> map, List<JsonElement> array, Object primitive) {
        if (map == null && array == null && primitive == null) {
            throw new NullPointerException("One of \"map\", \"array\", or \"primitive\" must not be null");
        }

        this.object = map;
        this.array = array;
        this.primitive = primitive;
    }

    public boolean isJsonObject() {
        return this.object != null;
    }

    public boolean isJsonArray() {
        return this.array != null;
    }

    public boolean isJsonPrimitive() {
        return this.primitive != null;
    }

    public JsonObject asJsonObject() {
        return JsonObject.from(this.object);
    }

    public JsonArray asJsonArray() {
        return JsonArray.from(this.array);
    }

    public JsonPrimitive asJsonPrimitive() {
        return JsonPrimitive.from(this.primitive);
    }
}