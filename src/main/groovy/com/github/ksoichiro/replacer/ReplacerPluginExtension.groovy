package com.github.ksoichiro.replacer

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class ReplacerPluginExtension {
    NamedDomainObjectContainer<Configuration> configurations
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
