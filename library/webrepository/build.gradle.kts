plugins {
    id("LibraryConventionV1")
}

android {
    namespace = "com.singularityindonesia.webrepository"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}