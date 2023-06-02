/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

// hack to be run on mac m*
package org.jetbrains.kotlin.gradle.native

import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.BaseGradleIT
import org.jetbrains.kotlin.gradle.testbase.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.condition.OS

@OsCondition(supportedOn = [OS.MAC], enabledOnCI = [OS.MAC])
@NativeGradlePluginTests
class JsSetupConfigurationCacheIT : KGPBaseTest() {
    @Suppress("DEPRECATION")
    private val defaultJsOptions = BuildOptions.JsOptions(
        useIrBackend = true,
        jsCompilerType = KotlinJsCompilerType.IR
    )

    override val defaultBuildOptions =
        super.defaultBuildOptions.copy(
            jsOptions = defaultJsOptions,
            configurationCache = true,
            configurationCacheProblems = BaseGradleIT.ConfigurationCacheProblems.FAIL
        )

    // hack to be run on Mac m*
    @DisplayName("Check Node.JS setup on different platforms")
    @GradleTest
    @GradleTestVersions(minVersion = TestVersions.Gradle.G_7_6)
    fun checkNodeJsSetup(gradleVersion: GradleVersion) {
        project("kotlin-js-browser-project", gradleVersion) {
            build("kotlinUpgradeYarnLock") {
                assertTasksExecuted(":kotlinUpgradeYarnLock")
                assertConfigurationCacheStored()
            }

            build("kotlinUpgradeYarnLock") {
                assertTasksUpToDate(":kotlinUpgradeYarnLock")
                assertConfigurationCacheReused()
            }
        }
    }
}