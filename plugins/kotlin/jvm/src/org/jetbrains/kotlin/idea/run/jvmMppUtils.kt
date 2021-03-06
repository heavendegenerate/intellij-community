// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.kotlin.idea.run

import com.intellij.openapi.module.Module
import com.intellij.openapi.util.registry.Registry
import org.jetbrains.kotlin.idea.project.platform
import org.jetbrains.kotlin.platform.jvm.isJvm

fun Module.asJvmModule(): Module? {
    if (platform.isJvm()) return this

    return null
}

fun forceGradleRunnerInMPP() = Registry.`is`("kotlin.mpp.tests.force.gradle", true)