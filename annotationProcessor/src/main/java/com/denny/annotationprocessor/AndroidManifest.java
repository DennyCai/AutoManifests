package com.denny.annotationprocessor;

import com.denny.annotationprocessor.model.ManifestElement;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.util.Iterator;

import javax.tools.FileObject;

/**
 * Created by caidong on 2018/6/26.
 */
class AndroidManifest {

    public final static Namespace ANDROID = new Namespace("android", "http://schemas.android.com/apk/res/android");

    private final Document mManifest;
    private final Element mRoot;
    private final Element mApplication;

    public AndroidManifest() {
        mManifest = DocumentHelper.createDocument();
        mManifest.addComment(writeComment());
        mRoot = new ManifestElement().toElement();
        mApplication = DocumentHelper.createElement("application");
        mRoot.add(ANDROID);
    }

    private String writeComment() {
        return "Auto Manifest XML";
    }

    public void setApplication(Element element) {
//        Element app = element;
//        Iterator<Attribute> attrs = app.attributeIterator();
//        while (attrs.hasNext()) {
//            Attribute attr = attrs.next();
//            attr.setParent(null);
//            mApplication.add(attr);
//        }
    }

    public void addToApplication(Element element) {
        mApplication.add(element);
    }

    public void writeTo(FileObject out) throws IOException {
        OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
        prettyFormat.setEncoding("UTF-8");
        prettyFormat.setSuppressDeclaration(true);
        XMLWriter xmlWriter = new XMLWriter(out.openWriter(), prettyFormat);

        mRoot.add(mApplication);
        mManifest.add(mRoot);
        xmlWriter.write(mManifest);
        xmlWriter.close();
    }
}
