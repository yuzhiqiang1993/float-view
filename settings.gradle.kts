pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()

        // SNAPSHOT 仓库
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            content {
                includeVersionByRegex(".*", ".*", ".*-SNAPSHOT")
            }
        }
    }
}

rootProject.name = "floating-view"
include(":app")
include(":floating-view")
