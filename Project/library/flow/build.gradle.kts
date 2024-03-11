plugins {
    id("LibraryConventionV1")
    id("FeatureContextReceiver")
}

android {
    namespace = "com.singularityindonesia.flow"
}

dependencies {

    implementation(libs.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}