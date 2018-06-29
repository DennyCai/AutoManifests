package com.denny.annotationprocessor;

import com.denny.annotation.Activity;
import com.denny.annotation.Application;
import com.denny.annotation.ExtendsFrom;
import com.denny.annotation.Provider;
import com.denny.annotation.Receiver;
import com.denny.annotation.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public class AutoManifestProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;
    private FileObject mOut;
    private Elements mElemUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        mElemUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        AndroidManifest manifest = prepareManifest();
        AnnotationParser parser = new AnnotationParser(mElemUtils, new TypeChecker(mElemUtils), new Validator());

        Set<? extends Element> appSet = roundEnvironment.getElementsAnnotatedWith(Application.class);
        if (appSet.size() > 1) {
            throw new IllegalArgumentException("application annotation more than 1");
        } else if(!appSet.isEmpty()) {
            Element app = appSet.iterator().next();
            manifest.setApplication(parser.parse(app, Application.class));
        }

        for (Element ele : roundEnvironment.getElementsAnnotatedWith(Activity.class)) {
            org.dom4j.Element activity = parser.parse(ele, Activity.class);
            Activity.Main isMain = ele.getAnnotation(Activity.Main.class);
            if (isMain != null) {
                activity.add(parser.addMainInterFilter());
            }
            manifest.addToApplication(activity);
        }

        message(mOut.toUri().toString());
        try {
            manifest.writeTo(mOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private AndroidManifest prepareManifest() {
        try {
            mOut = mFiler.createResource(StandardLocation.SOURCE_OUTPUT,
                    "", "AndroidManifest.xml", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AndroidManifest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>(super.getSupportedAnnotationTypes());
        set.add(Application.class.getName());
        set.add(Activity.class.getName());
        set.add(Service.class.getName());
        set.add(Receiver.class.getName());
        set.add(Provider.class.getName());
        set.add(Activity.Main.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected void message(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
