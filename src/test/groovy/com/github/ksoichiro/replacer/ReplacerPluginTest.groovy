package com.github.ksoichiro.replacer

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class ReplacerPluginTest {
    @Test
    public void apply() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.ksoichiro.replacer'

        // Check tasks
        assertTrue(project.tasks.replacerClean instanceof CleanTask)
        assertTrue(project.tasks.replacerGenerate instanceof GenerateTask)
        assertTrue(project.tasks.replacerArchive instanceof ArchiveTask)
    }
}
