plugins {
    id("LibraryConventionV1")
    id("FeatureContextReceiver")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.singularityindonesia.webrepository"
}

dependencies {
    implementation(project(":library:model"))

    implementation(libs.core.ktx)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.cio)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}