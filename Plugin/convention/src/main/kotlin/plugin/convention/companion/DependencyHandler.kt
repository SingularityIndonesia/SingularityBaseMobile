package plugin.convention.companion

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

val debugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("debugImplementation", dependencyNotation)
    }

val devDebugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("devDebugImplementation", dependencyNotation)
    }

val stagingDebugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("stagingDebugImplementation", dependencyNotation)
    }

val prodDebugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("prodDebugImplementation", dependencyNotation)
    }

val releaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("releaseImplementation", dependencyNotation)
    }

val devReleaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("devReleaseImplementation", dependencyNotation)
    }

val stagingReleaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("stagingReleaseImplementation", dependencyNotation)
    }

val prodReleaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("prodReleaseImplementation", dependencyNotation)
    }

val debugAllImplementation: DependencyHandler.(Any) -> Unit =
    { pkg ->
        listOf(
            debugImplementation,
            devDebugImplementation,
            stagingDebugImplementation,
            prodDebugImplementation
        ).forEach {
            it(pkg)
        }
    }

val releaseAllImplementation: DependencyHandler.(Any) -> Unit =
    { pkg ->
        listOf(
            releaseImplementation,
            devReleaseImplementation,
            stagingReleaseImplementation,
            prodReleaseImplementation
        ).forEach {
            it(pkg)
        }
    }

// short hands
fun KotlinDependencyHandler.System(pkgName: String) {
    implementation("system:$pkgName")
}

fun KotlinDependencyHandler.Shared(pkgName: String) {
    implementation("shared:$pkgName")
}

/**
 * Import data module from project neighbor.
 */
fun KotlinDependencyHandler.data(pkgName: String) {
    implementation(project(":$pkgName:data"))
}

/**
 * Import model module from project neighbor.
 */
fun KotlinDependencyHandler.model(pkgName: String) {
    implementation(project(":$pkgName:model"))
}

/**
 * Import presentation module from project neighbor.
 */
fun KotlinDependencyHandler.presentation(pkgName: String) {
    implementation(project(":$pkgName:presentation"))
}