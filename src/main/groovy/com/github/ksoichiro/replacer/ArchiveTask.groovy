package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ArchiveTask extends DefaultTask {
    @TaskAction
    def exec() {
        def id = new Date().format('yyyyMMddHHmmss')
        ReplacerPluginExtension replacer = project.replacer
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
