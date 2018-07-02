package com.denny.annotationprocessor;


import com.denny.annotationprocessor.model.KeyValue;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by caidong on 2018/6/28.
 */
public class AnnotationParser {

    private javax.lang.model.util.Elements mEleUtils;
    private TypeChecker mChecker;
    private Validator mValidator;

    public AnnotationParser(Elements eleUtils, TypeChecker checker, Validator validator) {
        mEleUtils = eleUtils;
        mChecker = checker;
        mValidator = validator;
    }

    public org.dom4j.Element parse(javax.lang.model.element.Element clzEle, Class<? extends Annotation> annClass) {
        Annotation annotation = clzEle.getAnnotation(annClass);
        checkType(clzEle, annotation, annClass);
        List<KeyValue> kvList = parseAnnotation(clzEle, annotation, annClass, mValidator);
        return transformElement(annClass.getSimpleName(), kvList);
    }

    private org.dom4j.Element transformElement(String elementName, List<KeyValue> kvList) {
        org.dom4j.Element element = DocumentHelper.createElement(elementName.toLowerCase());
        for (KeyValue keyValue : kvList) {
            element.add(new DefaultAttribute(QName.get(keyValue.getKey(), AndroidManifest.ANDROID), keyValue.getVal()));
        }
        return element;
    }

    private List<KeyValue> parseAnnotation(Element clzEle, Annotation annotation, Class<? extends Annotation> annClass, Validator validator) {
        Method[] methods = annClass.getDeclaredMethods();
        List<KeyValue> keyValues = new LinkedList<>();
        keyValues.add(new KeyValue("name", clzEle.toString()));
        for (Method method : methods) {
            String key = method.getName();
            String val = null;
            try {
                String res = method.invoke(annotation, null).toString();
                val = validateValue(res, clzEle, method, method.getAnnotations(), validator);
                if (!StringUtils.isEmpty(val)) {
                    keyValues.add(new KeyValue(key, val));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return keyValues;
    }

    private String validateValue(String val, Element comEle, Method method, Annotation[] annotations, Validator validator) {
        if (!ArrayUtils.isEmpty(annotations)) {
            return validator.validate(val, comEle, method, annotations);
        }
        return val;
    }


    protected void checkType(Element clzEle, Annotation annotation, Class<? extends Annotation> annClass) {
        TypeElement type = mEleUtils.getTypeElement(clzEle.toString());
        mChecker.checkExtendsFrom(type, annClass);
    }

    public org.dom4j.Element addMainInterFilter() {
        org.dom4j.Element intentFilter = DocumentHelper.createElement("intent-filter");
        org.dom4j.Element action = DocumentHelper.createElement("action")
                .addAttribute(QName.get("name", AndroidManifest.ANDROID), "android.intent.action.MAIN");
        org.dom4j.Element category = DocumentHelper.createElement("category")
                .addAttribute(QName.get("name", AndroidManifest.ANDROID), "android.intent.category.LAUNCHER");
        intentFilter.add(action);
        intentFilter.add(category);
        return intentFilter;
    }
}
