package plugin.convention.companion

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.withKotlinMultiplatformExtension(
    bloc: KotlinMultiplatformExtension.() -> Unit
) {
    extensions.configure<KotlinMultiplatformExtension>(bloc)
}

fun Project.withBaseExtension(
    bloc: BaseExtension.() -> Unit
) {
    extensions.configure<BaseExtension>(bloc)
}

fun Project.withLibraryExtension(
    bloc: LibraryExtension.() -> Unit
) {
    extensions.configure<LibraryExtension>(bloc)
}

fun Project.withBaseAppModuleExtension(
    bloc: BaseAppModuleExtension.()-> Unit
) {
    extensions.configure<BaseAppModuleExtension>(bloc)
}

fun Project.withPluginManager(bloc: PluginManager.()-> Unit) {
    bloc.invoke(pluginManager)
}