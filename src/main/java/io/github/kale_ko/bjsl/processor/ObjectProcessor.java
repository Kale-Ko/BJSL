package io.github.kale_ko.bjsl.processor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

public class ObjectProcessor {
    protected boolean ignoreNulls;
    protected boolean ignoreEmptyObjects;
    protected boolean ignoreDefaults;

    protected boolean caseSensitiveEnums;

    protected ObjectProcessor(boolean ignoreNulls, boolean ignoreEmptyObjects, boolean ignoreDefaults, boolean caseSensitiveEnums) {
        this.ignoreNulls = ignoreNulls;
        this.ignoreEmptyObjects = ignoreEmptyObjects;
        this.ignoreDefaults = ignoreDefaults;

        this.caseSensitiveEnums = caseSensitiveEnums;
    }

    public static class Builder {
        protected boolean ignoreNulls = false;
        protected boolean ignoreEmptyObjects = false;
        protected boolean ignoreDefaults = false;

        protected boolean caseSensitiveEnums = false;

        public Builder() {}

        public boolean getIgnoreNulls() {
            return this.ignoreNulls;
        }

        public Builder setIgnoreNulls(boolean value) {
            this.ignoreNulls = value;

            return this;
        }

        public boolean getIgnoreEmptyObjects() {
            return this.ignoreEmptyObjects;
        }

        public Builder setIgnoreEmptyObjects(boolean value) {
            this.ignoreEmptyObjects = value;

            return this;
        }

        public boolean getIgnoreDefaults() {
            return this.ignoreDefaults;
        }

        public Builder setIgnoreDefaults(boolean value) {
            this.ignoreDefaults = value;

            return this;
        }

        public boolean getCaseSensitiveEnums() {
            return this.caseSensitiveEnums;
        }

        public Builder setCaseSensitiveEnums(boolean value) {
            this.caseSensitiveEnums = value;

            return this;
        }

        public ObjectProcessor build() {
            return new ObjectProcessor(this.ignoreNulls, this.ignoreEmptyObjects, this.ignoreDefaults, this.caseSensitiveEnums);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T toObject(ParsedElement element, Class<T> clazz) {
        return (T) toObject(element, TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[] {}));
    }

    public Object toObject(ParsedElement element, Type type) {
        return toObject(element, TypeFactory.defaultInstance().constructType(type));
    }

    @SuppressWarnings("unchecked")
    public Object toObject(ParsedElement element, JavaType type) {
        try {
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
                            if (object.getClass() == Character.class) {
                                return (byte) (char) object;
                            } else if (object.getClass() == Short.class) {
                                return (byte) (short) object;
                            } else if (object.getClass() == Integer.class) {
                                return (byte) (int) object;
                            } else if (object.getClass() == Long.class) {
                                return (byte) (long) object;
                            } else {
                                return (byte) object;
                            }
                        } else if (type.getRawClass() == Character.class || type.getRawClass() == char.class) {
                            if (object.getClass() == Byte.class) {
                                return (char) (byte) object;
                            } else if (object.getClass() == Short.class) {
                                return (char) (short) object;
                            } else if (object.getClass() == Integer.class) {
                                return (char) (int) object;
                            } else if (object.getClass() == Long.class) {
                                return (char) (long) object;
                            } else {
                                return (char) object;
                            }
                        } else if (type.getRawClass() == Short.class || type.getRawClass() == short.class) {
                            if (object.getClass() == Byte.class) {
                                return (short) (byte) object;
                            } else if (object.getClass() == Character.class) {
                                return (short) (char) object;
                            } else if (object.getClass() == Integer.class) {
                                return (short) (int) object;
                            } else if (object.getClass() == Long.class) {
                                return (short) (long) object;
                            } else {
                                return (short) object;
                            }
                        } else if (type.getRawClass() == Integer.class || type.getRawClass() == int.class) {
                            if (object.getClass() == Byte.class) {
                                return (int) (byte) object;
                            } else if (object.getClass() == Character.class) {
                                return (int) (char) object;
                            } else if (object.getClass() == Short.class) {
                                return (int) (short) object;
                            } else if (object.getClass() == Long.class) {
                                return (int) (long) object;
                            } else {
                                return (int) object;
                            }
                        } else if (type.getRawClass() == Long.class || type.getRawClass() == long.class) {
                            if (object.getClass() == Byte.class) {
                                return (long) (byte) object;
                            } else if (object.getClass() == Character.class) {
                                return (long) (char) object;
                            } else if (object.getClass() == Short.class) {
                                return (long) (short) object;
                            } else if (object.getClass() == Integer.class) {
                                return (long) (int) object;
                            } else {
                                return (long) object;
                            }
                        } else if (type.getRawClass() == Float.class || type.getRawClass() == float.class) {
                            if (object.getClass() == Double.class) {
                                return (float) (double) object;
                            } else {
                                return (float) object;
                            }
                        } else if (type.getRawClass() == Double.class || type.getRawClass() == double.class) {
                            if (object.getClass() == Float.class) {
                                return (double) (float) object;
                            } else {
                                return (double) object;
                            }
                        } else if (type.getRawClass() == Boolean.class || type.getRawClass() == boolean.class) {
                            return (boolean) object;
                        } else {
                            return type.getRawClass().cast(object);
                        }
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Element could not be cast to \"" + type.getRawClass().getSimpleName() + "\"", e);
                    }
                }
            } else if (!type.getRawClass().isAnonymousClass() && !type.getRawClass().isRecord() && !type.getRawClass().isAnnotation()) {
                if (element instanceof ParsedObject) {
                    if (type instanceof MapType) {
                        Map<String, Object> object = null;

                        if (!type.getRawClass().isInterface()) {
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

                            if (object == null) {
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
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
                            throw new RuntimeException("No constructors for \"" + type.getRawClass().getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else if (!type.getRawClass().isInterface()) {
                        Object object = null;

                        try {
                            for (Constructor<?> constructor : (Constructor<?>[]) type.getRawClass().getConstructors()) {
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

                        if (object == null) {
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
                            throw new RuntimeException("No constructors for \"" + type.getRawClass().getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else {
                        throw new RuntimeException("Type \"" + type.getRawClass().getSimpleName() + "\" is not serializable");
                    }
                } else if (element instanceof ParsedArray) {
                    if (type instanceof CollectionType) {
                        Collection<Object> object = null;

                        if (!type.getRawClass().isInterface()) {
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

                            if (object == null) {
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
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
                            throw new RuntimeException("No constructors for \"" + type.getRawClass().getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else if (type instanceof ArrayType) {
                        try {
                            int nonNull = element.asArray().getSize();

                            if (ignoreNulls || ignoreEmptyObjects) {
                                nonNull = 0;

                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass().componentType());
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                        nonNull++;
                                    }
                                }
                            }

                            Object[] array = (Object[]) Array.newInstance(((ArrayType) type).getContentType().getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass().componentType());
                                if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                    array[i] = subObject;

                                    i++;
                                }
                            }

                            return array;
                        } catch (NegativeArraySizeException e) {
                            throw new RuntimeException("Error while parsing: ", e);
                        }
                    } else if (!type.getRawClass().isInterface()) {
                        try {
                            int nonNull = element.asArray().getSize();

                            if (ignoreNulls || ignoreEmptyObjects) {
                                nonNull = 0;

                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass().componentType());
                                    if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                        nonNull++;
                                    }
                                }
                            }

                            Object[] array = (Object[]) Array.newInstance(type.getRawClass().componentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass().componentType());
                                if (!((ignoreNulls && subObject == null) || (ignoreEmptyObjects && subObject instanceof Object[] && ((Object[]) subObject).length == 0) || (ignoreEmptyObjects && subObject instanceof Collection<?> && ((Collection<?>) subObject).size() == 0) || (ignoreEmptyObjects && subObject instanceof Map<?, ?> && ((Map<?, ?>) subObject).size() == 0))) {
                                    array[i] = subObject;

                                    i++;
                                }
                            }

                            return array;
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
            }

            throw e;
        }
    }

    public ParsedElement toElement(Object object) {
        try {
            try {
                return ParsedPrimitive.from(object);
            } catch (ClassCastException e2) {
                if (object instanceof Enum<?> anEnum) {
                    return ParsedPrimitive.fromString(anEnum.name());
                } else if (object instanceof Object[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : (Object[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Collection<?> list) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : list) {
                        ParsedElement subElement = toElement(item);
                        if (!((ignoreNulls && subElement.isPrimitive() && subElement.asPrimitive().isNull()) || (ignoreEmptyObjects && subElement.isArray() && subElement.asArray().getSize() == 0) || (ignoreEmptyObjects && subElement.isObject() && subElement.asObject().getSize() == 0))) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Map<?, ?> map) {
                    ParsedObject objectElement = ParsedObject.create();

                    for (Map.Entry<?, ?> entry : map.entrySet()) {
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
                            for (Constructor<?> constructor : (Constructor<?>[]) object.getClass().getConstructors()) {
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

                        if (defaultObject == null) {
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
                            BJSL.getLogger().warning("No constructors for \"" + object.getClass().getSimpleName() + "\" found and unsafe initialization failed, defaults will not be ignored");
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

                                if (ignoreDefaults && (subElement.isPrimitive() && subElement.asPrimitive().get().equals(field.get(defaultObject)))) {
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
                                                if (field.get(object).equals(defaultAnnotation.stringValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.BYTE && defaultAnnotation.byteValue() != Byte.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.byteValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.CHAR && defaultAnnotation.charValue() != Character.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.charValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.SHORT && defaultAnnotation.shortValue() != Short.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.shortValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.INTEGER && defaultAnnotation.intValue() != Integer.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.intValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.LONG && defaultAnnotation.longValue() != Long.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.longValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.DOUBLE && defaultAnnotation.doubleValue() != Double.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.doubleValue())) {
                                                    shouldSerialize = false;
                                                }
                                            } else if (subElement.asPrimitive().getType() == ParsedPrimitive.PrimitiveType.FLOAT && defaultAnnotation.floatValue() != Float.MIN_VALUE) {
                                                if (field.get(object).equals(defaultAnnotation.floatValue())) {
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
            }

            throw e;
        }
    }

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