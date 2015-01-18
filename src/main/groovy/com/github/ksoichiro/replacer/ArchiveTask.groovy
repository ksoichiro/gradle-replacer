package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Zip

class ArchiveTask extends DefaultTask {
    @TaskAction
    def exec() {
        def id = new Date().format('yyyyMMddHHmmss')
        project.replacer.configurations.each { Configuration config ->
            def outputBaseDir = new File(project.buildDir, "outputs")
            def outputDir = new File(outputBaseDir, "${config.name}")
            if (!outputDir.exists()) {
                return
            }
            project.ant.zip(
                    destfile: "${project.buildDir}/archives/${id}/${config.name}-${id}.zip",
                    basedir: outputBaseDir,
                    includes: "${config.name}/**/*"
            ).execute()
        }
    }
}
