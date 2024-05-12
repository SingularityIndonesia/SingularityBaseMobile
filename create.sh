#!/bin/bash

# Define the options and their default values
TYPE="main"
NAME=""

# Parse the command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    -t|--type)
      TYPE="$2"
      shift # past argument
      shift # past value
      ;;
    -n|--name)
      NAME="$2"
      shift # past argument
      shift # past value
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done


# PRESENTATION SCRIPT
presentation_script_pt1=$(cat <<'EOF'
plugins {
    id("LibraryConventionV1")
    id("FeatureJetpackCompose")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation("system:core")
            implementation("system:designsystem")
            implementation("shared:common")
EOF
)
presentation_script_pt2="

            implementation(project(\":$NAME:data\"))
            implementation(project(\":$NAME:model\"))
        }
        iosMain.dependencies {

        }
    }
}

android {

"
presentation_script_pt3="
    namespace = \"$TYPE.$NAME.presentation\""
presentation_script_pt4=$(cat <<'EOF'

    dependencies {
      debugImplementation(libs.compose.ui.tooling)
    }
}

task("testClasses")
EOF
)

presentation_script="$presentation_script_pt1$presentation_script_pt2$presentation_script_pt3$presentation_script_pt4"

data_script_pt1=$(cat <<'EOF'
plugins {
    id("LibraryConventionV1")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            implementation(libs.kotlinx.serialization.json)

            implementation("system:core")
            implementation("shared:common")
            implementation("shared:webrepository")

EOF
)
data_script_pt2="

            implementation(project(\":$NAME:model\"))
        }
        iosMain.dependencies {
          implementation(libs.ktor.client.ios)
        }
    }
}


android {

"
data_script_pt3="
    namespace = \"$TYPE.$NAME.data\""
data_script_pt4=$(cat <<'EOF'

    dependencies {

    }
}

task("testClasses")
EOF
)

data_script="$data_script_pt1$data_script_pt2$data_script_pt3$data_script_pt4"

model_pt1=$(cat <<'EOF'
plugins {
    id("LibraryConventionV1")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {

        }
    }
}

android {

EOF
)
model_pt2="
    namespace = \"$TYPE.$NAME.model\""
model_pt3=$(cat <<'EOF'

    dependencies {

    }
}

task("testClasses")
EOF
)

model_script="$model_pt1$model_pt2$model_pt3"

manifest_script=$(cat <<'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

</manifest>
EOF
)

createMain() {
  cd Main
  mkdir "$NAME"
  cd "$NAME"
  mkdir presentation
  mkdir presentation/src
  mkdir presentation/src/androidMain
  mkdir presentation/src/androidMain/res
  mkdir presentation/src/androidMain/res/mipmap-mdpi
  mkdir presentation/src/androidMain/res/drawable-v24
  mkdir presentation/src/androidMain/res/mipmap-hdpi
  mkdir presentation/src/androidMain/res/drawable
  mkdir presentation/src/androidMain/res/mipmap-xxxhdpi
  mkdir presentation/src/androidMain/res/mipmap-xxhdpi
  mkdir presentation/src/androidMain/res/values
  mkdir presentation/src/androidMain/res/mipmap-xhdpi
  mkdir presentation/src/androidMain/res/mipmap-anydpi-v26
  mkdir presentation/src/androidMain/kotlin

  mkdir presentation/src/commonMain
  mkdir presentation/src/commonMain/composeResources
  mkdir presentation/src/commonMain/composeResources/drawable
  mkdir presentation/src/commonMain/kotlin
  mkdir "presentation/src/commonMain/kotlin/$TYPE"
  mkdir "presentation/src/commonMain/kotlin/$TYPE/$NAME"
  mkdir "presentation/src/commonMain/kotlin/$TYPE/$NAME/presentation"

  mkdir presentation/src/iosMain
  mkdir presentation/src/iosMain/kotlin

  echo "$manifest_script" > presentation/src/androidMain/AndroidManifest.xml
  echo "$presentation_script" > presentation/build.gradle.kts

  mkdir data
  mkdir data/src
  mkdir data/src/androidMain
  mkdir data/src/androidMain/kotlin

  mkdir data/src/commonMain
  mkdir data/src/commonMain/kotlin
  mkdir "data/src/commonMain/kotlin/$TYPE"
  mkdir "data/src/commonMain/kotlin/$TYPE/$NAME"
  mkdir "data/src/commonMain/kotlin/$TYPE/$NAME/data"

  mkdir data/src/iosMain
  mkdir data/src/iosMain/kotlin

  echo "$manifest_script" > data/src/androidMain/AndroidManifest.xml
  echo "$data_script" > data/build.gradle.kts

  mkdir model
  mkdir model/src
  mkdir model/src/androidMain
  mkdir model/src/androidMain/kotlin
  mkdir model/src/commonMain
  mkdir model/src/commonMain/kotlin
  mkdir "model/src/commonMain/kotlin/$TYPE"
  mkdir "model/src/commonMain/kotlin/$TYPE/$NAME"
  mkdir "model/src/commonMain/kotlin/$TYPE/$NAME/model"

  echo "$manifest_script" > model/src/androidMain/AndroidManifest.xml
  echo "$model_script" > model/build.gradle.kts

  cd ..
  echo "

File(settingsDir, \"./$NAME\")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filterNot { it.name.contains(\"gradle\") }
    ?.filterNot { it.name.contains(\"build\") }
    ?.filterNot { it.name.contains(\".\") }
    ?.toList()
    ?.forEach { dir ->
        include(\":$NAME:\${dir.name}\")
    }" >> settings.gradle.kts

  # back to root
  cd ..
}


createMain