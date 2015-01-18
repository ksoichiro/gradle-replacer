package com.github.ksoichiro.replacer

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

class ReplacerPluginTest {
    private static final String PLUGIN_ID = 'com.github.ksoichiro.replacer'

    @Test
    public void apply() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: PLUGIN_ID

        // Check tasks
        assertTrue(project.tasks.replacerClean instanceof CleanTask)
        assertTrue(project.tasks.replacerGenerate instanceof GenerateTask)
        assertTrue(project.tasks.replacerArchive instanceof ArchiveTask)

        // Check extension
        assertTrue(project.extensions.replacer instanceof ReplacerPluginExtension)
        assertEquals("src", project.extensions.replacer.srcDir)
        assertEquals("main", project.extensions.replacer.srcMainDir)
        assertEquals("templates", project.extensions.replacer.templateDir)
        assertEquals("outputs", project.extensions.replacer.outputDir)
        assertEquals("archives", project.extensions.replacer.archiveDir)
        assertEquals("build.properties", project.extensions.replacer.properties)
        assertEquals("yyyyMMddHHmmss", project.extensions.replacer.archiveIdFormat)
        assertEquals("zip", project.extensions.replacer.archiveType)
    }

    @Test
    public void cleanNoDirs() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: PLUGIN_ID

        project.file("${project.buildDir}/outputs").delete()
        project.file("${project.buildDir}/archives").delete()

        project.tasks.replacerClean.execute()
    }

    @Test
    public void cleanNormal() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: PLUGIN_ID

        project.file("${project.buildDir}/outputs").mkdirs()
        project.file("${project.buildDir}/archives").mkdirs()

        project.tasks.replacerClean.execute()
    }

    @Test
    public void generate() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/normal/src"
        project.extensions.replacer.configurations {
            dev
            production
        }
        assertEquals("src/test/fixture/normal/src", project.extensions.replacer.srcDir)
        assertTrue(project.file(project.extensions.replacer.srcDir).exists())
        assertEquals(2, project.extensions.replacer.configurations.size())

        project.tasks.replacerGenerate.execute()
    }

    @Test
    public void generateWithNoProperties() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/no_props/src"
        project.extensions.replacer.configurations {
            dev
            production
        }

        project.tasks.replacerGenerate.execute()
    }

    @Test
    public void generateWithNoConfigDirectories() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/no_config_dirs/src"
        project.extensions.replacer.configurations {
            dev
            production
        }

        project.tasks.replacerGenerate.execute()
    }

    @Test
    public void archive() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/normal/src"
        project.extensions.replacer.archiveIdFormat = "_"
        project.extensions.replacer.configurations {
            dev
            production
        }

        assertEquals("zip", project.replacer.archiveType)
        project.tasks.replacerArchive.execute()
        assertTrue(project.file("${project.buildDir}/archives/_/dev-_.zip").exists())
    }

    @Test
    public void archiveGzip() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/normal/src"
        project.extensions.replacer.archiveType = "gzip"
        project.extensions.replacer.archiveIdFormat = "_"
        project.extensions.replacer.configurations {
            dev
            production
        }

        assertEquals("gzip", project.replacer.archiveType)
        project.tasks.replacerClean.execute()
        project.tasks.replacerGenerate.execute()
        project.tasks.replacerArchive.execute()
        assertTrue(project.file("${project.buildDir}/archives/_/dev-_.tar.gz").exists())
    }

    @Test
    public void archiveBzip2() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/normal/src"
        project.extensions.replacer.archiveType = "bzip2"
        project.extensions.replacer.archiveIdFormat = "_"
        project.extensions.replacer.configurations {
            dev
            production
        }

        assertEquals("bzip2", project.replacer.archiveType)
        project.tasks.replacerClean.execute()
        project.tasks.replacerGenerate.execute()
        project.tasks.replacerArchive.execute()
        assertTrue(project.file("${project.buildDir}/archives/_/dev-_.tar.bz2").exists())
    }

    @Test
    public void archiveUnsupported() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/normal/src"
        project.extensions.replacer.archiveType = "unknown"
        project.extensions.replacer.configurations {
            dev
            production
        }

        assertEquals("unknown", project.replacer.archiveType)
        try {
            project.tasks.replacerClean.execute()
            project.tasks.replacerGenerate.execute()
            project.tasks.replacerArchive.execute()
            fail()
        } catch (Exception) {
        }
    }

    @Test
    public void archiveNoOutputs() {
        Project project = ProjectBuilder.builder().withProjectDir(new File(".")).build()
        project.apply plugin: PLUGIN_ID
        project.extensions.replacer.srcDir = "src/test/fixture/normal/src"
        project.extensions.replacer.configurations {
            dev
            production
        }

        project.tasks.replacerClean.execute()
        project.tasks.replacerArchive.execute()
        assertFalse(project.file("${project.buildDir}/outputs").exists())
        assertFalse(project.file("${project.buildDir}/archives").exists())
    }
}
