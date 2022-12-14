package io.github.kale_ko.bjsl.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target({ ElementType.FIELD })
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Inherited()
public @interface DoSerialize {}