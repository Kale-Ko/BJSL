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
public class InitializationUtil {
    private InitializationUtil() {
    }

    private static boolean allowInitializingNonStaticMemberParents = false;

    /**
     * Allow initializing non-static member parents
     *
     * @since 2.0.0
     */
    public static void unsafeAINSMP() {
        allowInitializingNonStaticMemberParents = true;
    }

    /**
     * Initialize a class safely using a 0-args constructor
     *
     * @param clazz The class to initialize
     * @param <T>   The type to initialize
     *
     * @return The initialized instance of clazz
     */
    public static <T> @NotNull T initialize(@NotNull Class<T> clazz) {
        try {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                if (!allowInitializingNonStaticMemberParents) {
                    throw new InitializationException(new RuntimeException("Refusing to initialize a non-static member's parent, it is likely you forgot to add the 'static' keyword to your class. If this is not the case call `InitializationUtil#allowInitializingNonStaticMemberParents()`"));
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
    public static <T> @NotNull Object initializeArray(@NotNull Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }
}