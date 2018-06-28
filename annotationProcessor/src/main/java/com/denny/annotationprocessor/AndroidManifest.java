package com.denny.annotationprocessor;

import com.denny.annotationprocessor.model.ApplicationElement;
import com.denny.annotationprocessor.model.ComponentElement;
import com.denny.annotationprocessor.model.ManifestElement;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.tools.FileObject;

/**
 * Created by caidong on 2018/6/26.
 */
class AndroidManifest {

    private final static Namespace ANDROID = new Namespace("android", "http://schemas.android.com/apk/res/android");

    private final Document mManifest;
    private final Element mRoot;
    private final Element mApplication;

    public AndroidManifest() {
        mManifest = DocumentHelper.createDocument();
        mRoot = new ManifestElement().toElement();
        mApplication = DocumentHelper.createElement("application");
        mRoot.add(ANDROID);
    }

    public void setApplication(ApplicationElement element) {
        Element app = element.toElement();
        Iterator<Attribute> attrs = app.attributeIterator();
        while (attrs.hasNext()) {
            Attribute attr = attrs.next();
            attr.setParent(null);
            mApplication.add(attr);
        }
    }

    public void addComponent(ComponentElement element) {
        mApplication.add(element.toElement());
    }

    public void writeTo(FileObject out) throws IOException {
        mRoot.add(mApplication);
        mManifest.add(mRoot);
        Writer writer = out.openWriter();
        mManifest.write(writer);
        writer.close();
    }
}
