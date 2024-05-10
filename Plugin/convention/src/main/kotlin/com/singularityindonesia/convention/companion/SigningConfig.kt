/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention.companion

import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.FileInputStream
import java.util.*


/**
 * you need keystore file in the root project.
 * the keystore should contain the following variables:
 * - `store.file` points to the .jks file
 * - `store.password`
 * - `store.key.alias`
 * - `store.key.password`
 */
const val KEYSTORE_PROPERTIES_FILE_NAME = "keystore.properties"

fun getSigningConfig(
    project: Project,
    extension: CommonExtension<*, *, *, *, *>
): ApkSigningConfig? =
    Properties()
        .let {
            val fileLoaded = runCatching {
                it.load(
                    FileInputStream(
                        project.file("${project.rootProject.project.projectDir}/$KEYSTORE_PROPERTIES_FILE_NAME")
                    )
                )
            }.isSuccess

            if (fileLoaded) {
                it
            } else {
                null
            }
        }
        ?.let {
            with(extension) {
                signingConfigs.create("all") {
                    storeFile =
                        project.file("${project.rootProject.project.projectDir}/${it.getProperty("store.file")}")
                    storePassword = it.getProperty("store.password")
                    keyAlias = it.getProperty("store.key.alias")
                    keyPassword = it.getProperty("store.key.password")
                }
                signingConfigs.getByName("all")
            }
        }
