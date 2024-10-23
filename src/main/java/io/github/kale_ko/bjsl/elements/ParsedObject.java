package io.github.kale_ko.bjsl.elements;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A wrapper for an ordered map used to represent an Object in most data formats
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public class ParsedObject extends ParsedElement {
    /**
     * The map this object stores
     *
     * @since 1.0.0
     */
    final @NotNull Map<String, ParsedElement> object;

    /**
     * Create a new {@link ParsedObject} from an object
     *
     * @param object The object to use
     *
     * @since 1.0.0
     */
    ParsedObject(@NotNull Map<String, ParsedElement> object) {
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
    public @NotNull @Unmodifiable Collection<Map.Entry<String, ParsedElement>> getEntries() {
        return Collections.unmodifiableCollection(this.object.entrySet());
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
    public @NotNull @Unmodifiable Collection<String> getKeys() {
        return Collections.unmodifiableCollection(this.object.keySet());
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
    public @NotNull @Unmodifiable Collection<ParsedElement> getValues() {
        return Collections.unmodifiableCollection(this.object.values());
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
    public boolean has(@NotNull String key) {
        return this.object.containsKey(key);
    }

    /**
     * Get the value of a certain key this object stores
     *
     * @param key The key to get
     *
     * @return The value of a certain key this object stores
     *
     * @throws java.lang.NullPointerException If there is no value associated with the key
     * @since 1.0.0
     */
    public @NotNull ParsedElement get(@NotNull String key) {
        if (!this.object.containsKey(key)) {
            throw new NullPointerException("Key \"" + key + "\" does not exist on this object");
        }
        return this.object.get(key);
    }

    /**
     * Set the value of a certain key this object stores
     *
     * @param key   The key to set
     * @param value The value to set
     *
     * @since 1.0.0
     */
    public void set(@NotNull String key, @NotNull ParsedElement value) {
        this.object.put(key, value);
    }

    /**
     * Remove a certain key/value pair this object stores
     *
     * @param key The key to remove
     *
     * @since 1.0.0
     */
    public void remove(@NotNull String key) {
        this.object.remove(key);
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "[object=" + this.object + "]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return ((ParsedObject) obj).object.equals(this.object);
    }

    @Override
    public int hashCode() {
        return this.object.hashCode();
    }

    /**
     * Create a new empty {@link ParsedObject}
     *
     * @return A new empty {@link ParsedObject}
     *
     * @since 1.0.0
     */
    public static @NotNull ParsedObject create() {
        return new ParsedObject(new LinkedHashMap<>());
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
    public static @NotNull ParsedObject from(@NotNull Map<String, ParsedElement> object) {
        return new ParsedObject(new LinkedHashMap<>(object));
    }
}