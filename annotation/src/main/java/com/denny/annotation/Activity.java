package com.denny.annotation;

/**
 * Created by caidong on 2018/6/26.
 */
public @interface Activity {

    boolean extend() default true;

    LaunchMode launchMode() default LaunchMode.None;

    String taskAffinity() default Define.Null;

    @BooleanType
    String excludeFromRecents() default Define.Null;

    String theme() default Define.Null;

    String process() default Define.Null;

    @BooleanType
    String noHistory() default Define.Null;

    String icon() default Define.Null;

    String multiprocess() default Define.Null;

    String configChanges() default Define.Null;

    @BooleanType
    String hardwareAccelerated() default Define.Null;

    String label() default Define.Null;

    @BooleanType
    String exported() default Define.Null;

    String screenOrientation() default Define.Null;

    String windowSoftInputMode() default Define.Null;

    @BooleanType
    String enable();
}
