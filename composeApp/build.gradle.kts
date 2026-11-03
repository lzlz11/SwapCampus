plugins {
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
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)
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