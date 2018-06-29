package com.denny.annotationprocessor.model;

/**
 * Created by caidong on 2018/6/29.
 */
public class KeyValue {

    private String mKey;
    private String mVal;

    public KeyValue(String key, String val) {
        mKey = key;
        mVal = val;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getVal() {
        return mVal;
    }

    public void setVal(String val) {
        mVal = val;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "mKey='" + mKey + '\'' +
                ", mVal='" + mVal + '\'' +
                '}';
    }
}
