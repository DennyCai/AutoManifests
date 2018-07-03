package com.denny.plugin

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.tasks.ProcessManifest
import com.android.build.gradle.tasks.factory.AndroidJavaCompile
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoManifestPlugin implements Plugin<Project> {

    static final String GROUP_NAME = 'AutoManifest'

    @Override
    void apply(Project project) {

        project.extensions.create("AutoManifest", AutoManifestExtension)

        project.afterEvaluate {
            project.android.applicationVariants.all { ApplicationVariant variant ->
                String variantName = variant.name.capitalize()
                def variantOutput = variant.outputs.first()

                def hasDisabledAutoManifest = {
                    it.ext.properties.containsKey("enable") && !it.ext.enable
                }

                // 检查是否开启
                if ((variant.productFlavors + variant.buildType).any(hasDisabledAutoManifest)) {
                    return
                }

                AndroidJavaCompile compile = variant.javaCompiler.asType(AndroidJavaCompile)

                variantOutput.processManifest.doFirst {
                    println "aapt:" + variantOutput.processManifest.aaptFriendlyManifestOutputFile
                }
                def processManifest = variantOutput.processManifest

                MergeAutoManifestTask task = project.tasks.create("process${variantName}MergeAutoManifest", MergeAutoManifestTask)
                task.group = GROUP_NAME
//                task.processManifest = processManifest
                task.autoManifest = FileUtils.getFile(compile.annotationProcessorOutputFolder, "AndroidManifest.xml")
                task.sourceManifest = FileUtils.getFile(variantOutput.processManifest.manifestOutputDirectory, "AndroidManifest.xml")
                task.reportFile = FileUtils.getFile(variantOutput.processManifest.reportFile.parentFile, "auto-manifest-report.txt")
                task.outManifest = task.sourceManifest

//                variant.packageApplication.doFirst {
//                    println FileUtils.getFile(variant.packageApplication.manifests.files[0], "AndroidManifest.xml").text
//                }
                variant.packageApplication.dependsOn task

            }
        }
    }
}