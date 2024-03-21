plugins {
    id("LibraryConventionV1")
    id("FeatureContextReceiver")
}

android {
    namespace = "com.singularityindonesia.viewmodel"
}

dependencies {

    implementation("data:main")
    implementation("exception:main")

    implementation(libs.android.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.ext.junit)
    androidTestImplementation(libs.android.espresso.core)
}