// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0-rc01" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.0-rc01" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

project.ext {
    extra["sdkVersion"] = 34
    extra["minSdkVersion"] = 26

    extra["jvmTargetVersion"] = "17"
    extra["sourceCompatibilityVersion"] = JavaVersion.VERSION_17
    extra["targetCompatibilityVersion"] = JavaVersion.VERSION_17
}
