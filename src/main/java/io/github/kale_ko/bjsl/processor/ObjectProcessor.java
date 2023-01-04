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
import io.github.kale_ko.bjsl.processor.annotations.DoSerialize;
import io.github.kale_ko.bjsl.processor.annotations.DontSerialize;

public class ObjectProcessor {
    protected boolean ignoreNulls;

    protected boolean caseSensitiveEnums;

    public ObjectProcessor() {
        this(false);
    }

    public ObjectProcessor(boolean ignoreNulls) {
        this(ignoreNulls, false);
    }

    public ObjectProcessor(boolean ignoreNulls, boolean caseSensitiveEnums) {
        this.ignoreNulls = ignoreNulls;

        this.caseSensitiveEnums = caseSensitiveEnums;
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
                    if (element instanceof ParsedPrimitive && element.asPrimitive().isString()) {
                        for (Object value : type.getRawClass().getEnumConstants()) {
                            if ((caseSensitiveEnums && ((Enum<?>) value).name().equals(element.asPrimitive().asString())) || (!caseSensitiveEnums && ((Enum<?>) value).name().equalsIgnoreCase(element.asPrimitive().asString()))) {
                                return value;
                            }
                        }

                        BJSL.getLogger().warning("Unknown enum value \"" + element.asPrimitive().asString() + "\" for type \"" + type.getRawClass().getSimpleName() + "\"");

                        return null;
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
                            return (byte) (long) object;
                        } else if (type.getRawClass() == Character.class || type.getRawClass() == char.class) {
                            return (char) (long) object;
                        } else if (type.getRawClass() == Short.class || type.getRawClass() == short.class) {
                            return (short) (long) object;
                        } else if (type.getRawClass() == Integer.class || type.getRawClass() == int.class) {
                            return (int) (long) object;
                        } else if (type.getRawClass() == Long.class || type.getRawClass() == long.class) {
                            return (long) object;
                        } else if (type.getRawClass() == Float.class || type.getRawClass() == float.class) {
                            return (float) (double) object;
                        } else if (type.getRawClass() == Double.class || type.getRawClass() == double.class) {
                            return (double) object;
                        } else if (type.getRawClass() == Boolean.class || type.getRawClass() == boolean.class) {
                            return (boolean) object;
                        } else {
                            return type.getRawClass().cast(object);
                        }
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Element could not be cast to \"" + type.getRawClass().getSimpleName() + "\"");
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
                                if (!(ignoreNulls && subObject == null)) {
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
                            for (Constructor<Object> constructor : (Constructor<Object>[]) type.getRawClass().getConstructors()) {
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
                                            if (annotation.annotationType() == DoSerialize.class) {
                                                shouldSerialize = true;
                                            } else if (annotation.annotationType() == DontSerialize.class) {
                                                shouldSerialize = false;
                                            }
                                        }

                                        if (field.getName().startsWith("this$")) {
                                            shouldSerialize = false;
                                        }

                                        if (ignoreNulls && field.get(object) == null) {
                                            shouldSerialize = false;
                                        }

                                        if (shouldSerialize) {
                                            Object subObject = toObject(element.asObject().get(field.getName()), field.getGenericType());
                                            field.set(object, subObject);
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
                                if (!(ignoreNulls && subObject == null)) {
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

                            if (ignoreNulls) {
                                nonNull = 0;

                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass().componentType());
                                    if (subObject != null) {
                                        nonNull++;
                                    }
                                }
                            }

                            Object[] array = (Object[]) Array.newInstance(((ArrayType) type).getContentType().getRawClass(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass().componentType());
                                if (!(ignoreNulls && subObject == null)) {
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

                            if (ignoreNulls) {
                                nonNull = 0;

                                for (ParsedElement subElement : element.asArray().getValues()) {
                                    Object subObject = toObject(subElement, type.getRawClass().componentType());
                                    if (subObject != null) {
                                        nonNull++;
                                    }
                                }
                            }

                            Object[] array = (Object[]) Array.newInstance(type.getRawClass().componentType(), nonNull);

                            int i = 0;
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                Object subObject = toObject(subElement, type.getRawClass().componentType());
                                if (!(ignoreNulls && subObject == null)) {
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
            } catch (ClassCastException e) {
                if (object instanceof Enum<?> anEnum) {
                    return ParsedPrimitive.fromString(anEnum.name());
                } else if (object instanceof Collection<?> list) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : list) {
                        ParsedElement subElement = toElement(item);
                        if (!(subElement.isPrimitive() && subElement.asPrimitive().isNull())) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Object[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object item : (Object[]) object) {
                        ParsedElement subElement = toElement(item);
                        if (!(subElement.isPrimitive() && subElement.asPrimitive().isNull())) {
                            arrayElement.add(subElement);
                        }
                    }

                    return arrayElement;
                } else if (object instanceof Map<?, ?> map) {
                    ParsedObject objectElement = ParsedObject.create();

                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        ParsedElement subElement = toElement(entry.getValue());
                        if (!(subElement.isPrimitive() && subElement.asPrimitive().isNull())) {
                            objectElement.set(entry.getKey().toString(), subElement);
                        }
                    }

                    return objectElement;
                } else {
                    ParsedObject objectElement = ParsedObject.create();

                    List<Field> fields = getFields(object.getClass());

                    for (Field field : fields) {
                        try {
                            if (!Modifier.isStatic(field.getModifiers()) && (field.canAccess(object) || field.trySetAccessible())) {
                                Boolean shouldSerialize = !Modifier.isTransient(field.getModifiers());

                                for (Annotation annotation : field.getDeclaredAnnotations()) {
                                    if (annotation.annotationType() == DoSerialize.class) {
                                        shouldSerialize = true;
                                    } else if (annotation.annotationType() == DontSerialize.class) {
                                        shouldSerialize = false;
                                    }
                                }

                                if (field.getName().startsWith("this$")) {
                                    shouldSerialize = false;
                                }

                                if (ignoreNulls && field.get(object) == null) {
                                    shouldSerialize = false;
                                }

                                if (shouldSerialize) {
                                    ParsedElement subElement = toElement(field.get(object));
                                    if (!(subElement.isPrimitive() && subElement.asPrimitive().isNull())) {
                                        objectElement.set(field.getName(), subElement);
                                    }
                                }
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e2) {
                            if (BJSL.getLogger() != null) {
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e2).printStackTrace(new PrintWriter(writer));
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