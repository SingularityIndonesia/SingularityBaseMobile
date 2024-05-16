# Postman Web Client Generator Plugin
Postman Web Client Generator is a Gradle plugin that I created to automatically generate web API clients. You simply need to apply the plugin to your Gradle build as follows:
``` kotlin
plugins {
    ...
    // you will need kotlin serialization as well
    kotlin("plugin.serialization")
    // api generator
    id("PostmanClientGenerator")
}

kotlin {
    sourceSets {
        ...
        commonMain.dependencies {
            ...
            // make sure you add ktor to the dependencies
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {
          implementation(libs.ktor.client.ios)
        }
    }
}

```

Next, put your postman api collection anywhere within the module, and this plugin will generate web client automatically for you.

# Important
The collection files must be named with the suffix `.postman_collection.json`. ex: `Cart New.postman_collection.json`. You can have spaces noproblem.

Your code will be generated in `build/generated/kotlin/postman_client` directory.
