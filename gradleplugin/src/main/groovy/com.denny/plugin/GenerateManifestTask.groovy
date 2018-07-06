package com.denny.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateManifestTask extends DefaultTask {

    @TaskAction
    def sub() {
        println "GenerateManifestTask-----------------"
    }

}