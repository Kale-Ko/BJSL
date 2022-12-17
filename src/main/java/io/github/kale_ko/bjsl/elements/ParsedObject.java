package io.github.kale_ko.bjsl.elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParsedObject extends ParsedElement {
    private ParsedObject() {
        this(new LinkedHashMap<String, ParsedElement>());
    }

    private ParsedObject(LinkedHashMap<String, ParsedElement> object) {
        super(object, null, null);
    }

    public int getSize() {
        return this.object.size();
    }

    public List<Map.Entry<String, ParsedElement>> getEntries() {
        return new ArrayList<Map.Entry<String, ParsedElement>>(this.object.entrySet());
    }

    public List<String> getKeys() {
        return new ArrayList<String>(this.object.keySet());
    }

    public List<ParsedElement> getValues() {
        return new ArrayList<ParsedElement>(this.object.values());
    }

    public Boolean has(String key) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }

        return this.object.containsKey(key);
    }

    public ParsedElement get(String key) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }

        if (this.object.containsKey(key)) {
            return this.object.get(key);
        } else {
            throw new NullPointerException("Key does not exist on this object");
        }
    }

    public void set(String key, ParsedElement value) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        if (this.object.containsKey(key)) {
            this.object.remove(key);
        }
        this.object.put(key, value);
    }

    public void remove(String key) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }

        if (this.object.containsKey(key)) {
            this.object.remove(key);
        } else {
            throw new NullPointerException("Key does not exist on this object");
        }
    }

    public static ParsedObject create() {
        return new ParsedObject();
    }

    public static ParsedObject from(Map<String, ParsedElement> object) {
        if (object == null) {
            throw new NullPointerException("Object can not be null");
        }

        return new ParsedObject(new LinkedHashMap<String, ParsedElement>(object));
    }
}