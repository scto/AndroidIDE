/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.templates.base.models

import com.itsaky.androidide.templates.ANDROID_GRADLE_PLUGIN_VERSION
import com.itsaky.androidide.templates.KOTLIN_VERSION

data class Plugin(val name: String, val id: String, val version: Version) {

  fun value(): String {
    return """
      alias(libs.plugin.${name.replace("-", ".")})
    """
      .trimIndent()
  }

  object Plugins {

    @JvmStatic
    val AndroidPlugin =
      parsePlugin("com.android.application", ANDROID_GRADLE_PLUGIN_VERSION)

    @JvmStatic
    val AndroidLibraryPlugin =
      parsePlugin("com.android.library", ANDROID_GRADLE_PLUGIN_VERSION)

    @JvmStatic
    val KotlinAndroidPlugin =
      parsePlugin("org.jetbrains.kotlin.android", KOTLIN_VERSION)
  }
}
