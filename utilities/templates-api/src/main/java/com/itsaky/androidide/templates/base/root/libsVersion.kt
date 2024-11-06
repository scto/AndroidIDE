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

package com.itsaky.androidide.templates.base.root

import com.itsaky.androidide.templates.base.ProjectTemplateBuilder
import com.itsaky.androidide.templates.base.models.Dependency
import com.itsaky.androidide.templates.base.models.Plugin
import java.io.File

internal fun ProjectTemplateBuilder.libsVersionsToml() {
  val name = "libs.versions.toml"
  val file = File(data.projectDir, "gradle/${name}")
  file.parentFile!!.mkdirs()

  executor.save(libsVersionTomlSrc(), file)
}

internal fun ProjectTemplateBuilder.libsVersionTomlSrc(): String {
  val dependencies = getDependencies()
  val plugins = getPlugins()

  return """
[versions]
${versionsSrc(dependencies, plugins)}

[libraries]
${librariesSrc(dependencies)}

[plugins]
${pluginsSrc(plugins)}


"""
}

private fun ProjectTemplateBuilder.versionsSrc(
  dependencies: HashSet<Dependency>,
  plugins: HashSet<Plugin>,
): String {

  val pluginVersions =
    plugins.joinToString("\n") { "${it.version.name} = ${it.version.version}" }

  val dependenciesVersions =
    dependencies.joinToString("\n") {
      val version = it.version
      if (version != null) {
        "${version.name} = ${version.version}"
      } else ""
    }

  return "$pluginVersions\n$dependenciesVersions".trim()
}

private fun ProjectTemplateBuilder.librariesSrc(
  dependencies: HashSet<Dependency>
): String {

  return dependencies
    .joinToString("\n") {
      val group = it.group
      val artifact = it.artifact
      val version = it.version

      "$artifact = { group = \"$group\", name = \"$artifact\"${if (version != null) ", version.ref = \"${version.name}\"" else ""} }"
    }
    .trim()
}

private fun ProjectTemplateBuilder.pluginsSrc(
  plugins: HashSet<Plugin>
): String {
  return plugins
    .joinToString("\n") {
      val name = it.name
      val id = it.id
      val version = it.version

      "$name = { id = \"$id\", version.ref = \"${version.name}}\""
    }
    .trim()
}

private fun ProjectTemplateBuilder.getDependencies(): HashSet<Dependency> {
  val dependencies = hashSetOf<Dependency>()
  modules.forEach { module ->
    dependencies.addAll(module.libraries.platforms)
    dependencies.addAll(module.libraries.dependencies)
  }

  return dependencies
}

private fun ProjectTemplateBuilder.getPlugins(): HashSet<Plugin> {
  val plugins = hashSetOf<Plugin>()
  modules.forEach { module -> plugins.addAll(module.libraries.plugins) }

  return plugins
}
