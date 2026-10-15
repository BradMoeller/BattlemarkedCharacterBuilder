import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("battlemarked")
        browser {
            commonWebpackConfig {
                outputFileName = "battlemarked.js"
            }
        }
        binaries.executable()
    }

    // Mobile targets come later: add androidTarget() / iosArm64() etc. here.
    // All UI + data lives in commonMain, so nothing else needs to move.

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
    }
}
