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
        google()
        mavenCentral()
    }
}

rootProject.name = "Mysplash"
include(":app")
include(":core:data")
include(":core:common")
include(":core:domain")
include(":core:ui")
include(":features:user")
include(":features:topics")

