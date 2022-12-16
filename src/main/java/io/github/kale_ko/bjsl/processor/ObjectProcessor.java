package io.github.kale_ko.bjsl.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
                        throw new RuntimeException("\"element\" is not a enum");
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
                        throw new RuntimeException("\"element\" could not be cast to \"" + clazz.getName() + "\"", e);
                    }
                }
            } else if (!clazz.isArray() && !clazz.isAnonymousClass() && !clazz.isInterface() && !clazz.isRecord() && !clazz.isAnnotation()) {
                if (element instanceof ParsedObject) {
                    if (clazz.isAssignableFrom(Map.class)) {
                        Map<String, Object> object = null;

                        try {
                            for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                                if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                    object = (Map<String, Object>) constructor.newInstance();

                                    break;
                                }
                            }
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                            if (BJSL.getLoggerEnabled()) {
                                BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                            }
                        }

                        if (object == null) {
                            try {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (Map<String, Object>) unsafe.allocateInstance(clazz);
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                            } catch (InstantiationException e) {
                                if (BJSL.getLoggerEnabled()) {
                                    BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                                }
                            }
                        }

                        if (object != null) {
                            for (Map.Entry<String, ParsedElement> entry : element.asObject().getEntries()) {
                                try {
                                    Object subObject = toObject(entry.getValue(), null);
                                    object.put(entry.getKey().toString(), subObject);
                                } catch (RuntimeException e) {
                                }
                            }
                        } else {
                            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else {
                        T object = null;

                        try {
                            for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                                if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                    object = constructor.newInstance();

                                    break;
                                }
                            }
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                            if (BJSL.getLoggerEnabled()) {
                                BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                            }
                        }

                        if (object == null) {
                            try {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (T) unsafe.allocateInstance(clazz);
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                            } catch (InstantiationException e) {
                                if (BJSL.getLoggerEnabled()) {
                                    BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                                }
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
                                                try {
                                                    Object subObject = toObject(element.asObject().get(field.getName()), field.getType());
                                                    field.set(object, subObject);
                                                } catch (RuntimeException e) {
                                                }
                                            }
                                        }
                                    }
                                } catch (IllegalArgumentException | IllegalAccessException e) {
                                    if (BJSL.getLoggerEnabled()) {
                                        BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                                    }
                                }
                            }

                            return object;
                        } else {
                            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    }
                } else if (element instanceof ParsedArray) {
                    if (clazz.isAssignableFrom(Collection.class)) {
                        Collection<Object> object = null;

                        try {
                            for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                                if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                                    object = (Collection<Object>) constructor.newInstance();

                                    break;
                                }
                            }
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                            if (BJSL.getLoggerEnabled()) {
                                BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                            }
                        }

                        if (object == null) {
                            try {
                                try {
                                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                                    unsafeField.setAccessible(true);
                                    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                                    object = (Collection<Object>) unsafe.allocateInstance(clazz);
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    e.printStackTrace();
                                }
                            } catch (InstantiationException e) {
                                if (BJSL.getLoggerEnabled()) {
                                    BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                                }
                            }
                        }

                        if (object != null) {
                            for (ParsedElement subElement : element.asArray().getValues()) {
                                try {
                                    object.add(toObject(subElement, clazz));
                                } catch (RuntimeException e) {
                                }
                            }

                            return (T) object;
                        } else {
                            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found and unsafe initialization failed");
                        }
                    } else {
                        Object[] array = new Object[element.asArray().getSize()];

                        int i = 0;
                        for (ParsedElement subElement : element.asArray().getValues()) {
                            try {
                                array[i] = toObject(subElement, clazz);
                            } catch (RuntimeException e) {
                            }

                            i++;
                        }

                        return (T) array;
                    }
                } else {
                    throw new RuntimeException("\"element\" is not an object or array");
                }
            } else {
                throw new RuntimeException("\"clazz\" is not a serializable type (" + clazz + ")");
            }
        } catch (RuntimeException e) {
            if (BJSL.getLoggerEnabled()) {
                BJSL.getLogger().severe("Error while parsing:\n" + e);
            }

            throw e;
        }

        return null;
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
                            if (BJSL.getLoggerEnabled()) {
                                BJSL.getLogger().warning("Nonfatal error while parsing:\n" + e);
                            }
                        }
                    }

                    return objectElement;
                } else {
                    throw new RuntimeException("\"object\" is not a serializable type");
                }
            }
        } catch (RuntimeException e) {
            if (BJSL.getLoggerEnabled()) {
                BJSL.getLogger().severe("Error while parsing:\n" + e);
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