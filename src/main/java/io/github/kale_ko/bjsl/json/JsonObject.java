package io.github.kale_ko.bjsl.json;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class JsonObject extends JsonElement {
    private JsonObject() {
        this(new TreeMap<String, JsonElement>());
    }

    private JsonObject(SortedMap<String, JsonElement> object) {
        super(object, null, null);
    }

    public int getSize() {
        return this.object.size();
    }

    public List<String> getKeys() {
        return new ArrayList<String>(this.object.keySet());
    }

    public List<JsonElement> getValues() {
        return new ArrayList<JsonElement>(this.object.values());
    }

    public Boolean has(String key) {
        if (key == null) {
            throw new NullPointerException("\"key\" can not be null");
        }

        return this.object.containsKey(key);
    }

    public JsonElement get(String key) {
        if (key == null) {
            throw new NullPointerException("\"key\" can not be null");
        }

        if (this.object.containsKey(key)) {
            return this.object.get(key);
        } else {
            throw new NullPointerException("\"key\" does not exist on this object");
        }
    }

    public void set(String key, JsonElement value) {
        if (key == null) {
            throw new NullPointerException("\"key\" can not be null");
        }

        if (this.object.containsKey(key)) {
            this.object.remove(key);
        }
        this.object.put(key, value);
    }

    public void remove(String key) {
        if (key == null) {
            throw new NullPointerException("\"key\" can not be null");
        }

        if (this.object.containsKey(key)) {
            this.object.remove(key);
        } else {
            throw new NullPointerException("\"key\" does not exist on this object");
        }
    }

    public static JsonObject create() {
        return new JsonObject();
    }

    public static JsonObject from(SortedMap<String, JsonElement> object) {
        return new JsonObject(object);
    }
}