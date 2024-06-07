import org.jetbrains.compose.ComposeExtension

/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeaturePane")
}

android {
    namespace = "system.designsystem"
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

extensions.getByType<ComposeExtension>().resources {
    publicResClass = true
    packageOfResClass = "system.designsystem.resources"
    generateResClass = always
}

task("testClasses")