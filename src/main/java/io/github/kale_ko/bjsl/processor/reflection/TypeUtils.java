package io.github.kale_ko.bjsl.processor.reflection;

import com.fasterxml.jackson.databind.JavaType;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for checking relations of class types
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public final class TypeUtils {
    private TypeUtils() {
    }

    /**
     * Checks if the given type is the same as the target type
     *
     * @param type       The type to check
     * @param targetType The target type to check against
     *
     * @return If the given type is the same as the target type
     *
     * @since 2.0.0
     */
    public static boolean isType(@NotNull JavaType type, @NotNull JavaType targetType) {
        return type.equals(targetType);
    }

    /**
     * Checks if the given type is the same or a super type of the target type
     *
     * @param type       The type to check
     * @param targetType The target type to check against
     *
     * @return If the given type is the same or a super type of the target type
     *
     * @since 2.0.0
     */
    public static boolean isTypeOrSuperTypeOf(@NotNull JavaType type, @NotNull JavaType targetType) {
        return getDistance(type, targetType) >= 0;
    }

    /**
     * Return the "sort value" for a type to two other types. See {@link java.util.Comparator}
     *
     * @param type        The type to check
     * @param targetTypeA The first target type to check against
     * @param targetTypeB The second target type to check against
     *
     * @return The "sort value" for a type to two other types
     *
     * @see java.util.Comparator
     */
    public static int sortDistance(@NotNull JavaType type, @NotNull JavaType targetTypeA, @NotNull JavaType targetTypeB) {
        return getDistance(type, targetTypeB) - getDistance(type, targetTypeA);
    }

    /**
     * Get the "distance" between two types. For every superclass the distance increases by 1. 0 means they are equal, -1 means it is not a superclass
     *
     * @param type       The type to check
     * @param targetType The target type to check against
     *
     * @return The "distance" between two types. For every superclass the distance increases by 1. 0 means they are equal, -1 means it is not a superclass
     *
     * @since 2.0.0
     */
    public static int getDistance(@NotNull JavaType type, @NotNull JavaType targetType) {
        if (isType(type, targetType)) {
            return 0;
        } else {
            int distance = 0;

            while ((type = type.getSuperClass()) != null) {
                distance++;
                if (isType(type, targetType)) {
                    return distance;
                }
            }

            return -1;
        }
    }
}
