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

package com.itsaky.androidide.templates.base

import com.itsaky.androidide.templates.base.models.Dependency
import com.itsaky.androidide.templates.base.models.Plugin
import com.itsaky.androidide.templates.base.models.defaultDependency
import com.itsaky.androidide.templates.base.models.parsePlugin

/**
 * Project template dependencies.
 *
 * @author Akash Yadav, Felipe Teixeira
 */
data class ModuleTemplateLibraries(
  internal val platforms: HashSet<Dependency> = hashSetOf(),
  internal val dependencies: HashSet<Dependency> = hashSetOf(),
  internal val plugins: HashSet<Plugin> = hashSetOf(),
) {

  /**
   * Add the dependency with the given maven coordinates to this module with the
   * [Implementation][com.itsaky.androidide.templates.base.models.DependencyConfiguration.Implementation]
   * configuration.
   *
   * @param group The group ID of the dependency.
   * @param artifact The artifact of the dependency.
   * @param version The version of the dependency.
   * @param isPlatform Whether this dependency declares a BOM.
   */
  @JvmOverloads
  fun addDependency(
    group: String,
    artifact: String,
    version: String,
    isPlatform: Boolean = false,
  ) {
    addDependency(defaultDependency(group, artifact, version), isPlatform)
  }

  /**
   * Adds the given dependency to the `build.gradle[.kts]` file for this module.
   *
   * @param dependency The dependency to add.
   * @param isPlatform Whether this dependency declares a BOM.
   */
  @JvmOverloads
  fun addDependency(dependency: Dependency, isPlatform: Boolean = false) {
    if (isPlatform) {
      this.platforms.add(dependency)
    } else {
      this.dependencies.add(dependency)
    }
  }

  @JvmOverloads
  fun addPlugin(id: String, version: String) {
    addPlugin(parsePlugin(id, version))
  }

  @JvmOverloads
  fun addPlugin(plugin: Plugin) {
    this.plugins.add(plugin)
  }
}
