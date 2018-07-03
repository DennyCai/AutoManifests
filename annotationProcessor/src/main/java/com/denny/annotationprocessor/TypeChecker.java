package com.denny.annotationprocessor;

import com.denny.annotation.ExtendsFrom;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by caidong on 2018/6/28.
 */
public class TypeChecker {

    private Elements mUtils;

    public TypeChecker(Elements utils) {
        mUtils = utils;
    }

    public Deque<TypeElement> checkExtendsFrom(TypeElement type, Class<? extends Annotation> annClass) {
        TypeMirror superType = type.getSuperclass();
        Deque<TypeElement> extendLinked = new LinkedList<>();
        extendLinked.add(type);
        String[] supers = resolveSuperClasses(annClass);
        if (supers == null) {
            throw new UnsupportedOperationException("Annotation class " + annClass.toString());
        }
        while (superType != null) {
            String cls = superType.toString();
            if (ArrayUtils.contains(supers, cls)) {
                return extendLinked;
            }
            type = mUtils.getTypeElement(cls);
            extendLinked.add(type);
            if (type == null) {
                break;
            }
            superType = type.getSuperclass();
        }
        throwIllegal(type.toString(), supers);
        return new LinkedList<>(Collections.<TypeElement>emptyList());
    }

    private String[] resolveSuperClasses(Class<? extends Annotation> ann) {
        String[] classes = null;
        ExtendsFrom extendsFrom = ann.getAnnotation(ExtendsFrom.class);
        if (extendsFrom != null) {
            classes = extendsFrom.value();
        }
        return classes;
    }

    private void throwIllegal(String className, String[] supers) {
        throw new IllegalArgumentException(String.format("class %s should extends %s", className,
                StringUtils.join(supers)));
    }
}
