package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanTask extends DefaultTask {
    @TaskAction
    def exec() {
        project.replacer.configurations.each { Configuration config ->
            def outputDir = new File(project.buildDir, "outputs/${config.name}")
            if (outputDir.exists()) {
                project.delete(outputDir)
            }
        }
    }
}
