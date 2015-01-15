package com.github.ksoichiro.replacer

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateTask extends DefaultTask {
    @TaskAction
    def exec() {
        project.replacer.configurations.each { Configuration config ->
            // Read properties to replace
            def props = new Properties()
            def mainPropFile = project.file("src/main/build.properties")
            if (mainPropFile.exists()) {
                mainPropFile.withInputStream { stream ->
                    props.load(stream)
                }
            }
            def configPropFile = project.file("src/${config.name}/build.properties")
            if (configPropFile.exists()) {
                configPropFile.withInputStream { stream ->
                    def configProps = new Properties()
                    configProps.load(stream)
                    props.putAll(configProps)
                }
            }

            // Copy and replace
            project.copy {
                from "src/main/templates"
                into "${project.buildDir}/outputs/${config.name}"
                filter(ReplaceTokens, tokens: props)
            }
        }
    }
}
