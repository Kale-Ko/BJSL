package io.github.kale_ko.bjsl.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    public String stringValue() default "";

    public byte byteValue() default Byte.MIN_VALUE;

    public char charValue() default Character.MIN_VALUE;

    public short shortValue() default Short.MIN_VALUE;

    public int intValue() default Integer.MIN_VALUE;

    public long longValue() default Long.MIN_VALUE;

    public float floatValue() default Float.MIN_VALUE;

    public double doubleValue() default Double.MIN_VALUE;
}