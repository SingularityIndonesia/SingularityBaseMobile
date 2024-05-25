plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureSerialization")
    id("FeatureKtor")
}

android {
    namespace = "shared.common"
}

task("testClasses")