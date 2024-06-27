package plugin.convention.companion

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.versionCatalog get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
