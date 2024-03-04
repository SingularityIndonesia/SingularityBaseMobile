plugins {
    id("LibraryConventionV1")
    id("FeatureJetpackCompose")
}

android {
    namespace = "com.singularityindonesia.designsystem"
}

dependencies {

    implementation(libs.core.ktx)

//    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}