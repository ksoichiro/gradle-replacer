package com.github.ksoichiro.replacer

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReplacerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def configurations = project.container(Configuration)
        project.extensions.create('replacer', ReplacerPluginExtension, project, configurations)
        project.task('generate', type: GenerateTask)
    }
}
