package com.denny.annotationprocessor.model;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by caidong on 2018/6/26.
 */
public abstract class ComponentElement extends BaseElement{

    private String mName;
    private String mProcess;
    private Boolean mExported;
    private Boolean mEnable;

    private Element mElement;

    public ComponentElement(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProcess() {
        return mProcess;
    }

    public void setProcess(String process) {
        mProcess = process;
    }

    public Boolean getExported() {
        return mExported;
    }

    public void setExported(Boolean exported) {
        mExported = exported;
    }

    public Boolean getEnable() {
        return mEnable;
    }

    public void setEnable(Boolean enable) {
        mEnable = enable;
    }

    public Element toElement() {
        Element element = super.toElement();
        if (StringUtils.isEmpty(mName)) {
            throw new IllegalArgumentException("activity name attribute should not be null");
        }
        createAttribute(element, "name", mName);
        if (mEnable != null) {
            createAttribute(element, "enable", mEnable.toString());
        }
        if (mExported != null) {
            createAttribute(element, "exported", mExported.toString());
        }
        if (!StringUtils.isEmpty(mProcess)) {
            createAttribute(element, "process", mProcess);
        }

        return element;
    }

    protected void createAttribute(Element element, String name, String value) {
        DocumentHelper.createAttribute(element, name, value);
    }
}
