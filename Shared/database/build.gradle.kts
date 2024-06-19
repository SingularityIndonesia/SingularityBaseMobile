plugins {
    id("LibraryConventionV2")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
}

android {
    namespace = "shared.database"
}

task("testClasses")
