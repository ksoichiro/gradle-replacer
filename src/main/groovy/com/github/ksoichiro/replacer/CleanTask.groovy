package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanTask extends DefaultTask {
    @TaskAction
    def exec() {
        def outputDir = new File(project.buildDir, "outputs")
        if (outputDir.exists()) {
            project.delete(outputDir)
        }
        def archiveDir = new File(project.buildDir, "archives")
        if (archiveDir.exists()) {
            project.delete(archiveDir)
        }
    }
}
