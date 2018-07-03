package com.denny.annotation;

/**
 * Created by caidong on 2018/6/26.
 */
@ExtendsFrom({Define.Activity.APP, Define.Activity.APPCOMPAT})
public @interface Activity {

    LaunchMode launchMode() default LaunchMode.None;

    String taskAffinity() default Define.Null;

    @Equals({ "true", "false"})
    String excludeFromRecents() default Define.Null;

    String theme() default Define.Null;

    String process() default Define.Null;

    @Equals({ "true", "false"})
    String noHistory() default Define.Null;

    String icon() default Define.Null;

    String multiprocess() default Define.Null;

    String configChanges() default Define.Null;

    @Equals({ "true", "false"})
    String hardwareAccelerated() default Define.Null;

    String label() default Define.Null;

    @Equals({ "true", "false"})
    String exported() default Define.Null;

    String screenOrientation() default Define.Null;

    String windowSoftInputMode() default Define.Null;

    @Equals({ "true", "false"})
    String enable() default Define.Null;

    String permission() default Define.Null;

    @Ignore
    boolean ignore() default false;

    @interface Main {
    }
}
