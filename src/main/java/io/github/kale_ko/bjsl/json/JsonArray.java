package io.github.kale_ko.bjsl.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonElement {
    private JsonArray() {
        this(new ArrayList<JsonElement>());
    }

    private JsonArray(List<JsonElement> array) {
        super(null, array, null);
    }

    public int getSize() {
        return this.array.size();
    }

    public List<JsonElement> getValues() {
        return new ArrayList<JsonElement>(this.array);
    }

    public JsonElement get(int index) {
        if (index < this.array.size()) {
            return this.array.get(index);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public void set(int index, JsonElement value) {
        if (index < this.array.size()) {
            this.array.set(index, value);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public void remove(int index) {
        if (index < this.array.size()) {
            this.array.remove(index);
        } else {
            throw new IndexOutOfBoundsException("\"index\" is out of bounds for length \"" + this.array.size() + "\"");
        }
    }

    public static JsonArray create() {
        return new JsonArray();
    }

    public static JsonArray from(List<JsonElement> array) {
        return new JsonArray(array);
    }
}