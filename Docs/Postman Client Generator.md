# Postman Web Client Generator Plugin
Postman Web Client Generator is a Gradle plugin that I created to automatically generate web API clients. 
See: https://plugins.gradle.org/plugin/io.github.stefanusayudha.PostmanClientGenerator .

You simply need to apply the plugin to your Gradle build as follows:
``` kotlin
plugins {
    ...
    // you will need kotlin serialization as well
    kotlin("plugin.serialization")
    // api generator
    id("io.github.stefanusayudha.PostmanClientGenerator")
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


Next, put your postman api collection anywhere within the module.
![Img1](https://github.com/SingularityIndonesia/SingularityBaseMobile/blob/base/Docs/Screenshot%202024-05-17%20at%2014.37.54.png)

Finally all you need is to build the project and clients will automatically generated.
![Img](https://github.com/SingularityIndonesia/SingularityBaseMobile/blob/base/Docs/Screenshot%202024-05-17%20at%2015.29.28.png)

Example Result:
![Img](https://github.com/SingularityIndonesia/SingularityBaseMobile/blob/base/Docs/Screenshot%202024-05-17%20at%2015.23.28.png)
# Important
The collection files must be named with the suffix `.postman_collection.json`. ex: `Cart New.postman_collection.json`. You can have spaces noproblem.

Your code will be generated in `build/generated/kotlin/postman_client` directory. The directory is automatically added to the sourceSet, you can consume the functions right away.

# Notes
- Number prediction are limited to Int, Long, Float, and Double. BigDecimal and BigInteger are currently not yet supported.
- Avoid using scientifict notation for more accurate number prediction. If you use scientifict notation, type willbe resolved as string.
- Multi dimensional array model is not yet supported.
- List response body is not yet supported. Please use json object instead for response body.
