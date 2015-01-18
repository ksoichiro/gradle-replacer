package com.github.ksoichiro.replacer

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateTask extends DefaultTask {
    private static final String DEFAULT_PROPFILENAME = "build.properties"

    @TaskAction
    def exec() {
        project.replacer.configurations.each { Configuration config ->
            ReplacerPluginExtension replacer = project.replacer

            // Read properties to replace
            def props = new Properties()
            def mainPropFile = project.file("${replacer.srcDir}/${replacer.srcMainDir}/${DEFAULT_PROPFILENAME}")
            if (mainPropFile.exists()) {
                mainPropFile.withInputStream { stream ->
                    props.load(stream)
                }
            }
            def configPropFile = project.file("${replacer.srcDir}/${config.name}/${DEFAULT_PROPFILENAME}")
            if (configPropFile.exists()) {
                configPropFile.withInputStream { stream ->
                    def configProps = new Properties()
                    configProps.load(stream)
                    props.putAll(configProps)
                }
            }

            // Copy and replace
            [
                    "${replacer.srcDir}/${replacer.srcMainDir}/${replacer.templateDir}",
                    "${replacer.srcDir}/${config.name}/${replacer.templateDir}",
            ].each { fromFiles ->
                if (project.file(fromFiles).exists()) {
                    project.copy {
                        from fromFiles
                        if (replacer.excludes && 0 < replacer.excludes.size()) {
                            exclude replacer.excludes
                        }
                        into "${project.buildDir}/${replacer.outputDir}/${config.name}"
                        filter(ReplaceTokens, tokens: props)
                    }
                }
            }
        }
    }
}
