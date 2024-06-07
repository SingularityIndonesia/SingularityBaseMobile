plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
}

android {
    namespace = "system.core"
}

task("testClasses")