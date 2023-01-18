package io.github.kale_ko.bjsl.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    public String stringValue() default "";

    public String[] stringArrayValue() default "";

    public byte byteValue() default 0;

    public byte[] byteArrayValue() default 0;

    public char charValue() default 0;

    public char[] charArrayValue() default 0;

    public short shortValue() default 0;

    public short[] shortArrayValue() default 0;

    public int intValue() default 0;

    public int[] intArrayValue() default 0;

    public long longValue() default 0;

    public long[] longArrayValue() default 0;

    public float floatValue() default 0;

    public float[] floatArrayValue() default 0;

    public double doubleValue() default 0;

    public double[] doubleArrayValue() default 0;

    public boolean booleanValue() default false;

    public boolean[] booleanArrayValue() default false;
}