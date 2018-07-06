package com.denny.plugin

import com.android.SdkConstants
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
                def outManifest = variant.sourceSets.find { it.name.equals(SdkConstants.FD_MAIN)}.manifestFile

                MergeAutoManifestTask task = project.tasks.create("process${variantName}MergeAutoManifest", MergeAutoManifestTask)
                task.group = GROUP_NAME
//                task.processManifest = processManifest
                task.autoManifest = FileUtils.getFile(compile.annotationProcessorOutputFolder, "AndroidManifest.xml")
                task.sourceManifest = outManifest//FileUtils.getFile(variantOutput.processManifest.manifestOutputDirectory, "AndroidManifest.xml")
                task.reportFile = FileUtils.getFile(variantOutput.processManifest.reportFile.parentFile, "auto-manifest-report.txt")
                task.outManifest = outManifest

//                variantOutput.processManifest.finalizedBy task

                def generate = project.tasks.create("generate${variantName}Manifest", GenerateManifestTask)
                generate.group = GROUP_NAME
                generate.dependsOn variant.javaCompiler

//                variant.packageApplication.doFirst {
//                    println FileUtils.getFile(variant.packageApplication.manifests.files[0], "AndroidManifest.xml").text
//                }
//                variantOutput.processManifest.finalizedBy(task)
//                variantOutput.processManifest.doFirst {
//                    variant.sourceSets.each {
//                        it.javaDirectories.each {
//                            println it.listFiles().inspect()
//                        }
//                    }
//                }


            }
        }
    }
}