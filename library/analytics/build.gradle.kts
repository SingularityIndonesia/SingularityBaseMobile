plugins {
    id("LibraryConventionV1")
}

android {
    namespace = "com.singularityindonesia.analytics"
}

dependencies {

    implementation(libs.core.ktx)
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation(project(":library:exception"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}