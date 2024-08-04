package plugin.convention.companion

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

fun Project.withAllOpenExtension(bloc: AllOpenExtension.() -> Unit) {
    extensions.configure<AllOpenExtension>(bloc)
}
