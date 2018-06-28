package com.denny.annotationprocessor.model;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;

/**
 * Created by caidong on 2018/6/27.
 */
public abstract class BaseElement {

    public final static Namespace ANDROID = new Namespace("android", "http://schemas.android.com/apk/res/android");

    protected abstract String getElementName();

    public Element toElement() {
        return DocumentHelper.createElement(getElementName());
    }

    protected void createAttribute(Element element, String name, String value) {
        element.add(new DefaultAttribute(QName.get(name, ANDROID), value));
    }

}
