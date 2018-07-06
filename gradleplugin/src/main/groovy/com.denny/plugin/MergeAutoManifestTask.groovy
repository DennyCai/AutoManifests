package com.denny.plugin

import com.android.build.gradle.internal.LoggerWrapper
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.MergingReport
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.util.StringUtil
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
            if (result!= null && result.result.error) {
                result.loggingRecords.each {
                    println it.message
                }
                result.intermediaryStages.each {
                    println it
                }
                throw new IllegalStateException()
            }
            def str = result.getMergedDocument(MergingReport.MergedManifestKind.MERGED)
            backupManifest(outManifest)
            if (str != null && !str.isEmpty()) {
                outManifest.write(str)
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw e
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