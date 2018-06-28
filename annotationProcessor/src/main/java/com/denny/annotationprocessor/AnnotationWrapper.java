package com.denny.annotationprocessor;

/**
 * Created by caidong on 2018/6/26.
 */
public class AnnotationWrapper<T> {

    private Class<T> mAnnotation;

    public AnnotationWrapper(Class<T> ann) {
        mAnnotation = ann;
    }


}
