gradle-replacer
===

[![Build Status](http://img.shields.io/travis/ksoichiro/gradle-replacer.svg?style=flat)](https://travis-ci.org/ksoichiro/gradle-replacer)
[![Maven Central](http://img.shields.io/maven-central/v/com.github.ksoichiro/gradle-replacer.svg?style=flat)](https://github.com/ksoichiro/gradle-replacer/releases/latest)
[![Coverage Status](https://coveralls.io/repos/ksoichiro/gradle-replacer/badge.svg?branch=master)](https://coveralls.io/r/ksoichiro/gradle-replacer?branch=master)
[![License](http://img.shields.io/:license-MIT-blue.svg?style=flat)](https://github.com/ksoichiro/gradle-replacer/blob/master/LICENSE)

Gradle plugin that provides a minimalistic template engine feature.

## TL;DR

You can maintain any configuration files with "properties" format.  
The plugin copies files from templates and
replaces tags with properties for each configurations.

    src/main/templates/config.xml
    src/dev/build.properties
    src/production/build.properties
    ↓
    build/outputs/dev/config.xml
    build/outputs/production/config.xml

## Example

### Build script

build.gradle

```groovy
buildscript {
    repositories {
        maven {
            url uri('https://oss.sonatype.org/content/repositories/snapshots/')
        }
    }
    dependencies {
        classpath 'com.github.ksoichiro:gradle-replacer:0.1.0-SNAPSHOT'
    }
}

apply plugin: 'com.github.ksoichiro.replacer'

replacer {
    configurations {
        dev
        production
    }
}
```

### Inputs

src/main/templates/config.xml

```xml
<server>
    <url>@SERVER_URL@</url>
    <port>@SERVER_PORT@</port>
</server>
```

src/main/build.properties

```
SERVER_URL=ssl://192.168.100.56
SERVER_PORT=9900
```

src/production/build.properties

```
SERVER_URL=ssl://example.com
SERVER_PORT=9901
```

### Execution

```sh
$ ./gradlew replacerGenerate
```

### Outputs

build/outputs/dev/config.xml

```xml
<server>
    <url>ssl://192.168.100.56</url>
    <port>9900</port>
</server>
```

build/outputs/production/config.xml

```xml
<server>
    <url>ssl://example.com</url>
    <port>9901</port>
</server>
```


## Usage

### Tasks

#### Clean

Deletes `build/outputs` and `build/archives` directory.

```
$ ./gradlew replacerClean
```

#### Generate (Copy and replace)

Copies source files and replace tags.

```
$ ./gradlew replacerGenerate
```

#### Archive

Archives the generated sources.  
This depends on `replacerGenerate` task.

```
$ ./gradlew replacerArchive
```

### Configurations

```groovy
replacer {
    // Definition of the targets:
    // e.g. develop, staging, production
    // You need to define them at least one.
    configurations {
        // These are treated as closures.
        // You can omit {}.
        dev {
        }
        production
    }

    // Source directory
    srcDir = "src"

    // Base source directory under srcDir
    srcMainDir = "main"

    // Template files directory under srcMainDir
    // or srcDir/<configuration name>/
    templateDir = "templates"

    // Generated files directory under build
    outputDir = "outputs"

    // Archived files directory under build
    archiveDir = "archives"

    // Properties file directory under templateDir
    properties = "build.properties"

    // DateFormat used for directory name under archiveDir
    archiveIdFormat = "yyyyMMddHHmmss"

    // Archive types for "replacerArchive" task.
    // Available type: zip, gzip, bzip2
    archiveType = "zip"

    // Files to be excluded
    excludes = [ ".gitkeep", ".swp" ]
}
```

## Why?

* I have had to maintain some configuration files for multiple environments.
  All the config files have same structure and almost all contents are identical.
  Only some values are different.
  Therefore, I wanted some tools to manage them with one template file and multiple properties files.  
  The tool should be:
    * Easy to set up
    * Easy to maintain configurations

  In my team, most project members have Java environment,
  so I thought the Gradle plugin is the best for that purpose.
* Gradle (groovy) has template engines,
  but they are too functional.  
  I wanted more intelligible, simple tool.  
  Also, I didn't want the tool to require scripts or
  complex setting files.  
  Just install, write minimum configurations and execute.

## License

Copyright (c) 2015 Soichiro Kashima  
Licensed under MIT license.  
See the bundled [LICENSE](https://github.com/ksoichiro/gradle-android-git/blob/master/LICENSE) file for details.
