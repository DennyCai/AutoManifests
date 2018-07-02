package com.denny.annotationprocessor;

import com.denny.annotation.ExtendsFrom;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by caidong on 2018/6/28.
 */
public class TypeChecker {

    private static final Map<Class<?>, String[]> sClassSupers = new HashMap<>();
    private Elements mUtils;

    static {
//        put(Application.class, Define.APPLICATION);
//        put(Activity.class, Define.Activity.APP, Define.Activity.APPCOMPAT);
//        put(Service.class, Define.SERVICE);
//        put(Receiver.class, Define.RECEIVER);
//        put(Provider.class, Define.PROVIDER);
    }

    public TypeChecker(Elements utils) {
        mUtils = utils;
    }

    private static void put(Class<?> clz, String... clasNames) {
        sClassSupers.put(clz, clasNames);
    }


    public void checkExtendsFrom(TypeElement type, Class<? extends Annotation> annClass) {
        TypeMirror superType = type.getSuperclass();
        String[] supers = resolveSuperClasses(annClass);
        if (supers == null) {
            throw new UnsupportedOperationException("Annotation class " + annClass.toString());
        }
        while (superType != null) {
            String cls = superType.toString();
            if (ArrayUtils.contains(supers, cls)) {
                return;
            }
            type = mUtils.getTypeElement(cls);
            if (type == null) {
                break;
            }
            superType = type.getSuperclass();
        }
        throwIllegal(type.toString(), supers);
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
