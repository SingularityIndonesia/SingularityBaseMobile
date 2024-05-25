plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("FeatureSerialization")
    id("FeatureHttpClient")
}

android {
    namespace = "shared.common"
}

task("testClasses")