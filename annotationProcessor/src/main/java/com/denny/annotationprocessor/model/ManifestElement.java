package com.denny.annotationprocessor.model;

import org.dom4j.Element;

/**
 * Created by caidong on 2018/6/27.
 */
public class ManifestElement extends BaseElement {

    @Override
    protected String getElementName() {
        return "manifest";
    }

    @Override
    public Element toElement() {
        Element root = super.toElement();
        root.add(ANDROID);
        root.addAttribute("package", "com.denny.automanifest");
        return root;
    }
}
