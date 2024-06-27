#!/bin/bash
# Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
# You are not allowed to remove the copyright. Unless you have a "free software" licence.

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

# Check if the target is unrecognized or not one of the allowed targets
if [[ ! "$TYPE" =~ ^(main|shared|system)$ ]]; then
  echo "Unrecognized or invalid target"
  exit 1
fi

# PRESENTATION SCRIPT
presentation_script_pt1=$(cat <<'EOF'
import plugin.convention.companion.Shared
import plugin.convention.companion.System
import plugin.convention.companion.model
import plugin.convention.companion.data

plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeaturePane")
    id("FeatureSerialization")
    id("FeatureHttpClient")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
            System("designsystem")
            Shared("common")
EOF
)
presentation_script_pt2="

            data(\"$NAME\")
            model(\"$NAME\")
        }
    }
}

android {
"
presentation_script_pt3="
    namespace = \"$TYPE.$NAME.presentation\""
presentation_script_pt4=$(cat <<'EOF'

}

task("testClasses")
EOF
)

presentation_script="$presentation_script_pt1$presentation_script_pt2$presentation_script_pt3$presentation_script_pt4"

data_script_pt1=$(cat <<'EOF'
import plugin.convention.companion.model
import plugin.convention.companion.Shared
import plugin.convention.companion.System

plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeatureSerialization")
    id("FeatureHttpClient")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            System("core")
            Shared("common")

EOF
)
data_script_pt2="

            model(\"$NAME\")
        }
    }
}

android {
"
data_script_pt3="
    namespace = \"$TYPE.$NAME.data\""
data_script_pt4=$(cat <<'EOF'

}

task("testClasses")
EOF
)

data_script="$data_script_pt1$data_script_pt2$data_script_pt3$data_script_pt4"

model_pt1=$(cat <<'EOF'
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
    id("FeatureSerialization")
}

android {
EOF
)
model_pt2="
    namespace = \"$TYPE.$NAME.model\""
model_pt3=$(cat <<'EOF'

}

task("testClasses")
EOF
)

model_script="$model_pt1$model_pt2$model_pt3"

common_library_pt1=$(cat <<'EOF'
plugins {
    id("LibraryConventionV1")
    id("CompileIOS")
    id("CompileWasm")
    id("FeatureCoroutine")
}

android {
EOF
)
common_library_pt2="
    namespace = \"$TYPE.$NAME\""
common_library_pt3=$(cat <<'EOF'

}

task("testClasses")
EOF
)

common_library_scipt="$common_library_pt1$common_library_pt2$common_library_pt3"

createMain() {
  # shellcheck disable=SC2164
  mkdir "Main/$NAME"
  # shellcheck disable=SC2164
  cd "Main/$NAME"

  mkdir -p "presentation/common/$NAME"
  echo "$presentation_script" > presentation/build.gradle.kts

  mkdir -p "data/common/$NAME"
  echo "$data_script" > data/build.gradle.kts

  mkdir -p "model/common/$NAME"

  echo "$model_script" > model/build.gradle.kts

  cd ..
  echo "

File(settingsDir, \"./$NAME\")
    .listFiles()
    ?.asSequence()
    ?.filter { it.isDirectory }
    ?.filter { it.listFiles()?.map { it.name }?.contains(\"build.gradle.kts\") == true }
    ?.onEach { dir ->
        include(\":$NAME:\${dir.name}\")
    }
    ?.toList()" >> settings.gradle.kts

  # back to root
  cd ..
}

createCommonLibrary() {
  cd "$TYPE"

  mkdir -p "$NAME/common/$NAME"
  echo "$common_library_scipt" > "$NAME/build.gradle.kts"

  # back to root
  cd ..
}

# Run the appropriate create function based on the target
case "$TYPE" in
  "main")
    createMain
    ;;
  "shared")
    createCommonLibrary
    ;;
  "system")
    createCommonLibrary
    ;;
esac
