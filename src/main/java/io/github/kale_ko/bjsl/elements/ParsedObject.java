package io.github.kale_ko.bjsl.elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for an ordered map used to represent an Object in most data formats
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParsedObject extends ParsedElement {
    /**
     * The map this object stores
     *
     * @since 1.0.0
     */
    protected final Map<String, ParsedElement> object;

    /**
     * Create a new {@link ParsedObject}
     *
     * @since 1.0.0
     */
    protected ParsedObject() {
        this(new LinkedHashMap<>());
    }

    /**
     * Create a new {@link ParsedObject} from an object
     *
     * @param object The object to use
     *
     * @since 1.0.0
     */
    protected ParsedObject(Map<String, ParsedElement> object) {
        this.object = object;
    }

    /**
     * Get the amount of key/value pairs this object stores
     *
     * @return The amount of key/value pairs this object stores
     *
     * @since 1.0.0
     */
    public int getSize() {
        return this.object.size();
    }

    /**
     * Get a list of all the key/value pairs this object stores
     * <p>
     * Note: Returns a copy of the list
     *
     * @return A list of all the key/value pairs this object stores
     *
     * @since 1.0.0
     */
    public List<java.util.Map.Entry<String, ParsedElement>> getEntries() {
        return List.copyOf(this.object.entrySet());
    }

    /**
     * Get a list of all the keys this object stores
     * <p>
     * Note: Returns a copy of the list
     *
     * @return A list of all the keys this object stores
     *
     * @since 1.0.0
     */
    public List<String> getKeys() {
        return List.copyOf(this.object.keySet());
    }

    /**
     * Get a list of all the values this object stores
     * <p>
     * Note: Returns a copy of the list
     *
     * @return A list of all the values this object stores
     *
     * @since 1.0.0
     */
    public List<ParsedElement> getValues() {
        return List.copyOf(this.object.values());
    }

    /**
     * Check if this object stores a certain key
     *
     * @param key The key to check
     *
     * @return If this object stores a certain key
     *
     * @since 1.0.0
     */
    public boolean has(String key) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }

        return this.object.containsKey(key);
    }

    /**
     * Get the value of a certain key this object stores
     *
     * @param key The key to get
     *
     * @return The value of a certain key this object stores
     *
     * @since 1.0.0
     */
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

    /**
     * Set the value of a certain key this object stores
     *
     * @param key   The key to set
     * @param value The value to set
     *
     * @since 1.0.0
     */
    public void set(String key, ParsedElement value) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }

        this.object.remove(key);
        this.object.put(key, value);
    }

    /**
     * Remove a certain key/value pair this object stores
     *
     * @param key The key to remove
     *
     * @since 1.0.0
     */
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

    /**
     * Create a new empty {@link ParsedObject}
     *
     * @return A new empty {@link ParsedObject}
     *
     * @since 1.0.0
     */
    public static ParsedObject create() {
        return new ParsedObject();
    }

    /**
     * Create a new {@link ParsedObject} that will be populated with the passed map
     *
     * @param object The map to use to populate the new object
     *
     * @return A new {@link ParsedObject} populated with the passed map
     *
     * @since 1.0.0
     */
    public static ParsedObject from(Map<String, ParsedElement> object) {
        if (object == null) {
            throw new NullPointerException("Object can not be null");
        }

        return new ParsedObject(new LinkedHashMap<>(object));
    }
}