package io.github.kale_ko.bjsl.processor;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import io.github.kale_ko.bjsl.processor.annotations.AlwaysSerialize;
import io.github.kale_ko.bjsl.processor.annotations.Default;
import io.github.kale_ko.bjsl.processor.annotations.DontSerialize;

/**
 * An Object processor for mapping elements to objects and objects to elements
 * <p>
 * Also has options for reducing the amount of output keys
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ObjectProcessor {
    /**
     * Weather null values should be ignored when serializing
     *
     * @since 1.0.0
     */
    protected boolean ignoreNulls;

    /**
     * Weather empty objects (Objects with a size of 0) should be ignored when serializing
     *
     * @since 1.0.0
     */
    protected boolean ignoreEmptyObjects;

    /**
     * Weather default values should be ignored when serializing
     * <p>
     * These can either be gotten from @{@link Default} annotations or the value in a new instance of the object
     *
     * @since 1.0.0
     */
    protected boolean ignoreDefaults;

    /**
     * Weather checks for enum names should be case sensitive
     *
     * @since 1.0.0
     */
    protected boolean caseSensitiveEnums;

    /**
     * A map of object types to type processors
     *
     * @since 1.0.0
     */
    protected Map<JavaType, TypeProcessor> typeProcessors;

    /**
     * Create a new Parser using certain factories
     *
     * @param ignoreNulls
     *        Weather null values should be ignored when serializing
     * @param ignoreEmptyObjects
     *        Weather empty objects (Objects with a size of 0) should be ignored when serializing
     * @param ignoreDefaults
     *        Weather default values should be ignored when serializing
     * @param caseSensitiveEnums
     *        Weather checks for enum names should be case sensitive
     * @param typeProcessors
     *        A map of object types to type processors
     * @since 1.0.0
     */
    protected ObjectProcessor(boolean ignoreNulls, boolean ignoreEmptyObjects, boolean ignoreDefaults, boolean caseSensitiveEnums, Map<JavaType, TypeProcessor> typeProcessors) {
        this.ignoreNulls = ignoreNulls;
        this.ignoreEmptyObjects = ignoreEmptyObjects;
        this.ignoreDefaults = ignoreDefaults;

        this.caseSensitiveEnums = caseSensitiveEnums;

        this.typeProcessors = typeProcessors;
    }

    /**
     * A builder class for creating new {@link ObjectProcessor}s
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * Weather or not null values should be ignored when serializing
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean ignoreNulls = false;

        /**
         * Weather or not empty objects (Objects with a size of 0) should be ignored when serializing
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean ignoreEmptyObjects = false;

        /**
         * Weather or not default values should be ignored when serializing
         * <p>
         * These can either be gotten from @{@link Default} annotations or the value in a new instance of the object
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean ignoreDefaults = false;

        /**
         * Weather checks for enum names should be case sensitive
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
        protected Map<JavaType, TypeProcessor> typeProcessors = new HashMap<JavaType, TypeProcessor>();

        /**
         * Weather or not to enable the default type processors
         *
         * @since 1.4.0
         */
        protected boolean enableDefaultTypeProcessors = true;

        /**
         * Create a new {@link ObjectProcessor} builder
         *
         * @since 1.0.0
         */
        public Builder() {}

        /**
         * Get weather or not null values should be ignored when serializing
         * <p>
         * Default is false
         *
         * @return Weather or not null values should be ignored when serializing
         * @since 1.0.0
         */
        public boolean getIgnoreNulls() {
            return this.ignoreNulls;
        }

        /**
         * Set weather or not null values should be ignored when serializing
         * <p>
         * Default is false
         *
         * @param value
         *        Weather or not null values should be ignored when serializing
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder setIgnoreNulls(boolean value) {
            this.ignoreNulls = value;

            return this;
        }

        /**
         * Get weather or not empty objects (Objects with a size of 0) should be ignored when serializing
         * <p>
         * Default is false
         *
         * @return Weather or not empty objects (Objects with a size of 0) should be ignored when serializing
         * @since 1.0.0
         */
        public boolean getIgnoreEmptyObjects() {
            return this.ignoreEmptyObjects;
        }

        /**
         * Set weather or not empty objects (Objects with a size of 0) should be ignored when serializing
         * <p>
         * Default is false
         *
         * @param value
         *        Weather or not empty objects (Objects with a size of 0) should be ignored when serializing
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder setIgnoreEmptyObjects(boolean value) {
            this.ignoreEmptyObjects = value;

            return this;
        }

        /**
         * Get weather or not default values should be ignored when serializing
         * <p>
         * These can either be gotten from @{@link Default} annotations or the value in a new instance of the object
         * <p>
         * Default is false
         *
         * @return Weather or not default values should be ignored when serializing
         * @since 1.0.0
         */
        public boolean getIgnoreDefaults() {
            return this.ignoreDefaults;
        }

        /**
         * Set weather or not default values should be ignored when serializing
         * <p>
         * These can either be gotten from @{@link Default} annotations or the value in a new instance of the object
         * <p>
         * Default is false
         *
         * @param value
         *        Weather or not default values should be ignored when serializing
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder setIgnoreDefaults(boolean value) {
            this.ignoreDefaults = value;

            return this;
        }

        /**
         * Get weather checks for enum names should be case sensitive
         * <p>
         * Default is false
         *
         * @return Weather checks for enum names should be case sensitive
         * @since 1.0.0
         */
        public boolean getCaseSensitiveEnums() {
            return this.caseSensitiveEnums;
        }

        /**
         * Set weather checks for enum names should be case sensitive
         * <p>
         * Default is false
         *
         * @param value
         *        Weather checks for enum names should be case sensitive
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder setCaseSensitiveEnums(boolean value) {
            this.caseSensitiveEnums = value;

            return this;
        }

        /**
         * Get all the type processors created
         *
         * @return All the type processors created
         * @since 1.0.0
         */
        public Map<JavaType, TypeProcessor> getTypeProcessors() {
            return this.typeProcessors;
        }

        /**
         * Check if a type processor exists
         * <p>
         * Calls {@link #hasTypeProcessor(JavaType)}
         *
         * @param clazz
         *        The class to check for
         * @return If a type processor for clazz exists
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(Class<?> clazz) {
            return this.hasTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] {}));
        }

        /**
         * Check if a type processor exists
         * <p>
         * Calls {@link #hasTypeProcessor(JavaType)}
         *
         * @param type
         *        The type to check for
         * @return If a type processor for type exists
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(Type type) {
            return this.hasTypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Check if a type processor exists
         * <p>
         * Note: This method also checks for superclasses
         *
         * @param type
         *        The type to check for
         * @return If a type processor for type exists
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(JavaType type) {
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Get a type processor
         * <p>
         * Calls {@link #getTypeProcessor(JavaType)}
         *
         * @param clazz
         *        The class to get
         * @return The type processor for class
         * @since 1.0.0
         */
        public TypeProcessor getTypeProcessor(Class<?> clazz) {
            return this.getTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] {}));
        }

        /**
         * Get a type processor
         * <p>
         * Calls {@link #getTypeProcessor(JavaType)}
         *
         * @param type
         *        The type to get
         * @return The type processor for type
         * @since 1.0.0
         */
        public TypeProcessor getTypeProcessor(Type type) {
            return this.getTypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Get a type processor
         * <p>
         * Note: This method also checks for superclasses
         *
         * @param type
         *        The type to get
         * @return The type processor for type
         * @since 1.0.0
         */
        public TypeProcessor getTypeProcessor(JavaType type) {
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    return typeProcessor.getValue();
                }
            }

            throw new RuntimeException("A type processor does not exists for class \"" + type.getRawClass().getSimpleName() + "\" or one of its superclasses");
        }

        /**
         * Create a type processor
         * <p>
         * Calls {@link #createTypeProcessor(JavaType, TypeProcessor)}
         *
         * @param clazz
         *        The class to create
         * @param value
         *        The type processor to use
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder createTypeProcessor(Class<?> clazz, TypeProcessor value) {
            return this.createTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] {}), value);
        }

        /**
         * Create a type processor
         * <p>
         * Calls {@link #createTypeProcessor(JavaType, TypeProcessor)}
         *
         * @param type
         *        The type to create
         * @param value
         *        The type processor to use
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder createTypeProcessor(Type type, TypeProcessor value) {
            return this.createTypeProcessor(TypeFactory.defaultInstance().constructType(type), value);
        }

        /**
         * Create a type processor
         *
         * @param type
         *        The type to create
         * @param value
         *        The type processor to use
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder createTypeProcessor(JavaType type, TypeProcessor value) {
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    throw new RuntimeException("A type processor already exists for class \"" + type.getRawClass().getSimpleName() + "\" or one of its superclasses");
                }
            }

            this.typeProcessors.put(type, value);

            return this;
        }

        /**
         * Remove a type processor
         * <p>
         * Calls {@link #removeTypeProcessor(JavaType)}
         *
         * @param clazz
         *        The class to remove
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder removeTypeProcessor(Class<?> clazz) {
            return this.removeTypeProcessor(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] {}));
        }

        /**
         * Remove a type processor
         * <p>
         * Calls {@link #removeTypeProcessor(JavaType)}
         *
         * @param type
         *        The type to remove
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder removeTypeProcessor(Type type) {
            return this.removeTypeProcessor(TypeFactory.defaultInstance().constructType(type));
        }

        /**
         * Remove a type processor
         * <p>
         * Note: This method also checks for superclasses
         *
         * @param type
         *        The type to remove
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder removeTypeProcessor(JavaType type) {
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    typeProcessors.remove(typeProcessor.getKey());

                    return this;
                }
            }

            throw new RuntimeException("A type processor does not exists for class \"" + type.getRawClass().getSimpleName() + "\" or one of its superclasses");
        }

        /**
         * Get weather or not to enable the default type processors
         * <p>
         * Default is true
         *
         * @return Weather or not to enable the default type processors
         * @since 1.4.0
         */
        public boolean getEnableDefaultTypeProcessors() {
            return this.enableDefaultTypeProcessors;
        }

        /**
         * Set weather or not to enable the default type processors
         * <p>
         * There are default type processors defined for {@link UUID}, {@link URI}, {@link URL}, {@link Path}, {@link File}, {@link InetAddress}, {@link InetSocketAddress}, {@link Calendar}, {@link Date}, and {@link Instant}
         * <p>
         * Default is true
         *
         * @param value
         *        Weather or not to enable the default type processors
         * @return Self for chaining
         * @since 1.4.0
         */
        public Builder setEnableDefaultTypeProcessors(boolean value) {
            this.enableDefaultTypeProcessors = value;

            return this;
        }

        /**
         * Uses the current settings to build a new {@link ObjectProcessor}
         *
         * @return A new {@link ObjectProcessor} instance
         * @since 1.0.0
         */
        public ObjectProcessor build() {
            if (this.enableDefaultTypeProcessors) {
                if (!this.hasTypeProcessor(UUID.class)) {
                    this.createTypeProcessor(UUID.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof UUID) {
                                return ParsedPrimitive.fromString(((UUID) object).toString());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return UUID.fromString(element.asPrimitive().asString());
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(URI.class)) {
                    this.createTypeProcessor(URI.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof URI) {
                                return ParsedPrimitive.fromString(((URI) object).toString());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return new URI(element.asPrimitive().asString());
                                } catch (URISyntaxException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(URL.class)) {
                    this.createTypeProcessor(URL.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof URL) {
                                return ParsedPrimitive.fromString(((URL) object).toString());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return new URL(element.asPrimitive().asString());
                                } catch (MalformedURLException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Path.class)) {
                    this.createTypeProcessor(Path.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof Path) {
                                return ParsedPrimitive.fromString(((Path) object).toString());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return Path.of(element.asPrimitive().asString());
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(File.class)) {
                    this.createTypeProcessor(File.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof File) {
                                return ParsedPrimitive.fromString(((File) object).getPath());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return new File(element.asPrimitive().asString());
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(InetAddress.class)) {
                    this.createTypeProcessor(InetAddress.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof InetAddress) {
                                return ParsedPrimitive.fromString(((InetAddress) object).getHostName());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return InetAddress.getByName(element.asPrimitive().asString());
                                } catch (UnknownHostException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(InetSocketAddress.class)) {
                    this.createTypeProcessor(InetSocketAddress.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof InetSocketAddress) {
                                return ParsedPrimitive.fromString(((InetSocketAddress) object).getHostName() + ":" + ((InetSocketAddress) object).getPort());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return new InetSocketAddress(InetAddress.getByName(element.asPrimitive().asString().split(":")[0]), Integer.parseInt(element.asPrimitive().asString().split(":")[1]));
                                } catch (UnknownHostException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Calendar.class)) {
                    this.createTypeProcessor(Calendar.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof Calendar) {
                                return ParsedPrimitive.fromString(DateFormat.getDateInstance(DateFormat.DEFAULT).format(((Calendar) object).getTime()));
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(DateFormat.getDateInstance(DateFormat.DEFAULT).parse(element.asPrimitive().asString()));
                                    return calendar;
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Date.class)) {
                    this.createTypeProcessor(Date.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof Date) {
                                return ParsedPrimitive.fromString(DateFormat.getDateInstance(DateFormat.DEFAULT).format((Date) object));
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return DateFormat.getDateInstance(DateFormat.DEFAULT).parse(element.asPrimitive().asString());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                return null;
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Instant.class)) {
                    this.createTypeProcessor(Instant.class, new TypeProcessor() {
                        @Override
                        public ParsedElement toElement(Object object) {
                            if (object != null && object instanceof Instant) {
                                return ParsedPrimitive.fromLong(((Instant) object).toEpochMilli());
                            } else {
                                return ParsedPrimitive.fromNull();
                            }
                        }

                        @Override
                        public Object toObject(ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isLong()) {
                                return Instant.ofEpochMilli(element.asPrimitive().asLong());
                            } else {
                                return null;
                            }
                        }
                    });
                }
            }

            return new ObjectProcessor(this.ignoreNulls, this.ignoreEmptyObjects, this.ignoreDefaults, this.caseSensitiveEnums, this.typeProcessors);
        }
    }

    /**
     * Maps this element into an Object
     * <p>
     * Calls {@link #toObject(ParsedElement, JavaType)}
     *
     * @param element
     *        The element to map
     * @param clazz
     *        The object type to map to
     * @param <T>
     *        The object type to map to
     * @return A new Object of type clazz with the values of element
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T toObject(ParsedElement element, Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Clazz can not be null");
        }

        return (T) toObject(element, TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] {}));
    }

    /**
     * Maps this element into an Object
     * <p>
     * Calls {@link #toObject(ParsedElement, JavaType)}
     *
     * @param element
     *        The element to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of element
     * @since 1.0.0
     */
    public Object toObject(ParsedElement element, Type type) {
        if (type == null) {
            throw new NullPointerException("Type can not be null");
        }

        return toObject(element, TypeFactory.defaultInstance().constructType(type));
    }

    /**
     * Maps this element into an Object
     *
     * @param element
     *        The element to map
     * @param type
     *        The object type to map to
     * @return A new Object of type type with the values of element
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public Object toObject(ParsedElement element, JavaType type) {
        if (type == null) {
            throw new NullPointerException("Type can not be null");
        }

        try {
            if (element == null || (element.isPrimitive() && element.asPrimitive().isNull())) {
                return null;
            }

            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    return typeProcessor.getValue().toObject(element);
                }
            }

            if (element instanceof ParsedPrimitive) {
                if (type.getRawClass().isEnum()) {
                    if (element instanceof ParsedPrimitive) {
                        if (element.asPrimitive().isString()) {
                            for (Object value : type.getRawClass().getEnumConstants()) {
                                if ((caseSensitiveEnums && ((Enum<?>) value).name().equals(element.asPrimitive().asString())) || (!caseSensitiveEnums && ((Enum<?>) value).name().equalsIgnoreCase(element.asPrimitive().asString()))) {
                                    return value;
                                }
                            }

                            BJSL.getLogger().warning("Unknown enum value \"" + element.asPrimitive().asString() + "\" for type \"" + type.getRawClass().getSimpleName() + "\"");

                            return null;
                        } else if (element.asPrimitive().isNull()) {
                            return null;
                        } else {
                            throw new RuntimeException("Element for type \"" + type.getRawClass().getSimpleName() + "\" is not a enum");
                        }
                    } else {
                        throw new RuntimeException("Element for type \"" + type.getRawClass().getSimpleName() + "\" is not a enum");
                    }
                } else {
                    try {
                        Object object = element.asPrimitive().get();

                        if (object == null) {
                            return object;
                        } else if (type.getRawClass() == String.class) {
                            return (String) object;
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
                            return type.getRawClass().cast(object);
                        }
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Element could not be cast to \"" + type.getRawClass().getSimpleName() + "\"", e);
                    }
                }
            } else if (!type.getRawClass().isAnonymousClass() && !type.getRawClass().isAnnotation()) {
                if (element instanceof ParsedObject) {
                    if (type instanceof MapType) {
                        Map<String, Object> object = null;

                        if (!type.getRawClass().isInterface()) {
                            if (type.getRawClass().getConstructors().length > 0) {
                                try {
                                    for (Constructor<?> constructor : type.getRawClass().getConstructors()) {
                                        if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                            object = (Map<String, Object>) constructor.newInstance();

                                            break;
                                        }
                                    }
                                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                    if (BJSL.getLogger() != null) {
                                        StringWriter writer = new StringWriter();
                                        new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                        BJSL.getLogger().warning(writer.toString());
                                    }
                                }
                            } else {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (Map<String, Object>) unsafe.allocateInstance(type.getRawClass());
                                } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                    if (BJSL.getLogger() != null) {
                                        StringWriter writer = new StringWriter();
                                        new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                        BJSL.getLogger().warning(writer.toString());
                                    }
                                }
                            }
                        } else {
                            try {
                                for (Constructor<?> constructor : LinkedHashMap.class.getConstructors()) {
                                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                        object = (Map<String, Object>) constructor.newInstance();

                                        break;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                if (BJSL.getLogger() != null) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }
                        }

                        if (object != null) {
                            for (Map.Entry<String, ParsedElement> entry : element.asObject().getEntries()) {
                                Object subObject = toObject(entry.getValue(), ((MapType) type).getContentType());
                                if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                    object.put(entry.getKey(), subObject);
                                }
                            }

                            return object;
                        } else {
                            throw new RuntimeException("No 0-args constructors for \"" + type.getRawClass().getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else if (!type.getRawClass().isInterface()) {
                        Object object = null;

                        if (type.getRawClass().getConstructors().length > 0) {
                            try {
                                for (Constructor<?> constructor : type.getRawClass().getConstructors()) {
                                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                        object = constructor.newInstance();

                                        break;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                if (BJSL.getLogger() != null) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }
                        } else {
                            try {
                                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                unsafeField.setAccessible(true);
                                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                object = unsafe.allocateInstance(type.getRawClass());
                            } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                if (BJSL.getLogger() != null) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }
                        }

                        if (object != null) {
                            List<Field> fields = getFields(object.getClass());

                            for (Field field : fields) {
                                try {
                                    if (!Modifier.isStatic(field.getModifiers()) && (field.canAccess(object) || field.trySetAccessible())) {
                                        Boolean shouldSerialize = element.asObject().has(field.getName()) && !Modifier.isTransient(field.getModifiers());

                                        for (Annotation annotation : field.getDeclaredAnnotations()) {
                                            if (annotation.annotationType() == AlwaysSerialize.class) {
                                                shouldSerialize = true;
                                            } else if (annotation.annotationType() == DontSerialize.class) {
                                                shouldSerialize = false;
                                            }
                                        }

                                        if (field.getName().startsWith("this$")) {
                                            shouldSerialize = false;
                                        }

                                        if (shouldSerialize) {
                                            Object subObject = toObject(element.asObject().get(field.getName()), field.getGenericType());
                                            if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                                field.set(object, subObject);
                                            }
                                        }
                                    }
                                } catch (IllegalArgumentException | IllegalAccessException e) {
                                    if (BJSL.getLogger() != null) {
                                        StringWriter writer = new StringWriter();
                                        new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                        BJSL.getLogger().warning(writer.toString());
                                    }
                                }
                            }

                            return object;
                        } else {
                            throw new RuntimeException("No 0-args constructors for \"" + type.getRawClass().getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else {
                        throw new RuntimeException("Type \"" + type.getRawClass().getSimpleName() + "\" is not serializable");
                    }
                } else if (element instanceof ParsedArray) {
                    if (type instanceof CollectionType) {
                        Collection<Object> object = null;

                        if (!type.getRawClass().isInterface()) {
                            if (type.getRawClass().getConstructors().length > 0) {
                                try {
                                    for (Constructor<?> constructor : type.getRawClass().getConstructors()) {
                                        if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                            object = (Collection<Object>) constructor.newInstance();

                                            break;
                                        }
                                    }
                                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                    if (BJSL.getLogger() != null) {
                                        StringWriter writer = new StringWriter();
                                        new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                        BJSL.getLogger().warning(writer.toString());
                                    }
                                }
                            } else {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (Collection<Object>) unsafe.allocateInstance(type.getRawClass());
                                } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                    if (BJSL.getLogger() != null) {
                                        StringWriter writer = new StringWriter();
                                        new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                        BJSL.getLogger().warning(writer.toString());
                                    }
                                }
                            }
                        } else {
                            try {
                                for (Constructor<?> constructor : ArrayList.class.getConstructors()) {
                                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                        object = (Collection<Object>) constructor.newInstance();

                                        break;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                if (BJSL.getLogger() != null) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }
                        }

                        if (object != null) {
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, ((CollectionType) type).getContentType());
                                if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                    object.add(subObject);
                                }
                            }

                            return object;
                        } else {
                            throw new RuntimeException("No 0-args constructors for \"" + type.getRawClass().getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else if (type instanceof ArrayType) {
                        try {
                            int nonNull = element.asArray().getSize();

                            if (ignoreNulls || ignoreEmptyObjects) {
                                nonNull = 0;

                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass().getComponentType());
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                        nonNull++;
                                    }
                                }
                            }

                            Object result;

                            if (type.getRawClass() == byte[].class) {
                                byte[] array = (byte[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (byte) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == char[].class) {
                                char[] array = (char[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (char) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == short[].class) {
                                short[] array = (short[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (short) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == int[].class) {
                                int[] array = (int[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (int) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == long[].class) {
                                long[] array = (long[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (long) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == float[].class) {
                                float[] array = (float[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (float) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == double[].class) {
                                double[] array = (double[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (double) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == boolean[].class) {
                                boolean[] array = (boolean[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (boolean) toObject(subElement, type.getRawClass().getComponentType());

                                    i++;
                                }

                                result = array;
                            } else {
                                Object[] array = (Object[]) Array.newInstance(type.getRawClass().getComponentType(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass().getComponentType());
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                        array[i] = subObject;

                                        i++;
                                    }
                                }

                                result = array;
                            }

                            return result;
                        } catch (NegativeArraySizeException e) {
                            throw new RuntimeException("Error while parsing: ", e);
                        }
                    } else if (!type.getRawClass().isInterface()) {
                        try {
                            int nonNull = element.asArray().getSize();

                            if (ignoreNulls || ignoreEmptyObjects) {
                                nonNull = 0;

                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass());
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                        nonNull++;
                                    }
                                }
                            }

                            Object result;

                            if (type.getRawClass() == byte[].class) {
                                byte[] array = (byte[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (byte) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == char[].class) {
                                char[] array = (char[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (char) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == short[].class) {
                                short[] array = (short[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (short) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == int[].class) {
                                int[] array = (int[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (int) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == long[].class) {
                                long[] array = (long[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (long) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == float[].class) {
                                float[] array = (float[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (float) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == double[].class) {
                                double[] array = (double[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (double) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else if (type.getRawClass() == boolean[].class) {
                                boolean[] array = (boolean[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    array[i] = (boolean) toObject(subElement, type.getRawClass());

                                    i++;
                                }

                                result = array;
                            } else {
                                Object[] array = (Object[]) Array.newInstance(type.getRawClass(), nonNull);

                                int i = 0;
                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass());
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                        array[i] = subObject;

                                        i++;
                                    }
                                }

                                result = array;
                            }

                            return result;
                        } catch (NegativeArraySizeException e) {
                            throw new RuntimeException("Error while parsing: ", e);
                        }
                    } else {
                        throw new RuntimeException("Type \"" + type.getRawClass().getSimpleName() + "\" is not serializable");
                    }
                } else {
                    throw new RuntimeException("Element is not an object or array");
                }
            } else {
                throw new RuntimeException("Type \"" + type.getRawClass().getSimpleName() + "\" is not serializable");
            }
        } catch (RuntimeException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());

                return null;
            } else {
                throw new RuntimeException("Error while parsing:", e);
            }
        }
    }

    /**
     * Maps this Object into a {@link ParsedElement}
     *
     * @param object
     *        The object to map
     * @return A new {@link ParsedElement} with the values of object
     * @since 1.0.0
     */
    public ParsedElement toElement(Object object) {
        try {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object.getClass().getTypeParameters().length == 0) {
                JavaType type = TypeFactory.defaultInstance().constructSimpleType(object.getClass(), new JavaType[] {});

                for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                    if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                        return typeProcessor.getValue().toElement(object);
                    }
                }
            }

            try {
                return ParsedPrimitive.from(object);
            } catch (ClassCastException e2) {
                if (object instanceof Enum<?>) {
                    return ParsedPrimitive.fromString(((Enum<?>) object).name());
                } else if (object instanceof byte[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (byte item : (byte[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof char[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (char item : (char[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof short[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (short item : (short[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof int[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (int item : (int[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof long[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (long item : (long[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof float[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (float item : (float[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof double[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (double item : (double[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof boolean[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (boolean item : (boolean[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Object[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : (Object[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Collection<?>) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : (Collection<?>) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Map<?, ?>) {
                    ParsedObject objectElement = ParsedObject.create();

                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                        ParsedElement subElement = toElement(entry.getValue());
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            objectElement.set(entry.getKey().toString(), subElement);
                        }
                    }

                    return objectElement;
                } else {
                    ParsedObject objectElement = ParsedObject.create();

                    Object defaultObject = null;

                    if (ignoreDefaults) {
                        if (object.getClass().getConstructors().length > 0) {
                            try {
                                for (Constructor<?> constructor : object.getClass().getConstructors()) {
                                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                        defaultObject = constructor.newInstance();

                                        break;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                if (BJSL.getLogger() != null) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }
                        } else {
                            try {
                                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                unsafeField.setAccessible(true);
                                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                defaultObject = unsafe.allocateInstance(object.getClass());
                            } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                if (BJSL.getLogger() != null) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }
                        }

                        if (defaultObject == null) {
                            BJSL.getLogger().warning("Initialization of " + object.getClass().getSimpleName() + " failed, defaults will not be ignored");
                        }
                    }

                    List<Field> fields = getFields(object.getClass());

                    for (Field field : fields) {
                        try {
                            if (!Modifier.isStatic(field.getModifiers()) && (field.canAccess(object) || field.trySetAccessible())) {
                                Boolean shouldSerialize = !Modifier.isTransient(field.getModifiers());

                                ParsedElement subElement = toElement(field.get(object));

                                if (ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) {
                                    shouldSerialize = false;
                                }

                                if (ignoreEmptyObjects && ((subElement.isArray() && subElement.asArray().getSize() == 0) || (subElement.isObject() && subElement.asObject().getSize() == 0))) {
                                    shouldSerialize = false;
                                }

                                if (ignoreDefaults && (subElement.isPrimitive() && !subElement.asPrimitive().isNull() && subElement.asPrimitive().get().equals(field.get(defaultObject)))) {
                                    shouldSerialize = false;
                                }

                                for (Annotation annotation : field.getDeclaredAnnotations()) {
                                    if (annotation.annotationType() == AlwaysSerialize.class) {
                                        shouldSerialize = true;
                                    } else if (annotation.annotationType() == DontSerialize.class) {
                                        shouldSerialize = false;
                                    } else if (annotation.annotationType() == Default.class) {
                                        Default defaultAnnotation = (Default) annotation;

                                        if (ignoreDefaults && subElement.isPrimitive()) {
                                            if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.STRING && !defaultAnnotation.stringValue().equals("")) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.stringValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.BYTE && defaultAnnotation.byteValue() != Byte.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.byteValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.CHAR && defaultAnnotation.charValue() != Character.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.charValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.SHORT && defaultAnnotation.shortValue() != Short.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.shortValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.INTEGER && defaultAnnotation.intValue() != Integer.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.intValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.LONG && defaultAnnotation.longValue() != Long.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.longValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.DOUBLE && defaultAnnotation.doubleValue() != Double.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.doubleValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.FLOAT && defaultAnnotation.floatValue() != Float.MIN_VALUE) {
                                                if (field.get(object) != null && field.get(object).equals(defaultAnnotation.floatValue())) {
                                                    shouldSerialize = false;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (field.getName().startsWith("this$")) {
                                    shouldSerialize = false;
                                }

                                if (shouldSerialize) {
                                    objectElement.set(field.getName(), subElement);
                                }
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            if (BJSL.getLogger() != null) {
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                BJSL.getLogger().warning(writer.toString());
                            }
                        }
                    }

                    return objectElement;
                }
            }
        } catch (RuntimeException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().severe(writer.toString());

                return null;
            } else {
                throw new RuntimeException("Error while parsing:", e);
            }
        }
    }

    /**
     * Get all the fields on a class and its superclasses
     *
     * @param clazz
     *        The class to get the fields of
     * @param <T>
     *        The class to get the fields of
     * @return All the fields on the class and its superclasses
     * @since 1.0.0
     */
    protected static <T> List<Field> getFields(Class<T> clazz) {
        List<Field> fields = new ArrayList<Field>();

        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
            List<Field> parentFields = getFields(clazz.getSuperclass());

            for (Field parentField : parentFields) {
                Boolean overwritten = false;

                for (Field field : fields) {
                    if (field.getName().equals(parentField.getName())) {
                        overwritten = true;

                        continue;
                    }
                }

                if (!overwritten && !Modifier.isTransient(parentField.getModifiers())) {
                    fields.add(parentField);
                }
            }
        }

        return fields;
    }
}