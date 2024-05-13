# Singularity Codebase is Now a Multi Platform!!
*Always under the development process.*

I created this codebase to help startup companies kickstart their projects with Kotlin Multiplatform.
This code is free to use and distribute under the Creative Commons License.

This guide will help you to shape the thinking framework and understanding the codebase: [README_GUIDE.md](README_GUIDE.md)

## What is so cool
- **WebClient Generator Gradle Plugin**. The codebase is equipped with the web ApiGenerator gradle plugin. Once you enable this plugin in module gradle, all you need to do is define the api contract and this plugin will automatically convert the API contracts into web clients and generate all the response models automatically. See [ApiGenerator plugin](Docs/README_API_GENERATOR.md).
- Using **Gradle Composite Build**. This project is a Gradle **multi-project**. We separate the layers into several projects to allow for further development scaling. You can open whole project or each project individually.
- Is **multi-module**. This will make build process faster.
- Bussiness Context is **designed to be scaled horizontally**. No matter how many modules you add, the build performance will be fine during the development process. This is possible because the architecture of this codebase is designed to scale horizontally to optimize the **Gradle Caching**.
- Using the **Gradle Convention Plugin**. Although you may not need it much during the development process as it is automated, for more advanced developers, you can create your own conventions.
- Design system ready in **material3**.
- This codebase is designed to serve as the foundation **for large-scale projects**. You don't need to worry about how to scale up your codebase, because it's designed for that.

## Promises
More module will be added in the future such camera, biometic, etc.

## Request a Feature
I would love to know what you need. If you need a module, feel free to request for it.

## Contributing
Read [README_CONTRIBUTING.md](README_CONTRIBUTING.md)

## Temporary
It's unfortunate that the Kotlin context receiver feature is currently only available on the JVM.
However, the Kotlin team is actively working on it. Once the context receiver is ready, the design of this codebase will be finalized.

## Tips
Create new workspace scope to make it look better in Intelij/Android studio, you can use this pattern:
```
!file[*]:gradle//*&&!file[*]:iosApp//*&&!file[*]:.fleet//*&&!file[*]:gradle.properties&&!file[*]:gradlew&&!file[*]:.gitignore&&!file[*]:gradlew.bat&&!file[*]:.idea//*&&!file[*]:settings.gradle.kts
```

You can use create.sh to create a module easily.
```bash
# create example module in Main project.
$./create.sh -n "example"
# or
$./create.sh -t "main" -n "example"

# create example module in Shared project.
$./create.sh -t "shared" -n "example"

# create example module in System project.
$./create.sh -t "system" -n "example"
```

## Meet the author
[Stefanus Ayudha](https://www.linkedin.com/in/stefanus-ayudha-447a98b5/).
I would be happy to help you in creating an extraordinary starting.


