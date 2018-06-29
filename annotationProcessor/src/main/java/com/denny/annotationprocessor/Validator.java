package com.denny.annotationprocessor;

import com.denny.annotation.Equals;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.lang.model.element.Element;

/**
 * Created by caidong on 2018/6/28.
 */
public class Validator {

    public String validate(String val, Element comEle, Method method, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (ClassUtils.isAssignable(annotation.getClass(), Equals.class)) {
                Equals equals = method.getAnnotation(Equals.class);
                Class<?> declaringClz = method.getDeclaringClass();
                String annotatedClz = comEle.toString();
                if (!ArrayUtils.contains(equals.value(), val)) {
                    throw new IllegalArgumentException(
                            String.format("Class %s Annotation %s attribute %s value %s not equal [%s]",
                                    annotatedClz, declaringClz.getName(), method.getName(), val, StringUtils.join(equals.value(), ',')));
                }
            }
        }
        return val;
    }
}
