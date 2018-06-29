package com.denny.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.tasks.MergeManifests
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.ManifestSystemProperty
import com.android.manifmerger.MergingReport
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.taskfactory.AnnotationProcessingTaskFactory

class AutoManifestPlugin implements Plugin<Project> {

    static final String GROUP_NAME = 'AutoManifest'

    @Override
    void apply(Project project) {

        project.extensions.create("autoManifest", AutoManifestExtension)

        project.afterEvaluate {
            project.android.applicationVariants.all { ApplicationVariant variant ->
                String variantName = variant.name.capitalize()
                def variantOutput = variant.outputs.first()

                def hasDisabledAutoManifest = {
                    it.ext.properties.containsKey("enableAutoManifest") && !it.ext.enableAutoManifest
                }

                // 检查是否开启
                if ((variant.productFlavors + variant.buildType).any(hasDisabledAutoManifest)) {
                    return
                }

                MergeAutoManifestTask task = project.tasks.create("process${variantName}mergeAutoManifest", MergeAutoManifestTask)
                task.group = GROUP_NAME
                task.autoManifest = FileUtils.getFile(project.buildDir, "generated", "source", "apt", variantName, "AndroidManifest.xml")
                task.mustRunAfter variantOutput.processManifest
//                def task = project.tasks.findByName("process${variantName}Manifest")
                MergeManifests merge = task.asType(MergeManifests)
//                variantOutput.processManifest.doFirst {
//                    println "processManifest.doFirst---------------------"
//                    println variantOutput.processManifest.manifestOutputDirectory
//                }
//                merge.doFirst {
//                    println "DoFirst!!!!!!!!!!"
//                    File dir = project.buildDir
//                    merge.manifestOverlays.add(new File(dir, "generated/source/apt/debug/AndroidManifest.xml"))
//                    if (merge.manifestOverlays.isEmpty()) {
//                        println "manifestOverlays.isEmpty()"
//                    }
//                    println merge.packageManifest.files.toList()
//                    merge.manifestOverlays.each {
//                        println it.name
//                    }
//
//                }
//                variantOutput.processManifest.doLast {
//                    File manifestFile = FileUtils.getFile(variantOutput.processManifest.manifestOutputDirectory, "AndroidManifest.xml")
//                    File outFile = FileUtils.getFile(variantOutput.processManifest.manifestOutputDirectory, "AndroidManifest2.xml")
//                    File autoManifest = new File(project.buildDir, "generated/source/apt/debug/AndroidManifest.xml")
//                    File reportFile = FileUtils.getFile(project.buildDir, "outputs", "logs", "auto-manifest-report.txt")
//                    println manifestFile.text
//                    println merge.targetSdkVersion
//
//                    def mergeDocument = ManifestMerger2.newMerger(manifestFile, new LoggerWrapper(project.logger), ManifestMerger2.MergeType.APPLICATION)
//                        .addLibraryManifest(autoManifest)
//                        .setMergeReportFile(reportFile)
//                        .setOverride(ManifestSystemProperty.TARGET_SDK_VERSION, merge.targetSdkVersion)
//                        .withFeatures(ManifestMerger2.Invoker.Feature.NO_IMPLICIT_PERMISSION_ADDITION)
//                        .merge()
//                        .getMergedDocument(MergingReport.MergedManifestKind.MERGED)
//                    outFile.write(mergeDocument)
//                }
            }
        }
    }
}