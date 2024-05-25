plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureHttpClient")
}

android {
    namespace = "system.core"
}

task("testClasses")