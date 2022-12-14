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
        return null;
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