package io.github.kale_ko.bjsl.processor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.processor.annotations.DoSerialize;
import io.github.kale_ko.bjsl.processor.annotations.DontSerialize;

public class ObjectProcessor {
    protected boolean ignoreNulls;

    public ObjectProcessor() {
        this(false);
    }

    public ObjectProcessor(boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
    }

    @SuppressWarnings("unchecked")
    public <T> T toObject(ParsedElement element, Class<T> clazz) {
        try {
            if (element instanceof ParsedPrimitive) {
                if (clazz.isEnum()) {
                    if (element instanceof ParsedPrimitive && element.asPrimitive().isString()) {
                        for (T value : clazz.getEnumConstants()) {
                            if (value.equals(element.asPrimitive().asString())) {
                                return value;
                            }
                        }
                    } else {
                        throw new RuntimeException("Element is not a enum");
                    }
                } else {
                    try {
                        Object object = element.asPrimitive().get();

                        if (clazz == String.class) {
                            return clazz.cast((String) object);
                        } else if (clazz == Byte.class || clazz == byte.class) {
                            return (T) (Byte) (byte) (long) object;
                        } else if (clazz == Character.class || clazz == char.class) {
                            return (T) (Character) (char) (long) object;
                        } else if (clazz == Short.class || clazz == short.class) {
                            return (T) (Short) (short) (long) object;
                        } else if (clazz == Integer.class || clazz == int.class) {
                            return (T) (Integer) (int) (long) object;
                        } else if (clazz == Long.class || clazz == long.class) {
                            return (T) (Long) (long) object;
                        } else if (clazz == Float.class || clazz == float.class) {
                            return (T) (Float) (float) (double) object;
                        } else if (clazz == Double.class || clazz == double.class) {
                            return (T) (Double) (double) object;
                        } else if (clazz == Boolean.class || clazz == boolean.class) {
                            return (T) (Boolean) (boolean) object;
                        } else {
                            return clazz.cast(object);
                        }
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Element could not be cast to \"" + clazz.getName() + "\"", e);
                    }
                }
            } else if (!clazz.isAnonymousClass() && !clazz.isRecord() && !clazz.isAnnotation()) {
                if (element instanceof ParsedObject) {
                    if (Map.class.isAssignableFrom(clazz)) {
                        Map<String, Object> object = null;

                        if (!clazz.isInterface()) {
                            try {
                                for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                        object = (Map<String, Object>) constructor.newInstance();

                                        break;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                BJSL.getLogger().warning(writer.toString());
                            }

                            if (object == null) {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (Map<String, Object>) unsafe.allocateInstance(clazz);
                                } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
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
                                object = (Map<String, Object>) unsafe.allocateInstance(clazz);
                            } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                BJSL.getLogger().warning(writer.toString());
                            }
                        }

                        if (object != null) {
                            for (Map.Entry<String, ParsedElement> entry : element.asObject().getEntries()) {
                                Object subObject = toObject(entry.getValue(), Object.class);
                                object.put(entry.getKey(), subObject);
                            }

                            return (T) object;
                        } else {
                            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else if (!clazz.isInterface()) {
                        T object = null;

                        try {
                            for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                                if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                    object = constructor.newInstance();

                                    break;
                                }
                            }
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                            StringWriter writer = new StringWriter();
                            new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                            BJSL.getLogger().warning(writer.toString());
                        }

                        if (object == null) {
                            try {
                                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                unsafeField.setAccessible(true);
                                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                object = (T) unsafe.allocateInstance(clazz);
                            } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                BJSL.getLogger().warning(writer.toString());
                            }
                        }

                        if (object != null) {
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

                                        if (shouldSerialize) {
                                            if (element.asObject().has(field.getName())) {
                                                Object subObject = toObject(element.asObject().get(field.getName()), field.getType());
                                                field.set(object, subObject);
                                            }
                                        }
                                    }
                                } catch (IllegalArgumentException | IllegalAccessException e) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
                                }
                            }

                            return object;
                        } else {
                            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else {
                        throw new RuntimeException("clazz is not a serializable type (" + clazz + ")");
                    }
                } else if (element instanceof ParsedArray) {
                    if (Collection.class.isAssignableFrom(clazz)) {
                        Collection<Object> object = null;

                        if (!clazz.isInterface()) {
                            try {
                                for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                        object = (Collection<Object>) constructor.newInstance();

                                        break;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                BJSL.getLogger().warning(writer.toString());
                            }

                            if (object == null) {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (Collection<Object>) unsafe.allocateInstance(clazz);
                                } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                                    StringWriter writer = new StringWriter();
                                    new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                    BJSL.getLogger().warning(writer.toString());
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
                                StringWriter writer = new StringWriter();
                                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                                BJSL.getLogger().warning(writer.toString());
                            }
                        }

                        if (object != null) {
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                object.add(toObject(subElement, Object.class));
                            }

                            return (T) object;
                        } else {
                            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else if (!clazz.isInterface()) {
                        Object[] array = new Object[element.asArray().getSize()];

                        int i = 0;
                        for (ParsedElement subElement : element.asArray().getValues()) {
                            array[i] = toObject(subElement, clazz);

                            i++;
                        }

                        return (T) array;
                    } else {
                        throw new RuntimeException("clazz is not a serializable type (" + clazz + ")");
                    }
                } else {
                    throw new RuntimeException("Element is not an object or array");
                }
            } else {
                throw new RuntimeException("clazz is not a serializable type (" + clazz + ")");
            }
        } catch (RuntimeException e) {
            StringWriter writer = new StringWriter();
            new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
            BJSL.getLogger().severe(writer.toString());

            throw e;
        }

        throw new RuntimeException("Something went horribly wrong while parsing");
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
                        arrayElement.add(toElement(item));
                    }

                    return arrayElement;
                } else if (object instanceof Object[]) {
                    ParsedArray arrayElement = ParsedArray.create();

                    for (Object objects : (Object[]) object) {
                        arrayElement.add(toElement(objects));
                    }

                    return arrayElement;
                } else if (object instanceof Map<?, ?> map) {
                    ParsedObject objectElement = ParsedObject.create();

                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        objectElement.set(entry.getKey().toString(), toElement(entry.getValue()));
                    }

                    return objectElement;
                } else if (object instanceof Object) {
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

                                if (ignoreNulls && field.get(object) == null) {
                                    shouldSerialize = false;
                                }

                                if (shouldSerialize) {
                                    objectElement.set(field.getName(), toElement(field.get(object)));
                                }
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e2) {
                            StringWriter writer = new StringWriter();
                            new RuntimeException("Nonfatal error while parsing:", e2).printStackTrace(new PrintWriter(writer));
                            BJSL.getLogger().warning(writer.toString());
                        }
                    }

                    return objectElement;
                } else {
                    throw new RuntimeException("Object is not a serializable type");
                }
            }
        } catch (RuntimeException e) {
            StringWriter writer = new StringWriter();
            new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
            BJSL.getLogger().severe(writer.toString());

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