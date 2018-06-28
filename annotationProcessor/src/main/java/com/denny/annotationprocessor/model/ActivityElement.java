package com.denny.annotationprocessor.model;

import com.denny.annotation.Activity;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * Created by caidong on 2018/6/26.
 */
public class ActivityElement extends ComponentElement{

    private Activity mActivity;

    public ActivityElement(String pkgCls, Activity annotation) {
        super(pkgCls);
        mActivity = annotation;
        setEnable(BooleanUtils.toBooleanObject(annotation.enable()));
        setExported(BooleanUtils.toBooleanObject(annotation.exported()));
        setProcess(annotation.process());
    }

    @Override
    protected String getElementName() {
        return "activity";
    }

    @Override
    public Element toElement() {
        Element activity = super.toElement();

        if (!StringUtils.isEmpty(mActivity.theme())) {
            createAttribute(activity, "theme", mActivity.theme());
        }
        if (!StringUtils.isEmpty(mActivity.configChanges())) {
            createAttribute(activity, "configChanges", mActivity.configChanges());
        }
        return activity;
    }
}
