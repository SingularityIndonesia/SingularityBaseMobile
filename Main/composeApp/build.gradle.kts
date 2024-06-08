/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.internal.utils.getLocalProperty
import plugin.convention.companion.ReleaseNote
import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.env
import plugin.convention.companion.model
import plugin.convention.companion.onDevDebug
import plugin.convention.companion.onDevRelease
import plugin.convention.companion.onProdDebug
import plugin.convention.companion.onProdRelease
import plugin.convention.companion.onStagingDebug
import plugin.convention.companion.onStagingRelease
import plugin.convention.companion.presentation

plugins {
    id("AppConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeaturePane")
    id("FeatureNavigation")
    id("FeatureSerialization")
    id("FeatureHttpClient")
    id("FeatureAndroidPluto")
}

val ReleaseNote = "ReleaseNote.md"
    .let {
        val file = File(project.projectDir, it)
        ReleaseNote(file)
    }

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.material)
        }
        commonMain.dependencies {
            System("core")
            System("designsystem")
            Shared("common")
            Shared("webclient")

            presentation("dashboard")
            model("dashboard")
            presentation("example")
            model("example")
            presentation("ai_chat")
            model("ai_chat")
        }
    }
}

android {
    namespace = "com.singularity.basemobile"

    defaultConfig {
        applicationId = "com.singularity.basemobile"

        versionCode = ReleaseNote.versionCode
        versionName = ReleaseNote.versionName

        buildConfigField("String", "HOST", env("DEV_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("DEV_TODO_API_BASE_PATH"))
        buildConfigField("String", "GEMINI_API_KEY", env("GEMINI_API_KEY"))
    }
}

// read: Docs/Centralized Context Control.md
android.buildTypes {
    onDevDebug {
        buildConfigField("String", "HOST", env("DEV_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("DEV_TODO_API_BASE_PATH"))
    }

    onDevRelease {
        buildConfigField("String", "HOST", env("DEV_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("DEV_TODO_API_BASE_PATH"))
    }

    onStagingDebug {
        buildConfigField("String", "HOST", env("STAGE_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("STAGE_TODO_API_BASE_PATH"))
    }

    onStagingRelease {
        buildConfigField("String", "HOST", env("STAGE_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("STAGE_TODO_API_BASE_PATH"))
    }

    onProdDebug {
        buildConfigField("String", "HOST", env("PROD_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("PROD_TODO_API_BASE_PATH"))
    }

    onProdRelease {
        buildConfigField("String", "HOST", env("PROD_HOST"))
        buildConfigField("String", "TODO_API_BASE_PATH", env("PROD_TODO_API_BASE_PATH"))
    }
}

// experimental
composeCompiler {
    enableStrongSkippingMode = true
    includeSourceInformation = true
    stabilityConfigurationFile = project.projectDir.resolve("stability_config.conf")
}

task("testClasses")