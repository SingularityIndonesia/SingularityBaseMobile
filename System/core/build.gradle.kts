plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureKtor")
}

android {
    namespace = "system.core"
}

task("testClasses")