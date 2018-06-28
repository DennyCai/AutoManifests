package com.denny.annotation;

/**
 * Created by caidong on 2018/6/26.
 */
public enum LaunchMode {
    None(Define.Null),
    Standard("standard"),
    SingleTop("singleTop"),
    SingleTask("singleTask"),
    SingelInstance("singleInstance");

    private String mStr;

    LaunchMode(String text) {
        mStr = text;
    }

    @Override
    public String toString() {
        return mStr;
    }
}
