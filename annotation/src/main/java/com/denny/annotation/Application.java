package com.denny.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by caidong on 2018/6/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Application {

    String icon();

    String label();

    String theme();

    String roundIcon() default Define.Null;

    @BooleanType
    String allowBackup() default Define.Null;

    @BooleanType
    String largeHeap() default Define.Null;

    @BooleanType
    String supportsRtl() default Define.Null;
}
