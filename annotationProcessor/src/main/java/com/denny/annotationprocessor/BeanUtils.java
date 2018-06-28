package com.denny.annotationprocessor;

import com.denny.annotation.BooleanType;
import com.denny.annotation.Define;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by caidong on 2018/6/27.
 */
public class BeanUtils {

    public static <T extends Annotation> List<AbstractMap.SimpleEntry<String, String>> class2Entry(T anno) {
        List<AbstractMap.SimpleEntry<String, String>> list = new LinkedList<>();
        for (Method method : anno.getClass().getDeclaredMethods()) {
            try {
                String key = method.getName();
                String val =  method.invoke(anno, null).toString();
                val = checkTypes(method.getAnnotations(), val);
                if (!StringUtils.isEmpty(val)) {
                    list.add(new AbstractMap.SimpleEntry<>(key, val));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static String checkTypes(Annotation[] anns, String val) {
        for (Annotation annotation : anns) {
            return typeCheck(annotation, val);
        }
        return Define.Null;
    }

    private static String typeCheck(Annotation anno, String val) {
        if (anno.getClass().equals(BooleanType.class)) {
            Boolean b = BooleanUtils.toBooleanObject(val);
            if (b == null) {
                return Define.Null;
            }
            return b.toString();
        }
        return val;
    }

}
