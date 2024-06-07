package plugin.convention.companion

import com.android.build.api.dsl.ApplicationBuildType
import org.gradle.api.NamedDomainObjectContainer

fun NamedDomainObjectContainer<ApplicationBuildType>.onDevDebug(
    bloc: ApplicationBuildType.() -> Unit
) {
    getByName("devDebug", bloc)
}

fun NamedDomainObjectContainer<ApplicationBuildType>.onDevRelease(
    bloc: ApplicationBuildType.() -> Unit
) {
    getByName("devRelease", bloc)
}

fun NamedDomainObjectContainer<ApplicationBuildType>.onStagingDebug(
    bloc: ApplicationBuildType.() -> Unit
) {
    getByName("stagingDebug", bloc)
}

fun NamedDomainObjectContainer<ApplicationBuildType>.onStagingRelease(
    bloc: ApplicationBuildType.() -> Unit
) {
    getByName("stagingRelease", bloc)
}

fun NamedDomainObjectContainer<ApplicationBuildType>.onProdDebug(
    bloc: ApplicationBuildType.() -> Unit
) {
    getByName("prodDebug", bloc)
}

fun NamedDomainObjectContainer<ApplicationBuildType>.onProdRelease(
    bloc: ApplicationBuildType.() -> Unit
) {
    getByName("prodRelease", bloc)
}