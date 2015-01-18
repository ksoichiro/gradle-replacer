package com.github.ksoichiro.replacer

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateTask extends DefaultTask {
    private static final String DEFAULT_PROPFILENAME = "build.properties"

    @TaskAction
    def exec() {
        project.replacer.configurations.each { Configuration config ->
            // Read properties to replace
            def props = new Properties()
            def mainPropFile = project.file("src/main/${DEFAULT_PROPFILENAME}")
            if (mainPropFile.exists()) {
                mainPropFile.withInputStream { stream ->
                    props.load(stream)
                }
            }
            def configPropFile = project.file("src/${config.name}/${DEFAULT_PROPFILENAME}")
            if (configPropFile.exists()) {
                configPropFile.withInputStream { stream ->
                    def configProps = new Properties()
                    configProps.load(stream)
                    props.putAll(configProps)
                }
            }

            // Copy and replace
            [
                    "src/main/templates",
                    "src/${config.name}/templates",
            ].each { fromFiles ->
                if (project.file(fromFiles).exists()) {
                    project.copy {
                        from fromFiles
                        ReplacerPluginExtension replacer = project.replacer
                        if (replacer.excludes && 0 < replacer.excludes.size()) {
                            exclude replacer.excludes
                        }
                        into "${project.buildDir}/outputs/${config.name}"
                        filter(ReplaceTokens, tokens: props)
                    }
                }
            }
        }
    }
}
