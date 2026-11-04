plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvmToolchain(17)
    android {
        namespace = "com.example.swapcampus_zheng_li.composeapp"
        compileSdk = 37
        minSdk = 24
        androidResources { enable = true }
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
        withHostTestBuilder {}
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coil.compose)
            implementation(libs.compose.material.icons.core)
            api(project(":shared"))
            implementation(libs.kotlin.stdlib)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.coil.network.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.darwin)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

compose.resources {
    packageOfResClass = "com.example.swapcampus_zheng_li.composeapp.resources"
}
// These AndroidX libraries now publish iOS binaries. Use one coordinate for each
// library to avoid duplicate KLIB names from older JetBrains transitive artifacts.
configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("org.jetbrains.compose.annotation-internal:annotation"))
            .using(module("androidx.annotation:annotation:1.9.1"))
        substitute(module("org.jetbrains.compose.collection-internal:collection"))
            .using(module("androidx.collection:collection:1.5.0"))
        substitute(module("org.jetbrains.androidx.savedstate:savedstate"))
            .using(module("androidx.savedstate:savedstate:1.5.0"))
        substitute(module("org.jetbrains.androidx.savedstate:savedstate-compose"))
            .using(module("androidx.savedstate:savedstate-compose:1.5.0"))
        substitute(module("org.jetbrains.androidx.navigationevent:navigationevent"))
            .using(module("androidx.navigationevent:navigationevent:1.1.1"))
        substitute(module("org.jetbrains.androidx.navigationevent:navigationevent-compose"))
            .using(module("androidx.navigationevent:navigationevent-compose:1.1.1"))
    }
}
