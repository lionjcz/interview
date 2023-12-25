pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
//    plugins {
//        id("org.jetbrains.kotlin.android") version "1.8.0" apply false
//        // ...其他插件設定
//    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Interview"

include(":app")
 