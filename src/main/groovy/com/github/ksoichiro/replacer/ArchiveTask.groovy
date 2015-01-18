package com.github.ksoichiro.replacer

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
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
            switch (replacer.archiveType) {
                case "zip":
                    project.ant.zip(
                            destfile: "${project.buildDir}/${replacer.archiveDir}/${id}/${config.name}-${id}.zip",
                            basedir: outputBaseDir,
                            includes: "${config.name}/**/*"
                    ).execute()
                    break
                case "gzip":
                    project.ant.tar(
                            destfile: "${project.buildDir}/${replacer.archiveDir}/${id}/${config.name}-${id}.tar.gz",
                            basedir: outputBaseDir,
                            includes: "${config.name}/**/*",
                            compression: "gzip"
                    ).execute()
                    break
                case "bzip2":
                    project.ant.tar(
                            destfile: "${project.buildDir}/${replacer.archiveDir}/${id}/${config.name}-${id}.tar.bz2",
                            basedir: outputBaseDir,
                            includes: "${config.name}/**/*",
                            compression: "bzip2"
                    ).execute()
                    break
                default:
                    throw new GradleException("Unsupported archive type: ${replacer.archiveType}")
                    break
            }
        }
    }
}
