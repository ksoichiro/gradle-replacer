package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ArchiveTask extends DefaultTask {
    @TaskAction
    def exec() {
        ReplacerPluginExtension replacer = project.replacer
        def id = new Date().format(replacer.archiveIdFormat)
        replacer.configurations.each { Configuration config ->
            def outputBaseDir = new File(project.buildDir, replacer.outputDir)
            def outputDir = new File(outputBaseDir, "${config.name}")
            if (!outputDir.exists()) {
                return
            }
            project.ant.zip(
                    destfile: "${project.buildDir}/${replacer.archiveDir}/${id}/${config.name}-${id}.zip",
                    basedir: outputBaseDir,
                    includes: "${config.name}/**/*"
            ).execute()
        }
    }
}
