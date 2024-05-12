# Singularity Codebase is Now a Multi Platform!!
*Always under the development process.*

I created this codebase to help startup companies kickstart their projects with Kotlin Multiplatform.
This code is free to use and distribute under the Creative Commons License.

This guide will help you to shape the thinking framework and understanding the codebase: [README_GUIDE.md](README_GUIDE.md)
## Contributing
Read [README_CONTRIBUTING.md](README_CONTRIBUTING.md)

## Temporary
It's unfortunate that the Kotlin context receiver feature is currently only available on the JVM.
However, the Kotlin team is actively working on it. Once the context receiver is ready, the design of this codebase will be finalized.

## Tips
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


