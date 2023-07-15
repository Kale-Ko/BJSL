package io.github.kale_ko.bjsl.processor.reflection;

import io.github.kale_ko.bjsl.BJSL;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import sun.misc.Unsafe;

/**
 * A utility class for initializing objects and arrays from a class type
 * <p>
 * Objects can be initialized using 0-args constructors or using {@link sun.misc.Unsafe}
 *
 * @version 1.7.0
 * @since 1.7.0
 */
@SuppressWarnings("unchecked")
public class InitializationUtil {
    private InitializationUtil() {
    }

    /**
     * Initialize a class safely using a 0-args constructor
     *
     * @param clazz The class to initialize
     * @param <T>   The type to initialize
     *
     * @return The initialized instance of clazz
     */
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

    /**
     * Initialize a class unsafely using {@link sun.misc.Unsafe}
     * <p>
     * Will attempt to use {@link #initialize(Class)} before resorting to {@link sun.misc.Unsafe}
     *
     * @param clazz The class to initialize
     * @param <T>   The type to initialize
     *
     * @return The initialized instance of clazz
     */
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

    /**
     * Initialize a class safely using {@link Array#newInstance(Class, int)}
     *
     * @param clazz  The class to initialize
     * @param length The length of the new array
     * @param <T>    The type to initialize
     *
     * @return The initialized array instance of clazz with the passed length
     */
    public static <T> Object initializeArray(Class<T> clazz, int length) {
        return Array.newInstance(clazz, length);
    }
}