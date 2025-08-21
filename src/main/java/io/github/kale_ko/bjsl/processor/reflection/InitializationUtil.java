package io.github.kale_ko.bjsl.processor.reflection;

import io.github.kale_ko.bjsl.processor.exception.InitializationException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for initializing objects and arrays from a class type
 *
 * @version 2.0.0
 * @since 1.7.0
 */
@SuppressWarnings("unchecked")
public final class InitializationUtil {
    private InitializationUtil() {
    }

    private static boolean allowInitializingNonStaticMemberParents = false;

    /**
     * Allow initializing non-static member parents
     * <p>
     * This is an unsafe operation that enables initialization of non-static inner classes.
     * Normally, this is disabled to prevent accidentally initializing classes that require
     * a parent instance. Only enable this if you are certain your code requires it.
     * <p>
     * AINSMP stands for "Allow Initializing Non-Static Member Parents"
     *
     * @since 2.0.0
     */
    public static void unsafeAINSMP() {
        allowInitializingNonStaticMemberParents = true;
    }

    /**
     * Initialize a class safely using a 0-args constructor
     * <p>
     * This method attempts to create a new instance of the specified class using
     * a no-argument constructor. For non-static inner classes, it will attempt
     * to initialize the parent class as well (if allowed via {@link #unsafeAINSMP()}).
     *
     * @param clazz The class to initialize
     * @param <T>   The type to initialize
     *
     * @return The initialized instance of clazz
     *
     * @throws InitializationException if the class cannot be initialized
     */
    public static <T> @NotNull T initialize(@NotNull Class<T> clazz) {
        try {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                if (!allowInitializingNonStaticMemberParents) {
                    throw new InitializationException(new RuntimeException("Refusing to initialize a non-static inner class. " +
                            "This usually indicates you forgot to add the 'static' keyword to your class declaration. " +
                            "If you intentionally need to initialize non-static inner classes, call InitializationUtil.unsafeAINSMP() first."));
                }

                // Non-static member constructor
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 1) {
                        Object object = initialize(constructor.getParameterTypes()[0]);
                        return (T) constructor.newInstance(object);
                    }
                }
            } else {
                // Normal constructor
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if ((constructor.canAccess(null) || constructor.trySetAccessible()) && constructor.getParameterTypes().length == 0) {
                        return (T) constructor.newInstance();
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
            throw new InitializationException(e);
        }

        throw new InitializationException(clazz);
    }

    /**
     * Initialize an array safely using {@link Array#newInstance(Class, int)}
     *
     * @param clazz  The class to initialize
     * @param length The length of the new array
     * @param <T>    The type to initialize
     *
     * @return The initialized array instance of clazz with the passed length
     */
    public static <T> @NotNull T[] initializeArray(@NotNull Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    /**
     * Initialize a primitive array safely using {@link Array#newInstance(Class, int)}
     *
     * @param clazz  The class to initialize
     * @param length The length of the new array
     * @param <T>    The type to initialize
     *
     * @return The initialized array instance of clazz with the passed length
     */
    public static <T> @NotNull Object initializePrimitiveArray(@NotNull Class<T> clazz, int length) {
        return Array.newInstance(clazz, length);
    }
}