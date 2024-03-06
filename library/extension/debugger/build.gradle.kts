plugins {
    id("LibraryConventionV1")
}

android {
    namespace = "com.singularityindonesia.debugger"
}

dependencies {

    implementation(libs.core.ktx)
    implementation(project(":library:main-context"))
    implementation(project(":library:webrepository"))

    debugImplementation (libs.pluto)
    releaseImplementation (libs.pluto.no.op)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.cio)

    debugImplementation(libs.pluto.plugins.bundle.core)
    releaseImplementation(libs.pluto.plugins.bundle.core.no.op)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}