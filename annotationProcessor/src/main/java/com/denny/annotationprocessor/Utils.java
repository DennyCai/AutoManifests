package com.denny.annotationprocessor;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by caidong on 2018/6/27.
 */
public class Utils {

    private static final String APPLICATION_CLASS = "android.app.application";
    private static final String ACTIVITY_CLASS = "android.app.activity";
    private static final String APPCOMPAT_ACTIVITY_CLASS = "android.support.v7.app.AppCompatActivity";

    private static final Set<String> sActivityClassCache = new HashSet<>();

    private static Elements sUtils;

    public static void checkApplication(Element app) {
        TypeElement typeElement = getTypeElement(app);
        if (!isExtends(typeElement, APPLICATION_CLASS)) {
            throw new IllegalArgumentException("application class should extends android.app.application");
        }
    }

    public static void checkActivity(Element act) {
        TypeElement typeElement = getTypeElement(act);
        if (!isExtends(typeElement, sActivityClassCache, ACTIVITY_CLASS, APPCOMPAT_ACTIVITY_CLASS)) {
            throw new IllegalArgumentException(String.format("activity class should extends %s or %s",
                    ACTIVITY_CLASS, APPCOMPAT_ACTIVITY_CLASS));
        }
    }

    private static boolean isExtends(TypeElement element, Set<String> cache, String... classArray) {
        if (element == null) {
            return false;
        }
        boolean hasCache = (cache != null);
        String supClaName = element.getSuperclass().toString();
        if (hasCache && cache.contains(supClaName)) {
            return true;
        }
        if (equalsArray(supClaName, classArray) == null) {
            if (hasCache) {
                cache.add(supClaName);
            }
            return true;
        } else {
            return isExtends(getTypeElement(element.getSuperclass()), cache, classArray);
        }
    }

    private static TypeElement getTypeElement(Element ele) {
        return sUtils.getTypeElement(ele.toString());
    }

    private static TypeElement getTypeElement(TypeMirror app) {
        return sUtils.getTypeElement(app.toString());
    }

    private static String equalsArray(String str1, String... strs) {
        for (String str : strs) {
            if (StringUtils.equals(str1, str)) {
                return str;
            }
        }
        return null;
    }

    public static boolean isExtends(TypeElement element, String className) {
        return isExtends(element, null, className);
    }

    public static void init(Elements utils) {
        sUtils = utils;
    }
}
