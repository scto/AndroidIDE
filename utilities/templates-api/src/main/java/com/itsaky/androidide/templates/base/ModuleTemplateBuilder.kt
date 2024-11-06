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

import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.ModuleTemplate
import com.itsaky.androidide.templates.ModuleTemplateData
import com.itsaky.androidide.templates.ModuleTemplateRecipeResult
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.SrcSet
import com.itsaky.androidide.templates.TemplateBuilder
import com.itsaky.androidide.templates.TemplateRecipeConfigurator
import com.itsaky.androidide.templates.TemplateRecipeFinalizer
import com.itsaky.androidide.templates.base.models.Dependency
import com.itsaky.androidide.templates.base.models.Plugin
import com.itsaky.androidide.templates.base.util.SourceWriter
import java.io.File

/**
 * Abstract [TemplateBuilder] for building module projects.
 *
 * @property name The name of the module (gradle format, e.g. ':app').
 * @author Akash Yadav
 */
abstract class ModuleTemplateBuilder : ExecutorDataTemplateBuilder<ModuleTemplateRecipeResult, ModuleTemplateData>() {

  internal val libraries = ModuleTemplateLibraries()

  @PublishedApi
  internal val sourceWriter = SourceWriter()

  @PublishedApi
  internal var _name: String? = null

  val name: String
    get() = checkNotNull(_name) { "Name not set to module template" }

  val platforms: Set<Dependency>
    get() = libraries.platforms

  val dependencies: Set<Dependency>
    get() = libraries.dependencies

  val plugins: Set<Plugin>
    get() = libraries.plugins

  open fun RecipeExecutor.preConfig() {}

  open fun RecipeExecutor.postConfig() {}

  /**
   * Get the asset path for base module project template.
   *
   * @param path The path for the asset.
   * @see com.itsaky.androidide.templates.base.baseAsset
   */
  open fun baseAsset(path: String) =
    com.itsaky.androidide.templates.base.util.baseAsset("module", path)

  /** Get the `build.gradle[.kts]` file for this module.l */
  fun buildGradleFile(): File {
    return data.buildGradleFile()
  }

  /**
   * Get the path to the Java source file in the given [source set][srcSet] with
   * the given [packageName] and the [simple name][name].
   */
  fun srcFilePath(
    srcSet: SrcSet,
    packageName: String,
    name: String,
    language: Language,
  ): File {
    var path = packageName.replace('.', '/')
    path += "/${name}"
    path += ".${language.ext}"

    return File(javaSrc(srcSet), path)
  }

  /** Get the `java` sources directory for the [SrcSet.Main] source set. */
  fun mainJavaSrc(): File {
    return javaSrc(SrcSet.Main)
  }

  /** Get the `resources` directory for the [SrcSet.Main] source set. */
  fun mainResourcesDir(): File {
    return javaSrc(SrcSet.Main)
  }

  /** Get the `resources` directory for the given [source set][srcSet]. */
  fun resourcesDir(srcSet: SrcSet): File {
    return File(srcFolder(srcSet), "resources").also { it.mkdirs() }
  }

  /** Get the `java` source directory for the given [srcSet]. */
  fun javaSrc(srcSet: SrcSet): File {
    return File(srcFolder(srcSet), "java").also { it.mkdirs() }
  }

  /**
   * Get the source directory for the given [srcSet].
   *
   * @param srcSet The type of source directory.
   */
  fun srcFolder(srcSet: SrcSet): File {
    return data.srcFolder(srcSet)
  }

  /**
   * Configure the source files for this module.
   *
   * @param configure Function for configuring the source files.
   */
  inline fun RecipeExecutor.sources(
    crossinline configure: SourceWriter.() -> Unit
  ) {
    sourceWriter.apply(configure)
  }

  /**
   * Common pre-recipe configuration.
   *
   * @param moduleData Called after the base configuration is setup and before
   *   the [recipe] is executed. Caller can perform its own pre-recipe
   *   configuration here. Returns the [ModuleTemplateData] instance.
   */
  inline fun commonPreRecipe(
    crossinline extraConfig: TemplateRecipeConfigurator = {},
    crossinline moduleData: RecipeExecutor.() -> ModuleTemplateData,
  ): TemplateRecipeConfigurator = {
    val data = moduleData()

    this@ModuleTemplateBuilder._data = data
    this@ModuleTemplateBuilder._name = data.name
    this@ModuleTemplateBuilder._executor = this

    data.projectDir.mkdirs()

    // Create the main source directory
    srcFolder(SrcSet.Main)

    preConfig()
    extraConfig()
  }

  /**
   * Common post-recipe configuration.
   *
   * @param extraConfig Called after the [recipe] is executed. Caller can
   *   perform its own post-recipe configuration here.
   */
  fun commonPostRecipe(
    extraConfig: TemplateRecipeFinalizer = {}
  ): TemplateRecipeFinalizer = {

    // Write build.gradle[.kts]
    buildGradle()

    postConfig()
    extraConfig()
  }

  @JvmOverloads
  fun addDependency(
    group: String,
    artifact: String,
    version: String,
    isPlatform: Boolean = false,
  ) {
    libraries.addDependency(
      group = group,
      artifact = artifact,
      version = version,
      isPlatform = isPlatform,
    )
  }

  @JvmOverloads
  fun addDependency(dependency: Dependency, isPlatform: Boolean = false) {
    libraries.addDependency(dependency, isPlatform)
  }

  @JvmOverloads
  fun addPlugin(id: String, version: String) {
    libraries.addPlugin(id, version)
  }

  @JvmOverloads
  fun addPlugin(plugin: Plugin) {
    libraries.addPlugin(plugin)
  }

  /** Writes the `build.gradle[.kts]` file for this module. */
  abstract fun RecipeExecutor.buildGradle()

  override fun buildInternal(): ModuleTemplate {
    return ModuleTemplate(name, libraries, templateName!!, thumb!!, widgets!!, recipe!!)
  }
}
