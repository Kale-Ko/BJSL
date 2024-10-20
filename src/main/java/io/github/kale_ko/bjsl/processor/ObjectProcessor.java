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
import io.github.kale_ko.bjsl.processor.exception.EnumExpectedException;
import io.github.kale_ko.bjsl.processor.exception.ExpectFailedException;
import io.github.kale_ko.bjsl.processor.exception.InitializationException;
import io.github.kale_ko.bjsl.processor.exception.ProcessorException;
import io.github.kale_ko.bjsl.processor.reflection.InitializationUtil;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
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
         * Check if a type processor exists
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
         * Check if a type processor exists
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
         * Check if a type processor exists
         * <p>
         * Note: This method also checks for superclasses
         *
         * @param type The type to check for
         *
         * @return If a type processor for type exists
         *
         * @since 1.0.0
         */
        public boolean hasTypeProcessor(@NotNull JavaType type) {
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
         * Get a type processor
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
         * Get a type processor
         * <p>
         * Note: This method also checks for superclasses
         *
         * @param type The type to get
         *
         * @return The type processor for type
         *
         * @since 1.0.0
         */
        public @NotNull TypeProcessor getTypeProcessor(@NotNull JavaType type) {
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    return typeProcessor.getValue();
                }
            }

            throw new NullPointerException("A type processor does not exists for class \"" + type.getRawClass().getSimpleName() + "\" or one of its superclasses");
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
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    throw new NullPointerException("A type processor already exists for class \"" + type.getRawClass().getSimpleName() + "\" or one of its superclasses");
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
         * <p>
         * Note: This method also checks for superclasses
         *
         * @param type The type to remove
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder removeTypeProcessor(@NotNull JavaType type) {
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    typeProcessors.remove(typeProcessor.getKey());

                    return this;
                }
            }

            throw new NullPointerException("A type processor does not exists for class \"" + type.getRawClass().getSimpleName() + "\" or one of its superclasses");
        }

        /**
         * Get weather or not to enable the default type processors
         * <p>
         * Default is true
         *
         * @return Weather or not to enable the default type processors
         *
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
         * @param value Weather or not to enable the default type processors
         *
         * @return Self for chaining
         *
         * @since 1.4.0
         */
        public @NotNull Builder setEnableDefaultTypeProcessors(boolean value) {
            this.enableDefaultTypeProcessors = value;

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
            if (this.enableDefaultTypeProcessors) {
                if (!this.hasTypeProcessor(UUID.class)) {
                    this.createTypeProcessor(UUID.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof UUID) {
                                return ParsedPrimitive.fromString(object.toString());
                            } else {
                                throw new InvalidParameterException("object must be UUID");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return UUID.fromString(element.asPrimitive().asString());
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(URI.class)) {
                    this.createTypeProcessor(URI.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof URI) {
                                return ParsedPrimitive.fromString(object.toString());
                            } else {
                                throw new InvalidParameterException("object must be URI");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return new URI(element.asPrimitive().asString());
                                } catch (URISyntaxException e) {
                                    throw new InvalidParameterException("object is an invalid URI");
                                }
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(URL.class)) {
                    this.createTypeProcessor(URL.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof URL) {
                                return ParsedPrimitive.fromString(object.toString());
                            } else {
                                throw new InvalidParameterException("object must be URL");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return new URL(element.asPrimitive().asString());
                                } catch (MalformedURLException e) {
                                    throw new InvalidParameterException("object is an invalid URL");
                                }
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Path.class)) {
                    this.createTypeProcessor(Path.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof Path) {
                                return ParsedPrimitive.fromString(((Path) object).toString());
                            } else {
                                throw new InvalidParameterException("object must be Path");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return Path.of(element.asPrimitive().asString());
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(File.class)) {
                    this.createTypeProcessor(File.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof File) {
                                return ParsedPrimitive.fromString(((File) object).getPath());
                            } else {
                                throw new InvalidParameterException("object must be File");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return new File(element.asPrimitive().asString());
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(InetAddress.class)) {
                    this.createTypeProcessor(InetAddress.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof InetAddress) {
                                byte[] address = ((InetAddress) object).getAddress();

                                if (address.length == 4) {
                                    String[] addressBytes = new String[4];

                                    for (int i = 0; i < 4; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(address[i] & 0xff, 10);
                                    }

                                    return ParsedPrimitive.fromString(String.join(".", addressBytes));
                                } else if (address.length == 16) {
                                    String[] addressBytes = new String[8];

                                    for (int i = 0; i < 8; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(((address[i * 2] & 0xff) << 8) + (address[(i * 2) + 1] & 0xff), 16);
                                    }

                                    return ParsedPrimitive.fromString(String.join(":", addressBytes));
                                } else {
                                    throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                                }
                            } else {
                                throw new InvalidParameterException("object must be InetAddress");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    String[] addressBytes = element.asPrimitive().asString().split("[.:]");

                                    if (addressBytes.length == 4) {
                                        byte[] address = new byte[4];

                                        for (int i = 0; i < 4; i++) {
                                            address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xff);
                                        }

                                        return InetAddress.getByAddress(address);
                                    } else if (addressBytes.length == 8) {
                                        byte[] address = new byte[16];

                                        for (int i = 0; i < 8; i++) {
                                            address[i * 2] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                            address[(i * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                        }

                                        return InetAddress.getByAddress(address);
                                    } else {
                                        throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                                    }
                                } catch (UnknownHostException e) {
                                    throw new InvalidParameterException("object is an invalid InetAddress");
                                }
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(InetSocketAddress.class)) {
                    this.createTypeProcessor(InetSocketAddress.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof InetSocketAddress) {
                                byte[] address = ((InetSocketAddress) object).getAddress().getAddress();
                                int port = ((InetSocketAddress) object).getPort();

                                if (address.length == 4) {
                                    String[] addressBytes = new String[4];

                                    for (int i = 0; i < 4; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(address[i] & 0xff, 10);
                                    }

                                    return ParsedPrimitive.fromString(String.join(".", addressBytes) + ":" + Integer.toUnsignedString(port, 10));
                                } else if (address.length == 16) {
                                    String[] addressBytes = new String[8];

                                    for (int i = 0; i < 8; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(((address[i * 2] & 0xff) << 8) + (address[(i * 2) + 1] & 0xff), 16);
                                    }

                                    return ParsedPrimitive.fromString("[" + String.join(":", addressBytes) + "]:" + Integer.toUnsignedString(port, 10));
                                } else {
                                    throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                                }
                            } else {
                                throw new InvalidParameterException("object must be InetSocketAddress");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    String[] addressBytes = element.asPrimitive().asString().replace("[", "").replace("]", "").split("[.:]");

                                    if (addressBytes.length == 5) {
                                        byte[] address = new byte[4];
                                        int port;

                                        for (int i = 0; i < 4; i++) {
                                            address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xff);
                                        }

                                        port = Integer.parseUnsignedInt(addressBytes[4], 10);

                                        return new InetSocketAddress(InetAddress.getByAddress(address), port);
                                    } else if (addressBytes.length == 9) {
                                        byte[] address = new byte[16];
                                        int port;

                                        for (int i = 0; i < 8; i++) {
                                            address[i * 2] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                            address[(i * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                        }

                                        port = Integer.parseUnsignedInt(addressBytes[8], 10);

                                        return new InetSocketAddress(InetAddress.getByAddress(address), port);
                                    } else {
                                        throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                                    }
                                } catch (UnknownHostException e) {
                                    throw new InvalidParameterException("object is an invalid InetSocketAddress");
                                }
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Calendar.class)) {
                    this.createTypeProcessor(Calendar.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof Calendar) {
                                return ParsedPrimitive.fromString(DateFormat.getDateInstance(DateFormat.DEFAULT).format(((Calendar) object).getTime()));
                            } else {
                                throw new InvalidParameterException("object must be Calendar");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(DateFormat.getDateInstance(DateFormat.DEFAULT).parse(element.asPrimitive().asString()));
                                    return calendar;
                                } catch (ParseException e) {
                                    throw new InvalidParameterException("object is an invalid Date");
                                }
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Date.class)) {
                    this.createTypeProcessor(Date.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof Date) {
                                return ParsedPrimitive.fromString(DateFormat.getDateInstance(DateFormat.DEFAULT).format((Date) object));
                            } else {
                                throw new InvalidParameterException("object must be Date");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isString()) {
                                try {
                                    return DateFormat.getDateInstance(DateFormat.DEFAULT).parse(element.asPrimitive().asString());
                                } catch (ParseException e) {
                                    throw new InvalidParameterException("object is an invalid Date");
                                }
                            } else {
                                throw new InvalidParameterException("object must be String");
                            }
                        }
                    });
                }

                if (!this.hasTypeProcessor(Instant.class)) {
                    this.createTypeProcessor(Instant.class, new TypeProcessor() {
                        @Override
                        public @NotNull ParsedElement toElement(@Nullable Object object) {
                            if (object == null) {
                                return ParsedPrimitive.fromNull();
                            }

                            if (object instanceof Instant) {
                                return ParsedPrimitive.fromLong(((Instant) object).toEpochMilli());
                            } else {
                                throw new InvalidParameterException("object must be Instant");
                            }
                        }

                        @Override
                        public @Nullable Object toObject(@NotNull ParsedElement element) {
                            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                                return null;
                            }

                            if (element.isPrimitive() && element.asPrimitive().isLong()) {
                                return Instant.ofEpochMilli(element.asPrimitive().asLong());
                            } else if (element.isPrimitive() && element.asPrimitive().isString()) {
                                return Instant.ofEpochMilli(Long.parseLong(element.asPrimitive().asString()));
                            } else {
                                throw new InvalidParameterException("object must be Long");
                            }
                        }
                    });
                }
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
     * @since 1.0.0
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

            // TODO Order type processors based on how "close" the class handler is to the type processor handler
            for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                    return typeProcessor.getValue().toObject(element);
                }
            }

            if (element instanceof ParsedPrimitive) {
                if (type.getRawClass().isEnum()) {
                    if (element.asPrimitive().isString()) {
                        for (Object value : type.getRawClass().getEnumConstants()) {
                            if ((caseSensitiveEnums && ((Enum<?>) value).name().equals(element.asPrimitive().asString())) || (!caseSensitiveEnums && ((Enum<?>) value).name().equalsIgnoreCase(element.asPrimitive().asString()))) {
                                return value;
                            }
                        }

                        if (BJSL.getLogger() != null) {
                            BJSL.getLogger().warning("Unknown enum value \"" + element.asPrimitive().asString() + "\" for type \"" + type.getRawClass().getSimpleName() + "\"");
                        }

                        return null;
                    } else if (element.asPrimitive().isNull()) {
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
                if (element instanceof ParsedObject) {
                    if (type instanceof MapType) {
                        Map<String, Object> object;
                        if (!type.getRawClass().isInterface()) {
                            object = (Map<String, Object>) InitializationUtil.initialize(type.getRawClass());
                        } else {
                            object = InitializationUtil.initialize(LinkedHashMap.class);
                        }

                        for (Map.Entry<String, ParsedElement> entry : element.asObject().getEntries()) {
                            Object subObject = toObject(entry.getValue(), type.getContentType());
                            if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                object.put(entry.getKey(), subObject);
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
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                        field.set(object, subObject);
                                    }
                                }
                            }
                        }

                        return object;
                    } else {
                        throw new InvalidTypeException(type.getRawClass());
                    }
                } else if (element instanceof ParsedArray) {
                    if (type instanceof CollectionType) {
                        Collection<Object> object;
                        if (!type.getRawClass().isInterface()) {
                            object = (Collection<Object>) InitializationUtil.initialize(type.getRawClass());
                        } else {
                            object = InitializationUtil.initialize(LinkedList.class);
                        }

                        for (ParsedElement subElement : element.asArray().getValues()) {
                            Object subObject = toObject(subElement, type.getContentType());
                            if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                object.add(subObject);
                            }
                        }

                        return object;
                    } else if (type instanceof ArrayType) {
                        int nonNull = element.asArray().getSize();

                        if (ignoreArrayNulls || ignoreEmptyObjects) {
                            nonNull = 0;

                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass().getComponentType());
                                if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                    nonNull++;
                                }
                            }
                        }

                        Object result;

                        if (type.getRawClass() == byte[].class) {
                            byte[] array = (byte[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (byte) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == char[].class) {
                            char[] array = (char[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (char) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == short[].class) {
                            short[] array = (short[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (short) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == int[].class) {
                            int[] array = (int[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (int) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == long[].class) {
                            long[] array = (long[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (long) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == float[].class) {
                            float[] array = (float[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (float) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == double[].class) {
                            double[] array = (double[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (double) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == boolean[].class) {
                            boolean[] array = (boolean[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (boolean) toObject(subElement, type.getRawClass().getComponentType());

                                i++;
                            }

                            result = array;
                        } else {
                            Object[] array = (Object[]) InitializationUtil.initializeArray(type.getRawClass().getComponentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass().getComponentType());
                                if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                    array[i] = subObject;

                                    i++;
                                }
                            }

                            result = array;
                        }

                        return result;
                    } else if (!type.getRawClass().isInterface()) {
                        int nonNull = element.asArray().getSize();

                        if (ignoreArrayNulls || ignoreEmptyObjects) {
                            nonNull = 0;

                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass());
                                if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                    nonNull++;
                                }
                            }
                        }

                        Object result;

                        if (type.getRawClass() == byte[].class) {
                            byte[] array = (byte[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (byte) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == char[].class) {
                            char[] array = (char[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (char) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == short[].class) {
                            short[] array = (short[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (short) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == int[].class) {
                            int[] array = (int[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (int) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == long[].class) {
                            long[] array = (long[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (long) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == float[].class) {
                            float[] array = (float[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (float) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == double[].class) {
                            double[] array = (double[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (double) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else if (type.getRawClass() == boolean[].class) {
                            boolean[] array = (boolean[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                array[i] = (boolean) toObject(subElement, type.getRawClass());

                                i++;
                            }

                            result = array;
                        } else {
                            Object[] array = (Object[]) InitializationUtil.initializeArray(type.getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass());
                                if (!((ignoreArrayNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).isEmpty()) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).isEmpty()))) {
                                    array[i] = subObject;

                                    i++;
                                }
                            }

                            result = array;
                        }

                        return result;
                    } else {
                        throw new InvalidTypeException(type.getRawClass());
                    }
                } else {
                    throw new InvalidTypeException(type.getRawClass());
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

            if (object.getClass().getTypeParameters().length == 0) {
                JavaType type = TypeFactory.defaultInstance().constructSimpleType(object.getClass(), new JavaType[] { });

                for (Map.Entry<JavaType, TypeProcessor> typeProcessor : typeProcessors.entrySet()) {
                    if (typeProcessor.getKey().isTypeOrSuperTypeOf(type.getRawClass())) {
                        return typeProcessor.getValue().toElement(object);
                    }
                }
            }

            try {
                return ParsedPrimitive.from(object);
            } catch (ClassCastException e) {
                // Continue
            }

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

                for (Object item : List.copyOf((Collection<?>) object)) {
                    ParsedElement subElement = toElement(item);
                    if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                        arrayElement.add(subElement);
                    }
                }

                return arrayElement;
            } else if (object instanceof Map<?, ?>) {
                ParsedObject objectElement = ParsedObject.create();

                for (Map.Entry<?, ?> entry : Map.copyOf((Map<?, ?>) object).entrySet()) {
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
                    try {
                        defaultObject = InitializationUtil.initialize(object.getClass());
                    } catch (InitializationException e) {
                        if (BJSL.getLogger() != null) {
                            BJSL.getLogger().warning("Initialization of " + object.getClass().getSimpleName() + " failed, defaults will not be ignored.");

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
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
            List<Field> parentFields = getFields(clazz.getSuperclass());

            for (Field parentField : parentFields) {
                boolean overwritten = false;

                for (Field field : fields) {
                    if (field.getName().equals(parentField.getName())) {
                        overwritten = true;
                        break;
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