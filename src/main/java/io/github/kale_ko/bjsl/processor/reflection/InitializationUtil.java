package io.github.kale_ko.bjsl.processor.reflection;

import io.github.kale_ko.bjsl.BJSL;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import sun.misc.Unsafe;

@SuppressWarnings("unchecked")
public class InitializationUtil {
    private InitializationUtil() {
    }

    public static <T> T initialize(Class<T> clazz) {
        try {
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                    return (T) constructor.newInstance();
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().warning(writer.toString());
            }
        }

        return null;
    }

    public static <T> T initializeUnsafe(Class<T> clazz) {
        T attempt = initialize(clazz);
        if (attempt != null) {
            return attempt;
        }

        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            return (T) unsafe.allocateInstance(clazz);
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            if (BJSL.getLogger() != null) {
                StringWriter writer = new StringWriter();
                new RuntimeException("Nonfatal error while parsing:", e).printStackTrace(new PrintWriter(writer));
                BJSL.getLogger().warning(writer.toString());
            }
        }

        return null;
    }

    public static <T> Object initializeArray(Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }
}