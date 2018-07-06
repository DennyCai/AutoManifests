package com.denny.plugin

import com.android.build.gradle.internal.LoggerWrapper
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.MergingReport
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MergeAutoManifestTask extends DefaultTask {

    File sourceManifest
    File autoManifest
    File outManifest
    File reportFile

    @TaskAction
    def mergeAutoManifest() {
        def result = null
        try {
            result = ManifestMerger2.newMerger(sourceManifest, new LoggerWrapper(project.logger), ManifestMerger2.MergeType.APPLICATION)
                    .addLibraryManifest(autoManifest)
                    .setMergeReportFile(reportFile)
                    .withFeatures(ManifestMerger2.Invoker.Feature.NO_IMPLICIT_PERMISSION_ADDITION)
                    .merge()
            def str = result.getMergedDocument(MergingReport.MergedManifestKind.MERGED)
            backupManifest(outManifest)
            outManifest.write(str)
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (result!= null && result.result.error) {
            result.loggingRecords.each {
                it.message.println(System.err)
            }
            result.intermediaryStages.each {
                it.println(System.err)
            }
            throw new IllegalStateException()
        }

    }

    def backupManifest(File src) {
        if (src!= null && src.exists()) {
            File bk = new File(src.parentFile, "${src.name}.bk")
            if (!bk.exists()) {
                FileUtils.copyFile(src, bk)
            }
        }
    }

}