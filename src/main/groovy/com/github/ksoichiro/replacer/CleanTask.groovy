package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanTask extends DefaultTask {
    @TaskAction
    def exec() {
        ReplacerPluginExtension replacer = project.replacer
        def outputDir = new File(project.buildDir, replacer.outputDir)
        if (outputDir.exists()) {
            project.delete(outputDir)
        }
        def archiveDir = new File(project.buildDir, replacer.archiveDir)
        if (archiveDir.exists()) {
            project.delete(archiveDir)
        }
    }
}
