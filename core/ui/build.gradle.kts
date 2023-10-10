plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    val sdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    namespace = "com.timurkhabibulin.ui"
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:domain"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.ui:ui-graphics:1.5.3")
    implementation("androidx.compose.material3:material3:1.2.0-alpha09")
    implementation("androidx.compose.ui:ui-tooling:1.5.3")
    implementation(platform("androidx.compose:compose-bom:2023.10.00"))

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.paging:paging-compose:3.2.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.material:material:1.6.0-alpha07")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.0-alpha07")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.3")
}