package com.tinecommerce.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AdminVisible {
    public boolean tableVisible() default true;
    public int order() default 1000;
    //for relations:
    public String className() default "";
    public String mappedBy() default "";
    public boolean viewOnly() default false;
}
