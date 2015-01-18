package com.github.ksoichiro.replacer

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class ReplacerPluginExtension {
    NamedDomainObjectContainer<Configuration> configurations
    String srcDir = "src"
    String srcMainDir = "main"
    String templateDir = "templates"
    String outputDir = "outputs"
    String archiveDir = "archives"
    String properties = "build.properties"
    String[] excludes
    Project project

    ReplacerPluginExtension(Project project,
                            NamedDomainObjectContainer<Configuration> configurations) {
        this.project = project
        this.configurations = configurations
    }

    def configurations(Closure closure) {
        configurations.configure(closure)
    }
}
