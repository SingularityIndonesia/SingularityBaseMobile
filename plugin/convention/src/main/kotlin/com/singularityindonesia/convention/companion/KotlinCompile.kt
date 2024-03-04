package com.singularityindonesia.convention.companion

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.kotlinCompile(
    block: KotlinCompile.() -> Unit
) {
    tasks.withType<KotlinCompile>(block)
}