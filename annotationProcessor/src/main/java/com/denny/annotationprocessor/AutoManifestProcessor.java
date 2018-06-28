package com.denny.annotationprocessor;

import com.denny.annotation.Activity;
import com.denny.annotation.Application;
import com.denny.annotationprocessor.model.ActivityElement;
import com.denny.annotation.Android;
import com.denny.annotationprocessor.model.ApplicationElement;

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
import javax.tools.DocumentationTool;
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
        Utils.init(mElemUtils);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        AndroidManifest manifest = prepareManifest();

        Set<? extends Element> appSet = roundEnvironment.getElementsAnnotatedWith(Application.class);
        if (appSet.size() > 1) {
            throw new IllegalArgumentException("application annotation more than 1");
        } else if(!appSet.isEmpty()) {
            Element app = appSet.iterator().next();
            Utils.checkApplication(app);
            manifest.setApplication(new ApplicationElement(app.toString(), app.getAnnotation(Application.class)));
        }

        for (Element ele : roundEnvironment.getElementsAnnotatedWith(Activity.class)) {
            Utils.checkActivity(ele);
            manifest.addComponent(new ActivityElement(ele.toString(), ele.getAnnotation(Activity.class)));
        }

        parseActivity(manifest);
        message(mOut.toUri().toString());
        try {
            manifest.writeTo(mOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void parseActivity(AndroidManifest manifest) {

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
        set.add(Android.class.getName());
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
