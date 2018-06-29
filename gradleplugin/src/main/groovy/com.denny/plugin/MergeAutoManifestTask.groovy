package com.denny.plugin

import com.android.build.gradle.internal.LoggerWrapper
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.ManifestSystemProperty
import com.android.manifmerger.MergingReport
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MergeAutoManifestTask extends DefaultTask {

    File sourceManifest
    File autoManifest
    File outManifest
    File reportFile

    @TaskAction
    def mergeAutoManifest() {
        def result = ManifestMerger2.newMerger(sourceManifest, new LoggerWrapper(project.logger), ManifestMerger2.MergeType.APPLICATION)
                .addLibraryManifest(autoManifest)
                .setMergeReportFile(reportFile)
//                .setOverride(ManifestSystemProperty.TARGET_SDK_VERSION, merge.targetSdkVersion)
                .withFeatures(ManifestMerger2.Invoker.Feature.NO_IMPLICIT_PERMISSION_ADDITION)
                .merge()
        def str = result.getMergedDocument(MergingReport.MergedManifestKind.MERGED)
        outManifest.write(str)
    }

}