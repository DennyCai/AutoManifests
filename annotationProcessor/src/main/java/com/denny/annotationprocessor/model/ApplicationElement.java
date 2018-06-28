package com.denny.annotationprocessor.model;

import com.denny.annotation.Application;
import com.denny.annotationprocessor.BeanUtils;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Map;

/**
 * Created by caidong on 2018/6/27.
 */
public class ApplicationElement extends BaseElement{

    private Application mApp;

    public ApplicationElement(String name, Application application) {
        this(name);
        mApp = application;
    }

    @Override
    protected String getElementName() {
        return "application";
    }

    public Element toElement() {
        Element app = super.toElement();
        String name = null;
        for (Map.Entry<String, String> entry : BeanUtils.class2Entry(mApp)) {
            createAttribute(app, entry.getKey(), entry.getValue());
            if (entry.getKey().equals("name")) {
                name = entry.getValue();
            }
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("application name attribute should not be empty");
        }
        System.out.println(app.attributeCount());
        return app;
    }
}
