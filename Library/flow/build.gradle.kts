plugins {
    id("LibraryConventionV1")
    id("FeatureContextReceiver")
}

android {
    namespace = "com.singularityindonesia.flow"
}

dependencies {

    implementation(project(":data"))
    implementation(project(":exception"))

    implementation(libs.android.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.ext.junit)
    androidTestImplementation(libs.android.espresso.core)
}