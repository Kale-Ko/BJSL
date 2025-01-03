package io.github.kale_ko.bjsl.processor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.parsers.exception.InvalidTypeException;
import io.github.kale_ko.bjsl.processor.annotations.AlwaysSerialize;
import io.github.kale_ko.bjsl.processor.annotations.NeverSerialize;
import io.github.kale_ko.bjsl.processor.annotations.Rename;
import io.github.kale_ko.bjsl.processor.conditions.ExpectGreaterThan;
import io.github.kale_ko.bjsl.processor.conditions.ExpectIsNull;
import io.github.kale_ko.bjsl.processor.conditions.ExpectLessThan;
import io.github.kale_ko.bjsl.processor.conditions.ExpectNotNull;
import io.github.kale_ko.bjsl.processor.exception.*;
import io.github.kale_ko.bjsl.processor.reflection.InitializationUtil;
import io.github.kale_ko.bjsl.processor.reflection.TypeUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An Object processor for mapping elements to objects and objects to elements
 * <p>
 * Also has options for reducing the amount of output keys
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public class ObjectProcessor {
    /**
     * Weather null values should be ignored when serializing maps and objects
     *
     * @since 1.0.0
     */
    protected final boolean ignoreNulls;

    /**
     * Weather null values should be ignored when serializing lists and arrays
     *
     * @since 4.0.0
     */
    protected final boolean ignoreArrayNulls;

    /**
     * Weather empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
     *
     * @since 1.0.0
     */
    protected final boolean ignoreEmptyObjects;

    /**
     * Weather default values should be ignored when serializing maps and objects
     * <p>
     * These are gotten from the value in a new instance of the object
     *
     * @since 1.0.0
     */
    protected final boolean ignoreDefaults;

    /**
     * Weather checks for enum names should be case-sensitive
     *
     * @since 1.0.0
     */
    protected final boolean caseSensitiveEnums;

    /**
     * A map of object types to type processors
     *
     * @since 1.0.0
     */
    protected final @NotNull Map<JavaType, TypeProcessor> typeProcessors;

    /**
     * Create a new ObjectProcessor using certain factories
     *
     * @param ignoreNulls        Weather null values should be ignored when serializing maps and objects
     * @param ignoreArrayNulls   Weather null values should be ignored when serializing lists and arrays
     * @param ignoreEmptyObjects Weather empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
     * @param ignoreDefaults     Weather default values should be ignored when serializing maps and objects
     * @param caseSensitiveEnums Weather checks for enum names should be case-sensitive
     * @param typeProcessors     A map of object types to type processors
     *
     * @since 1.0.0
     */
    protected ObjectProcessor(boolean ignoreNulls, boolean ignoreArrayNulls, boolean ignoreEmptyObjects, boolean ignoreDefaults, boolean caseSensitiveEnums, @NotNull Map<JavaType, TypeProcessor> typeProcessors) {
        this.ignoreNulls = ignoreNulls;
        this.ignoreArrayNulls = ignoreArrayNulls;
        this.ignoreEmptyObjects = ignoreEmptyObjects;
        this.ignoreDefaults = ignoreDefaults;

        this.caseSensitiveEnums = caseSensitiveEnums;

        this.typeProcessors = typeProcessors;
    }

    /**
     * A builder class for creating new {@link ObjectProcessor}s
     *
     * @version 2.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * Weather or not null values should be ignored when serializing maps and objects
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean ignoreNulls = false;

        /**
         * Weather null values should be ignored when serializing lists and arrays
         * <p>
         * Default is true
         *
         * @since 4.0.0
         */
        protected boolean ignoreArrayNulls = false;

        /**
         * Weather or not empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean ignoreEmptyObjects = false;

        /**
         * Weather or not default values should be ignored when serializing maps and objects
         * <p>
         * These are gotten from the value in a new instance of the object
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean ignoreDefaults = false;

        /**
         * Weather checks for enum names should be case-sensitive
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean caseSensitiveEnums = false;

        /**
         * A map of object types to type processors
         *
         * @since 1.0.0
         */
        protected final @NotNull Map<JavaType, TypeProcessor> typeProcessors = new HashMap<>();

        /**
         * The options for the default type processors, null means disable the default type processors
         *
         * @since 2.0.0
         */
        protected @Nullable DefaultTypeProcessors.Options defaultTypeProcessorsOptions = DefaultTypeProcessors.Options.DEFAULT;

        /**
         * Create a new {@link ObjectProcessor} builder
         *
         * @since 1.0.0
         */
        public Builder() {
        }

        /**
         * Get weather or not null values should be ignored when serializing maps and objects
         * <p>
         * Default is false
         *
         * @return Weather or not null values should be ignored when serializing maps and objects
         *
         * @since 1.0.0
         */
        public boolean getIgnoreNulls() {
            return this.ignoreNulls;
        }

        /**
         * Set weather or not null values should be ignored when serializing maps and objects
         * <p>
         * Default is false
         *
         * @param value Weather or not null values should be ignored when serializing maps and objects
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setIgnoreNulls(boolean value) {
            this.ignoreNulls = value;

            return this;
        }

        /**
         * Get weather or not null values should be ignored when serializing lists and arrays
         * <p>
         * Default is false
         *
         * @return Weather or not null values should be ignored when serializing lists and arrays
         *
         * @since 4.0.0
         */
        public boolean getIgnoreArrayNulls() {
            return this.ignoreArrayNulls;
        }

        /**
         * Set weather or not null values should be ignored when serializing lists and arrays
         * <p>
         * Default is false
         *
         * @param value Weather or not null values should be ignored when serializing lists and arrays
         *
         * @return Self for chaining
         *
         * @since 4.0.0
         */
        public @NotNull Builder setIgnoreArrayNulls(boolean value) {
            this.ignoreArrayNulls = value;

            return this;
        }

        /**
         * Get weather or not empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
         * <p>
         * Default is false
         *
         * @return Weather or not empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
         *
         * @since 1.0.0
         */
        public boolean getIgnoreEmptyObjects() {
            return this.ignoreEmptyObjects;
        }

        /**
         * Set weather or not empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
         * <p>
         * Default is false
         *
         * @param value Weather or not empty objects (Objects with a size of 0) should be ignored when serializing maps and objects
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setIgnoreEmptyObjects(boolean value) {
            this.ignoreEmptyObjects = value;

            return this;
        }

        /**
         * Get weather or not default values should be ignored when serializing maps and objects
         * <p>
         * These are gotten from the value in a new instance of the object
         * <p>
         * Default is false
         *
         * @return Weather or not default values should be ignored when serializing maps and objects
         *
         * @since 1.0.0
         */
        public boolean getIgnoreDefaults() {
            return this.ignoreDefaults;
        }

        /**
         * Set weather or not default values should be ignored when serializing maps and objects
         * <p>
         * These are gotten from the value in a new instance of the object
         * <p>
         * Default is false
         *
         * @param value Weather or not default values should be ignored when serializing maps and objects
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setIgnoreDefaults(boolean value) {
            this.ignoreDefaults = value;

            return this;
        }

        /**
         * Get weather checks for enum names should be case-sensitive
         * <p>
         * Default is false
         *
         * @return Weather checks for enum names should be case-sensitive
         *
         * @since 1.0.0
         */
        public boolean getCaseSensitiveEnums() {
            return this.caseSensitiveEnums;
        }

        /**
         * Set weather checks for enum names should be case-sensitive
         * <p>
         * Default is false
         *
         * @param value Weather checks for enum names should be case-sensitive
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setCaseSensitiveEnums(boolean value) {
            this.caseSensitiveEnums = value;

            return this;
        }

        /**
         * Get all the type processors created
         *
         * @return All the type processors created
         *
         * @since 1.0.0
         */
        public @NotNull Map<JavaType, TypeProcessor> getTypeProcessors() {
            return this.typeProcessors;
        }

        /**
         * Check if a type processor exists for this class
         * <p>
         * Calls {@link #hasTypeProcessor(JavaType)}
         *
         * @param clazz The class to check for
         *
         * @return If a type processor for clazz exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(@NotNull Class<?> clazz) {
            return this.hasTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }));
        }

        /**
         * Check if a type processor exists for this class or a super class
         * <p>
         * Calls {@link #hasTypeProcessor(JavaType)}
         *
         * @param clazz The class to check for
         *
         * @return If a type processor for clazz exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeOrSupertypeProcessor(@NotNull Class<?> clazz) {
            return this.hasTypeOrSupertypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }));
        }

        /**
         * Check if a type processor exists for this class
         * <p>
         * Calls {@link #hasTypeProcessor(JavaType)}
         *
         * @param type The type to check for
         *
         * @return If a type processor for type exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(@NotNull Type type) {
            return this.hasTypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Check if a type processor exists for this class or a super class
         * <p>
         * Calls {@link #hasTypeProcessor(JavaType)}
         *
         * @param type The type to check for
         *
         * @return If a type processor for type exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeOrSupertypeProcessor(@NotNull Type type) {
            return this.hasTypeOrSupertypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Check if a type processor exists for this class
         *
         * @param type The type to check for
         *
         * @return If a type processor for type exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(@NotNull JavaType type) {
            return typeProcessors.entrySet().stream().anyMatch(entry -> TypeUtils.isType(type, entry.getKey()));
        }

        /**
         * Check if a type processor exists for this class or a super class
         *
         * @param type The type to check for
         *
         * @return If a type processor for type exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeOrSupertypeProcessor(@NotNull JavaType type) {
            return typeProcessors.entrySet().stream().anyMatch(entry -> TypeUtils.isTypeOrSuperTypeOf(type, entry.getKey()));
        }

        /**
         * Get a type processor for this class
         * <p>
         * Calls {@link #getTypeProcessor(JavaType)}
         *
         * @param clazz The class to get
         *
         * @return The type processor for class
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeProcessor(@NotNull Class<?> clazz) {
            return this.getTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }));
        }

        /**
         * Get a type processor for this class or a superclass
         * <p>
         * Calls {@link #getTypeProcessor(JavaType)}
         *
         * @param clazz The class to get
         *
         * @return The type processor for class
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeOrSupertypeProcessor(@NotNull Class<?> clazz) {
            return this.getTypeOrSupertypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }));
        }

        /**
         * Get a type processor for this class
         * <p>
         * Calls {@link #getTypeProcessor(JavaType)}
         *
         * @param type The type to get
         *
         * @return The type processor for type
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeProcessor(@NotNull Type type) {
            return this.getTypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Get a type processor for this class or a superclass
         * <p>
         * Calls {@link #getTypeProcessor(JavaType)}
         *
         * @param type The type to get
         *
         * @return The type processor for type
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeOrSupertypeProcessor(@NotNull Type type) {
            return this.getTypeOrSupertypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Get a type processor for this class
         *
         * @param type The type to get
         *
         * @return The type processor for type
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeProcessor(@NotNull JavaType type) {
            return typeProcessors.entrySet().stream().filter(entry -> TypeUtils.isType(type, entry.getKey())).findFirst().orElseThrow().getValue();
        }

        /**
         * Get a type processor for this class or a superclass
         *
         * @param type The type to get
         *
         * @return The type processor for type
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeOrSupertypeProcessor(@NotNull JavaType type) {
            return typeProcessors.entrySet().stream().filter(entry -> TypeUtils.isTypeOrSuperTypeOf(type, entry.getKey())).sorted((entryA, entryB) -> TypeUtils.sortDistance(type, entryA.getKey(), entryB.getKey())).findFirst().orElseThrow().getValue();
        }

        /**
         * Create a type processor
         * <p>
         * Calls {@link #createTypeProcessor(JavaType, TypeProcessor)}
         *
         * @param clazz The class to create
         * @param value The type processor to use
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder createTypeProcessor(@NotNull Class<?> clazz, @NotNull TypeProcessor value) {
            return this.createTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }), value);
        }

        /**
         * Create a type processor
         * <p>
         * Calls {@link #createTypeProcessor(JavaType, TypeProcessor)}
         *
         * @param type  The type to create
         * @param value The type processor to use
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder createTypeProcessor(@NotNull Type type, @NotNull TypeProcessor value) {
            return this.createTypeProcessor(TypeFactory.defaultInstance().constructType(type), value);
        }

        /**
         * Create a type processor
         *
         * @param type  The type to create
         * @param value The type processor to use
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder createTypeProcessor(@NotNull JavaType type, @NotNull TypeProcessor value) {
            if (this.hasTypeProcessor(type)) {
                throw new NullPointerException("A type processor already exists for class \"" + type.getRawClass().getSimpleName() + "\"");
            }

            this.typeProcessors.put(type, value);

            return this;
        }

        /**
         * Remove a type processor
         * <p>
         * Calls {@link #removeTypeProcessor(JavaType)}
         *
         * @param clazz The class to remove
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder removeTypeProcessor(@NotNull Class<?> clazz) {
            return this.removeTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }));
        }

        /**
         * Remove a type processor
         * <p>
         * Calls {@link #removeTypeProcessor(JavaType)}
         *
         * @param type The type to remove
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder removeTypeProcessor(@NotNull Type type) {
            return this.removeTypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Remove a type processor
         *
         * @param type The type to remove
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder removeTypeProcessor(@NotNull JavaType type) {
            if (!this.hasTypeProcessor(type)) {
                throw new NullPointerException("A type processor does not exists for class \"" + type.getRawClass().getSimpleName() + "\"");
            }

            this.typeProcessors.remove(type);

            return this;
        }

        /**
         * Get the options for the default type processors, null means disable the default type processors
         *
         * @return The options for the default type processors, null means disable the default type processors
         *
         * @since 2.0.0
         */
        public @Nullable DefaultTypeProcessors.Options getDefaultTypeProcessorsOptions() {
            return this.defaultTypeProcessorsOptions;
        }

        /**
         * Set the options for the default type processors, null means disable the default type processors
         *
         * @param value The options for the default type processors, null means disable the default type processors
         *
         * @return Self for chaining
         *
         * @since 2.0.0
         */
        public @NotNull Builder setDefaultTypeProcessorsOptions(@Nullable DefaultTypeProcessors.Options value) {
            this.defaultTypeProcessorsOptions = value;

            return this;
        }

        /**
         * Uses the current settings to build a new {@link ObjectProcessor}
         *
         * @return A new {@link ObjectProcessor} instance
         *
         * @since 1.0.0
         */
        public @NotNull ObjectProcessor build() {
            if (this.defaultTypeProcessorsOptions != null) {
                DefaultTypeProcessors.register(this);
            }

            return new ObjectProcessor(this.ignoreNulls, this.ignoreArrayNulls, this.ignoreEmptyObjects, this.ignoreDefaults, this.caseSensitiveEnums, this.typeProcessors);
        }
    }

    /**
     * Maps this element into an Object
     * <p>
     * Calls {@link #toObject(ParsedElement, JavaType)}
     *
     * @param element The element to map
     * @param clazz   The object type to map to
     * @param <T>     The object type to map to
     *
     * @return A new Object of passed type with the values of element
     *
     * @throws io.github.kale_ko.bjsl.processor.exception.ProcessorException If there is an exception while processing
     * @since 1.0.0h
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable T toObject(@NotNull ParsedElement element, @NotNull Class<T> clazz) {
        return (T) toObject(element, TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] { }));
    }

    /**
     * Maps this element into an Object
     * <p>
     * Calls {@link #toObject(ParsedElement, JavaType)}
     *
     * @param element The element to map
     * @param type    The object type to map to
     *
     * @return A new Object of passed type with the values of element
     *
     * @throws io.github.kale_ko.bjsl.processor.exception.ProcessorException If there is an exception while processing
     * @since 1.0.0
     */
    public @Nullable Object toObject(@NotNull ParsedElement element, @NotNull Type type) {
        return toObject(element, TypeFactory.defaultInstance().constructType(type));
    }

    /**
     * Maps this element into an Object
     *
     * @param element The element to map
     * @param type    The object type to map to
     *
     * @return A new Object of passed type with the values of element
     *
     * @throws io.github.kale_ko.bjsl.processor.exception.ProcessorException If there is an exception while processing
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public @Nullable Object toObject(@NotNull ParsedElement element, @NotNull JavaType type) {
        try {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            {
                Optional<TypeProcessor> typeProcessor = typeProcessors.entrySet().stream().filter(entry -> TypeUtils.isTypeOrSuperTypeOf(type, entry.getKey())).sorted((entryA, entryB) -> TypeUtils.sortDistance(type, entryA.getKey(), entryB.getKey())).map(Map.Entry::getValue).findFirst();
                if (typeProcessor.isPresent()) {
                    return typeProcessor.get().toObject(element);
                }
            }

            if (element instanceof ParsedPrimitive) {
                if (type.getRawClass().isEnum()) {
                    if (element.asPrimitive().isString()) {
                        for (Object value : type.getRawClass().getEnumConstants()) {
                            if ((!caseSensitiveEnums && ((Enum<?>) value).name().equalsIgnoreCase(element.asPrimitive().asString())) || (caseSensitiveEnums && ((Enum<?>) value).name().equals(element.asPrimitive().asString()))) {
                                return value;
                            }
                        }

                        if (BJSL.getLogger() != null) {
                            BJSL.getLogger().warning("Unknown enum value \"" + element.asPrimitive().asString() + "\" for type \"" + type.getRawClass().getSimpleName() + "\"");
                        }

                        return null;
                    } else {
                        throw new EnumExpectedException(type.getRawClass());
                    }
                } else {
                    Object object = element.asPrimitive().get();

                    if (object == null) {
                        return null;
                    } else if (type.getRawClass() == String.class) {
                        return object.toString();
                    } else if (type.getRawClass() == Byte.class || type.getRawClass() == byte.class) {
                        if (object.getClass() == String.class) {
                            return Byte.parseByte((String) object);
                        } else if (object.getClass() == Character.class) {
                            return (byte) (char) object;
                        } else if (object.getClass() == Short.class) {
                            return (byte) (short) object;
                        } else if (object.getClass() == Integer.class) {
                            return (byte) (int) object;
                        } else if (object.getClass() == Long.class) {
                            return (byte) (long) object;
                        } else if (object.getClass() == Float.class) {
                            return (byte) (float) object;
                        } else if (object.getClass() == Double.class) {
                            return (byte) (double) object;
                        } else {
                            return (byte) object;
                        }
                    } else if (type.getRawClass() == Character.class || type.getRawClass() == char.class) {
                        if (object.getClass() == String.class) {
                            return ((String) object).charAt(0);
                        } else if (object.getClass() == Byte.class) {
                            return (char) (byte) object;
                        } else if (object.getClass() == Short.class) {
                            return (char) (short) object;
                        } else if (object.getClass() == Integer.class) {
                            return (char) (int) object;
                        } else if (object.getClass() == Long.class) {
                            return (char) (long) object;
                        } else if (object.getClass() == Float.class) {
                            return (char) (float) object;
                        } else if (object.getClass() == Double.class) {
                            return (char) (double) object;
                        } else {
                            return (char) object;
                        }
                    } else if (type.getRawClass() == Short.class || type.getRawClass() == short.class) {
                        if (object.getClass() == String.class) {
                            return Short.parseShort((String) object);
                        } else if (object.getClass() == Byte.class) {
                            return (short) (byte) object;
                        } else if (object.getClass() == Character.class) {
                            return (short) (char) object;
                        } else if (object.getClass() == Integer.class) {
                            return (short) (int) object;
                        } else if (object.getClass() == Long.class) {
                            return (short) (long) object;
                        } else if (object.getClass() == Float.class) {
                            return (short) (float) object;
                        } else if (object.getClass() == Double.class) {
                            return (short) (double) object;
                        } else {
                            return (short) object;
                        }
                    } else if (type.getRawClass() == Integer.class || type.getRawClass() == int.class) {
                        if (object.getClass() == String.class) {
                            return Integer.parseInt((String) object);
                        } else if (object.getClass() == Byte.class) {
                            return (int) (byte) object;
                        } else if (object.getClass() == Character.class) {
                            return (int) (char) object;
                        } else if (object.getClass() == Short.class) {
                            return (int) (short) object;
                        } else if (object.getClass() == Long.class) {
                            return (int) (long) object;
                        } else if (object.getClass() == Float.class) {
                            return (int) (float) object;
                        } else if (object.getClass() == Double.class) {
                            return (int) (double) object;
                        } else {
                            return (int) object;
                        }
                    } else if (type.getRawClass() == Long.class || type.getRawClass() == long.class) {
                        if (object.getClass() == String.class) {
                            return Long.parseLong((String) object);
                        } else if (object.getClass() == Byte.class) {
                            return (long) (byte) object;
                        } else if (object.getClass() == Character.class) {
                            return (long) (char) object;
                        } else if (object.getClass() == Short.class) {
                            return (long) (short) object;
                        } else if (object.getClass() == Integer.class) {
                            return (long) (int) object;
                        } else if (object.getClass() == Float.class) {
                            return (long) (float) object;
                        } else if (object.getClass() == Double.class) {
                            return (long) (double) object;
                        } else {
                            return (long) object;
                        }
                    } else if (type.getRawClass() == Float.class || type.getRawClass() == float.class) {
                        if (object.getClass() == String.class) {
                            return Float.parseFloat((String) object);
                        } else if (object.getClass() == Byte.class) {
                            return (float) (byte) object;
                        } else if (object.getClass() == Character.class) {
                            return (float) (char) object;
                        } else if (object.getClass() == Short.class) {
                            return (float) (short) object;
                        } else if (object.getClass() == Integer.class) {
                            return (float) (int) object;
                        } else if (object.getClass() == Long.class) {
                            return (float) (long) object;
                        } else if (object.getClass() == Double.class) {
                            return (float) (double) object;
                        } else {
                            return (float) object;
                        }
                    } else if (type.getRawClass() == Double.class || type.getRawClass() == double.class) {
                        if (object.getClass() == String.class) {
                            return Double.parseDouble((String) object);
                        } else if (object.getClass() == Byte.class) {
                            return (double) (byte) object;
                        } else if (object.getClass() == Character.class) {
                            return (double) (char) object;
                        } else if (object.getClass() == Short.class) {
                            return (double) (short) object;
                        } else if (object.getClass() == Integer.class) {
                            return (double) (int) object;
                        } else if (object.getClass() == Long.class) {
                            return (double) (long) object;
                        } else if (object.getClass() == Float.class) {
                            return (double) (float) object;
                        } else {
                            return (double) object;
                        }
                    } else if (type.getRawClass() == Boolean.class || type.getRawClass() == boolean.class) {
                        if (object.getClass() == String.class) {
                            return Boolean.parseBoolean((String) object);
                        } else {
                            return (boolean) object;
                        }
                    } else {
                        if (BJSL.getLogger() != null) {
                            BJSL.getLogger().severe("Something went wrong! Primitive was passed to a non primitive type");
                        }

                        return null;
                    }
                }
            } else if (!type.getRawClass().isAnonymousClass() && !type.getRawClass().isAnnotation()) {
                switch (element) {
                    case ParsedObject parsedObject -> {
                        if (type instanceof MapType) {
                            Map<Object, Object> object;
                            if (!type.getRawClass().isInterface()) {
                                object = (Map<Object, Object>) InitializationUtil.initialize(type.getRawClass());
                            } else {
                                object = InitializationUtil.initialize(LinkedHashMap.class);
                            }

                            for (Map.Entry<String, ParsedElement> entry : element.asObject().getEntries()) {
                                Object subObject = toObject(entry.getValue(), type.getContentType());
                                if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] objects && objects.length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> collection && collection.isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> map && map.isEmpty()))) {
                                    object.put(toObject(ParsedPrimitive.fromString(entry.getKey()), type.getKeyType()), subObject);
                                }
                            }

                            return object;
                        } else if (!type.getRawClass().isInterface()) {
                            Object object = InitializationUtil.initialize(type.getRawClass());

                            List<Field> fields = getFields(object.getClass());
                            for (Field field : fields) {
                                if (!Modifier.isStatic(field.getModifiers()) && (field.canAccess(object) || field.trySetAccessible())) {
                                    boolean shouldSerialize = !(Modifier.isTransient(field.getModifiers()) || field.getName().startsWith("this$"));

                                    String subKey = field.getName();
                                    boolean expect = false;

                                    for (Annotation annotation : field.getDeclaredAnnotations()) {
                                        if (annotation.annotationType() == AlwaysSerialize.class) {
                                            shouldSerialize = true;
                                        } else if (annotation.annotationType() == NeverSerialize.class) {
                                            shouldSerialize = false;
                                        } else if (annotation.annotationType() == Rename.class) {
                                            subKey = ((Rename) annotation).value();
                                        } else if (annotation.annotationType() == ExpectNotNull.class || annotation.annotationType() == ExpectIsNull.class || annotation.annotationType() == ExpectGreaterThan.class || annotation.annotationType() == ExpectLessThan.class) {
                                            expect = true;
                                        }
                                    }

                                    if (shouldSerialize && element.asObject().has(subKey)) {
                                        Object subObject = toObject(element.asObject().get(subKey), field.getGenericType());

                                        if (expect) {
                                            for (Annotation annotation : field.getDeclaredAnnotations()) {
                                                if (annotation.annotationType() == ExpectNotNull.class) {
                                                    if (subObject == null) {
                                                        throw new ExpectFailedException(subKey + " != null");
                                                    }
                                                } else if (annotation.annotationType() == ExpectIsNull.class) {
                                                    if (subObject != null) {
                                                        throw new ExpectFailedException(subKey + " == null");
                                                    }
                                                } else if (annotation.annotationType() == ExpectGreaterThan.class) {
                                                    if (object.getClass() == Byte.class || object.getClass() == Short.class || object.getClass() == Integer.class) {
                                                        if (subObject == null || (int) subObject > (((ExpectLessThan) annotation).intValue() - (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " >" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).intValue());
                                                        }
                                                    } else if (object.getClass() == Long.class) {
                                                        if (subObject == null || (long) subObject > (((ExpectLessThan) annotation).longValue() - (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " >" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).longValue());
                                                        }
                                                    } else if (object.getClass() == Float.class) {
                                                        if (subObject == null || (float) subObject > (((ExpectLessThan) annotation).floatValue() - (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " >" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).floatValue());
                                                        }
                                                    } else if (object.getClass() == Double.class) {
                                                        if (subObject == null || (double) subObject > (((ExpectLessThan) annotation).doubleValue() - (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " >" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).doubleValue());
                                                        }
                                                    } else {
                                                        throw new ExpectFailedException(subKey + " is not a number");
                                                    }
                                                } else if (annotation.annotationType() == ExpectLessThan.class) {
                                                    if (object.getClass() == Byte.class || object.getClass() == Short.class || object.getClass() == Integer.class) {
                                                        if (subObject == null || (int) subObject < (((ExpectLessThan) annotation).intValue() + (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " <" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).intValue());
                                                        }
                                                    } else if (object.getClass() == Long.class) {
                                                        if (subObject == null || (long) subObject < (((ExpectLessThan) annotation).longValue() + (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " <" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).longValue());
                                                        }
                                                    } else if (object.getClass() == Float.class) {
                                                        if (subObject == null || (float) subObject < (((ExpectLessThan) annotation).floatValue() + (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " <" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).floatValue());
                                                        }
                                                    } else if (object.getClass() == Double.class) {
                                                        if (subObject == null || (double) subObject < (((ExpectLessThan) annotation).doubleValue() + (((ExpectLessThan) annotation).orEqual() ? 1 : 0))) {
                                                            throw new ExpectFailedException(subKey + " <" + (((ExpectLessThan) annotation).orEqual() ? "=" : "") + " " + ((ExpectLessThan) annotation).doubleValue());
                                                        }
                                                    } else {
                                                        throw new ExpectFailedException(subKey + " is not a number");
                                                    }
                                                }
                                            }
                                        }

                                        if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] objects && objects.length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> collection && collection.isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> map && map.isEmpty()))) {
                                            field.set(object, subObject);
                                        }
                                    }
                                }
                            }

                            return object;
                        } else {
                            throw new InvalidTypeException(type.getRawClass());
                        }
                    }
                    case ParsedArray parsedArray -> {
                        if (type instanceof CollectionType) {
                            Collection<Object> object;
                            if (!type.getRawClass().isInterface()) {
                                object = (Collection<Object>) InitializationUtil.initialize(type.getRawClass());
                            } else {
                                object = InitializationUtil.initialize(LinkedList.class);
                            }

                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getContentType());
                                if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] objects && objects.length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> collection && collection.isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> map && map.isEmpty()))) {
                                    object.add(subObject);
                                }
                            }

                            return object;
                        } else if (type instanceof ArrayType) {
                            if (type.getRawClass() == byte[].class) {
                                byte[] array = (byte[]) InitializationUtil.initializePrimitiveArray(byte.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, byte.class);
                                    array[i] = (byte) (subObject != null ? subObject : 0);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == char[].class) {
                                char[] array = (char[]) InitializationUtil.initializePrimitiveArray(char.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, char.class);
                                    array[i] = (char) (subObject != null ? subObject : 0);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == short[].class) {
                                short[] array = (short[]) InitializationUtil.initializePrimitiveArray(short.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, short.class);
                                    array[i] = (short) (subObject != null ? subObject : 0);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == int[].class) {
                                int[] array = (int[]) InitializationUtil.initializePrimitiveArray(int.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, int.class);
                                    array[i] = (int) (subObject != null ? subObject : 0);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == long[].class) {
                                long[] array = (long[]) InitializationUtil.initializePrimitiveArray(long.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, long.class);
                                    array[i] = (long) (subObject != null ? subObject : 0L);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == float[].class) {
                                float[] array = (float[]) InitializationUtil.initializePrimitiveArray(float.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, float.class);
                                    array[i] = (float) (subObject != null ? subObject : 0.0f);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == double[].class) {
                                double[] array = (double[]) InitializationUtil.initializePrimitiveArray(double.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, double.class);
                                    array[i] = (double) (subObject != null ? subObject : 0.0d);

                                    i++;
                                }

                                return array;
                            } else if (type.getRawClass() == boolean[].class) {
                                boolean[] array = (boolean[]) InitializationUtil.initializePrimitiveArray(boolean.class, element.asArray().getSize());

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, boolean.class);
                                    array[i] = (boolean) (subObject != null ? subObject : false);

                                    i++;
                                }

                                return array;
                            } else {
                                int size = element.asArray().getSize();

                                if (ignoreArrayNulls || ignoreEmptyObjects) {
                                    size = 0;

                                    for (ParsedElement subElement : element.asArray().getValues()) {
                                        Object subObject = toObject(subElement, type.getContentType().getRawClass());
                                        if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] objects && objects.length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> collection && collection.isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> map && map.isEmpty()))) {
                                            size++;
                                        }
                                    }
                                }

                                Object[] array = InitializationUtil.initializeArray(type.getContentType().getRawClass(), size);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getContentType().getRawClass());
                                    if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] objects && objects.length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> collection && collection.isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> map && map.isEmpty()))) {
                                        array[i] = subObject;

                                        i++;
                                    }
                                }

                                return array;
                            }
                        } else {
                            throw new InvalidTypeException(type.getRawClass());
                        }
                    }
                    default -> {
                        throw new InvalidTypeException(type.getRawClass());
                    }
                }
            } else {
                throw new InvalidTypeException(type.getRawClass());
            }
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }

    /**
     * Maps this Object into a {@link io.github.kale_ko.bjsl.elements.ParsedElement}
     *
     * @param object The object to map
     *
     * @return A new {@link io.github.kale_ko.bjsl.elements.ParsedElement} with the values of object
     *
     * @throws io.github.kale_ko.bjsl.processor.exception.ProcessorException If there is an exception while processing
     * @since 1.0.0
     */
    public @NotNull ParsedElement toElement(@Nullable Object object) {
        try {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            try {
                JavaType type = TypeFactory.defaultInstance().constructSimpleType(object.getClass(), new JavaType[] { });

                Optional<TypeProcessor> typeProcessor = typeProcessors.entrySet().stream().filter(entry -> TypeUtils.isTypeOrSuperTypeOf(type, entry.getKey())).sorted((entryA, entryB) -> TypeUtils.sortDistance(type, entryA.getKey(), entryB.getKey())).map(Map.Entry::getValue).findFirst();
                if (typeProcessor.isPresent()) {
                    return typeProcessor.get().toElement(object);
                }
            } catch (IllegalArgumentException e) {
                // Class has type parameters, continue
            }

            try {
                return ParsedPrimitive.from(object);
            } catch (ClassCastException e) {
                // Not a primitive, continue
            }

            switch (object) {
                case Enum<?> anEnum -> {
                    return ParsedPrimitive.fromString(anEnum.name());
                }
                case byte[] bytes -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (byte item : bytes) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case char[] chars -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (char item : chars) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case short[] shorts -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (short item : shorts) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case int[] integers -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (int item : integers) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case long[] longs -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (long item : longs) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case float[] floats -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (float item : floats) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case double[] doubles -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (double item : doubles) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case boolean[] booleans -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (boolean item : booleans) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case Object[] objects -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : objects) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case Collection<?> objects -> {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : List.copyOf(objects)) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                }
                case Map<?, ?> map -> {
                    ParsedObject objectElement = ParsedObject.create();

                    for (Map.Entry<?, ?> entry : Map.copyOf(map).entrySet()) {
                        ParsedElement subElement = toElement(entry.getValue());
                        if (!((ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) || (ignoreEmptyObjects && (subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0)))) {
                            objectElement.set(toString(entry.getKey()), subElement);
                        }
                    }

                    return objectElement;
                }
                default -> {
                    ParsedObject objectElement = ParsedObject.create();

                    Object defaultObject = null;
                    if (ignoreDefaults) {
                        try {
                            defaultObject = InitializationUtil.initialize(object.getClass());
                        } catch (InitializationException e) {
                            if (BJSL.getLogger() != null) {
                                BJSL.getLogger().warning("Initialization of " + object.getClass().getSimpleName() + " failed, defaults will not be ignored");

                                StringWriter stringWriter = new StringWriter();
                                PrintWriter writer = new PrintWriter(stringWriter);
                                e.printStackTrace(writer);
                                BJSL.getLogger().warning(stringWriter.toString());
                            }
                        }
                    }

                    List<Field> fields = getFields(object.getClass());
                    for (Field field : fields) {
                        if (!Modifier.isStatic(field.getModifiers()) && (field.canAccess(object) || field.trySetAccessible())) {
                            boolean shouldSerialize = !(Modifier.isTransient(field.getModifiers()) || field.getName().startsWith("this$"));

                            String subKey = field.getName();
                            ParsedElement subElement = toElement(field.get(object));

                            if (ignoreNulls && (subElement.isPrimitive() && subElement.asPrimitive().isNull())) {
                                shouldSerialize = false;
                            }

                            if (ignoreEmptyObjects && ((subElement.isObject() && subElement.asObject().getSize() == 0) || (subElement.isArray() && subElement.asArray().getSize() == 0))) {
                                shouldSerialize = false;
                            }

                            if (ignoreDefaults && (subElement.isPrimitive() && (!subElement.asPrimitive().isNull() ? subElement.asPrimitive().get().equals(field.get(defaultObject)) : field.get(defaultObject) == null))) {
                                shouldSerialize = false;
                            }

                            for (Annotation annotation : field.getDeclaredAnnotations()) {
                                if (annotation.annotationType() == AlwaysSerialize.class) {
                                    shouldSerialize = true;
                                } else if (annotation.annotationType() == NeverSerialize.class) {
                                    shouldSerialize = false;
                                } else if (annotation.annotationType() == Rename.class) {
                                    subKey = ((Rename) annotation).value();
                                }
                            }

                            if (shouldSerialize) {
                                objectElement.set(subKey, subElement);
                            }
                        }
                    }

                    return objectElement;
                }
            }
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }

    /**
     * Maps this primitive Object into a {@link java.lang.String}
     *
     * @param object The object to map
     *
     * @return A new {@link java.lang.String} with the value of the object
     *
     * @throws io.github.kale_ko.bjsl.processor.exception.ProcessorException If there is an exception while processing
     * @since 2.0.0
     */
    protected @NotNull String toString(@Nullable Object object) {
        try {
            ParsedElement element = toElement(object);

            if (element.isPrimitive()) {
                ParsedPrimitive primitive = element.asPrimitive();

                return primitive.toString();
            } else {
                throw new NotAPrimitiveException(element);
            }
        } catch (ProcessorException e) {
            throw e;
        } catch (Exception e) {
            throw new ProcessorException(e);
        }
    }


    /**
     * Get all the fields on a class and its superclasses
     *
     * @param clazz The class to get the fields of
     * @param <T>   The class to get the fields of
     *
     * @return All the fields on the class and its superclasses
     *
     * @since 1.0.0
     */
    protected static <T> @NotNull List<Field> getFields(@NotNull Class<T> clazz) {
        Set<String> fieldNames = new HashSet<>();
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            List<Field> superFields = getFields(superClazz);

            for (Field superField : superFields) {
                boolean overwritten = fieldNames.contains(superField.getName());

                if (!(overwritten || Modifier.isTransient(superField.getModifiers()))) {
                    fieldNames.add(superField.getName());
                    fields.add(superField);
                }
            }
        }

        return fields;
    }
}