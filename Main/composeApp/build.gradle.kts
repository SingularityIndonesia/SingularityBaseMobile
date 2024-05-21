import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("AppConventionV1")
    id("FeatureJetpackCompose")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.compose.navigation)

            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:common")
            implementation("shared:webrepository")

            implementation(project(":example:presentation"))
            implementation(project(":example:model"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    namespace = "com.singularity.basemobile"
    defaultConfig {
        applicationId = "com.singularity.basemobile"
        versionCode = 1
        versionName = "1.0"
    }

    // signing config
    val signingConfig = generateSigningConfig(this)

    // You only need to define build variant once here.
    // No need to define it in every module, because only composeApp module can have contexts,
    // and only composeApp module can access the environment variables.
    // Other modules are prohibited.
    defineBuildVariants(
        this,
        signingConfig
    )

    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

fun generateSigningConfig(
    mod: BaseAppModuleExtension
) = with(mod) {
    val keystore = run {
        Properties()
            .apply {
                load(
                    FileInputStream(
                        project.file("${project.projectDir}/keystore.properties")
                    )
                )
            }
    }

    signingConfigs.create("all") {
        storeFile(file(keystore.getProperty("store.file")))
        storePassword = keystore.getProperty("store.password")
        keyAlias = keystore.getProperty("store.key.alias")
        keyPassword = keystore.getProperty("store.key.password")
    }
}

fun defineBuildVariants(
    mod: BaseAppModuleExtension,
    signingConfig: ApkSigningConfig?
) = with(mod) {
    // put your environment variable in environment.properties file within this composeApp project dir
    val env = run {
        Properties()
            .apply {
                load(
                    FileInputStream(
                        project.file("${project.projectDir}/environment.properties")
                    )
                )
            }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isMinifyEnabled = true
            this.signingConfig = signingConfig
        }

        debug {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isMinifyEnabled = false
            this.signingConfig = signingConfig
        }

        create("devDebug") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
            buildConfigField(
                "String",
                "HOST",
                env.getProperty("DEV_HOST")
            )
            buildConfigField(
                "String",
                "API_BASE_PATH",
                env.getProperty("DEV_API_BASE_PATH")
            )
        }

        create("devRelease") {
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            buildConfigField(
                "String",
                "HOST",
                env.getProperty("DEV_HOST")
            )
            buildConfigField(
                "String",
                "API_BASE_PATH",
                env.getProperty("DEV_API_BASE_PATH")
            )
        }

        create("stagingDebug") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
            buildConfigField(
                "String",
                "HOST",
                env.getProperty("STAGE_HOST")
            )
            buildConfigField(
                "String",
                "API_BASE_PATH",
                env.getProperty("STAGE_API_BASE_PATH")
            )
        }

        create("stagingRelease") {
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            buildConfigField(
                "String",
                "HOST",
                env.getProperty("STAGE_HOST")
            )
            buildConfigField(
                "String",
                "API_BASE_PATH",
                env.getProperty("STAGE_API_BASE_PATH")
            )
        }

        create("prodDebug") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
            buildConfigField(
                "String",
                "HOST",
                env.getProperty("PROD_HOST")
            )
            buildConfigField(
                "String",
                "API_BASE_PATH",
                env.getProperty("PROD_API_BASE_PATH")
            )
        }

        create("prodRelease") {
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            buildConfigField(
                "String",
                "HOST",
                env.getProperty("PROD_HOST")
            )
            buildConfigField(
                "String",
                "API_BASE_PATH",
                env.getProperty("PROD_API_BASE_PATH")
            )
        }
    }
}


task("testClasses")