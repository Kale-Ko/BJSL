package io.github.kale_ko.bjsl.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.processor.annotations.DontSerialize;

public class ObjectProcessor {
    public static <T> T toObject(ParsedElement element, Class<T> clazz) {
        return null;
    }

    public static ParsedObject toElement(Object object) {
        ParsedObject objectElement = ParsedObject.create();

        List<Field> fields = getFields(object.getClass());

        for (Field field : fields) {
            try {
                Boolean accessible = field.canAccess(object);

                field.setAccessible(true);

                Boolean shouldSerialize = true;

                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation.annotationType() == DontSerialize.class) {
                        shouldSerialize = false;
                    }
                }

                if (shouldSerialize) {
                    try {
                        objectElement.set(field.getName(), ParsedPrimitive.fromObject(field.get(object)));
                    } catch (ClassCastException e) {
                        if (Modifier.isStatic(field.get(object).getClass().getModifiers())) {
                            objectElement.set(field.getName(), toElement(field.get(object)));
                        }
                    }
                }

                field.setAccessible(accessible);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return objectElement;
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