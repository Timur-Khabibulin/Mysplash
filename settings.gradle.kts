pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs"){
            from(files("libs.toml"))
        }
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
include(":features:home")
include(":features:search")
