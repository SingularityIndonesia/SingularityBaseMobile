plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
}

android {
    namespace = "shared.database"
}

task("testClasses")
