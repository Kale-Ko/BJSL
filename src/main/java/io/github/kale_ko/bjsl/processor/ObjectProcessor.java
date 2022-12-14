package io.github.kale_ko.bjsl.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.processor.annotations.DoSerialize;
import io.github.kale_ko.bjsl.processor.annotations.DontSerialize;

public class ObjectProcessor {
    public static <T> T toObject(ParsedElement element, Class<T> clazz) {
        if (element instanceof ParsedPrimitive) {
            if (clazz.isEnum()) {
                if (element instanceof ParsedPrimitive && element.asPrimitive().isString()) {
                    for (T value : clazz.getEnumConstants()) {
                        if (value.toString().equals(element.asPrimitive().asString())) {
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
                    throw new RuntimeException("\"element\" is not a primitive", e);
                }
            }
        } else if (!clazz.isArray() && !clazz.isAnonymousClass() && !clazz.isInterface() && !clazz.isRecord() && !clazz.isAnnotation()) {
            if (element instanceof ParsedObject) {
                for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
                    try {
                        if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                            T object = constructor.newInstance();

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
                                                field.set(object, toObject(element.asObject().get(field.getName()), field.getType()));
                                            }
                                        }
                                    }
                                } catch (IllegalArgumentException | IllegalAccessException e2) {
                                    e2.printStackTrace();
                                }
                            }

                            return object;
                        }
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                throw new RuntimeException("\"element\" is not an object");
            }

            throw new RuntimeException("No constructors for \"" + clazz.getSimpleName() + "\" found");
        } else {
            throw new RuntimeException("\"object\" is not a serializable type");
        }

        return null;
    }

    public static <T> T[] toArray(ParsedElement element, Class<T[]> clazz) {
        if (element instanceof ParsedArray) {
            Object[] array = new Object[element.asArray().getSize()];

            int i = 0;
            for (ParsedElement subElement : element.asArray().getValues()) {
                array[i] = toObject(subElement, clazz);

                i++;
            }

            return (T[]) array;
        } else {
            throw new RuntimeException("\"element\" is not an array");
        }
    }

    public static ParsedElement toElement(Object object) {
        try {
            return ParsedPrimitive.from(object);
        } catch (ClassCastException e) {
            if (object instanceof Enum<?> anEnum) {
                return ParsedPrimitive.fromString(anEnum.name());
            } else if (object instanceof Object[]) {
                ParsedArray arrayElement = ParsedArray.create();

                for (Object objects : (Object[]) object) {
                    arrayElement.add(toElement(objects));
                }

                return arrayElement;
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

                            if (shouldSerialize) {
                                objectElement.set(field.getName(), toElement(field.get(object)));
                            }
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e2) {
                        e2.printStackTrace();
                    }
                }

                return objectElement;
            } else {
                throw new RuntimeException("\"object\" is not a serializable type");
            }
        }
    }

    private static <T> List<Field> getFields(Class<T> clazz) {
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