plugins {
    id("LibraryConventionV1")
}

android {
    namespace = "com.singularityindonesia.debugger"
}

dependencies {

    implementation(libs.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}