package com.denny.annotationprocessor;


import com.denny.annotationprocessor.model.KeyValue;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        System.out.println(clzEle.toString() + " ignore:" + ignore(annotation, annClass));
        if (ignore(annotation, annClass)) {
            return null;
        }
        Deque<TypeElement> extendHierarchy = checkType(clzEle, annotation, annClass);
        System.out.println(extendHierarchy.toString());
        Deque<Map<String, String>> keyValues = parseHierarchyAnnotation(extendHierarchy, annClass, mValidator);
        System.out.println(keyValues);
        List<KeyValue> kvList = mergeHierarchyKeyValues(keyValues, mValidator);
        System.out.println("merge:" + kvList);
        return transformElement(annClass.getSimpleName(), kvList);
    }

    private boolean ignore(Annotation annotation, Class<? extends Annotation> annClass) {
        try {
            Method ignore = annClass.getDeclaredMethod("ignore", null);
            return (boolean) ignore.invoke(annotation, null);
        } catch (Exception e) {
            return false;
        }
    }

    private Deque<Map<String, String>> parseHierarchyAnnotation(Deque<TypeElement> extendHierachy, Class<? extends Annotation> annClass, Validator validator) {
        TypeElement type = null;
        Deque<Map<String, String>> deque = new LinkedList<>();
        while (!extendHierachy.isEmpty()) {
            type = extendHierachy.pop();
            Map<String, String> result = parseAnnotation(type, type.getAnnotation(annClass), annClass, mValidator);
            if (!result.isEmpty()) {
                deque.add(result);
            }
        }
        return deque;
    }

    private List<KeyValue> mergeHierarchyKeyValues(Deque<Map<String, String>> keyVals, Validator validator) {
        Map<String, String> base = keyVals.pollLast();
        Map<String, String> willMerge = null;
        while (!keyVals.isEmpty()) {
            willMerge = keyVals.pollLast();
            base.put("name", willMerge.remove("name"));//replace name attr

            for (Map.Entry<String, String> entry : willMerge.entrySet()) {
                String val;
                String key = entry.getKey();
                String baseVal = base.get(key);
                String newVal = entry.getValue();
                if (baseVal != null) {
                    val = baseVal;
                    String firstSym = newVal.substring(0, 1);
                    switch (firstSym) {
                        case "+":
                            val += newVal.substring(1);
                            break;
                        default:
                            val = newVal;
                    }
                } else {
                    val = newVal;
                }
                // TODO: 2018/7/2 need validate after value merged
                base.put(key, val);
            }
        }


        List<KeyValue> list = new LinkedList<>();
        for (Map.Entry<String, String> entry : base.entrySet()) {
            list.add(new KeyValue(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    private org.dom4j.Element transformElement(String elementName, List<KeyValue> kvList) {
        org.dom4j.Element element = DocumentHelper.createElement(elementName.toLowerCase());
        for (KeyValue keyValue : kvList) {
            element.add(new DefaultAttribute(QName.get(keyValue.getKey(), AndroidManifest.ANDROID), keyValue.getVal()));
        }
        return element;
    }

    private Map<String, String> parseAnnotation(Element clzEle, Annotation annotation, Class<? extends Annotation> annClass, Validator validator) {
        if (annotation == null) {
            return Collections.emptyMap();
        }
        Method[] methods = annClass.getDeclaredMethods();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", clzEle.toString());
        for (Method method : methods) {
            String key = method.getName();
            String val = null;
            try {
                String res = method.invoke(annotation, null).toString();
                val = validateValue(res, clzEle, method, method.getAnnotations(), validator);
                if (!StringUtils.isEmpty(val)) {
                    map.put(key, val);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private String validateValue(String val, Element comEle, Method method, Annotation[] annotations, Validator validator) {
        if (!ArrayUtils.isEmpty(annotations)) {
            return validator.validate(val, comEle, method, annotations);
        }
        return val;
    }


    protected Deque<TypeElement> checkType(Element clzEle, Annotation annotation, Class<? extends Annotation> annClass) {
        TypeElement type = mEleUtils.getTypeElement(clzEle.toString());
        return mChecker.checkExtendsFrom(type, annClass);
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
