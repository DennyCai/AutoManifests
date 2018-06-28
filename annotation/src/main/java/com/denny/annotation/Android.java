package com.denny.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Android {

    LaunchMode launchMode() default LaunchMode.None;

    String taskAffinity() default Define.Null;

    String excludeFromRecents() default Define.Null;

    int theme() default Define.NoId;

//    int themeRes() default Define.NoId;

    String process() default Define.Null;

    String noHistory() default Define.Null;

    String icon() default Define.Null;

//    int iconRes() default Define.NoId;

    String multiprocess() default Define.Null;

    String configChanges() default Define.Null;

    String hardwareAccelerated() default Define.Null;

    String label() default Define.Null;

//    int labelRes() default Define.NoId;

    String exported() default Define.Null;

    String screenOrientation() default Define.Null;

    String windowSoftInputMode() default Define.Null;
}
