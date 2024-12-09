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

package com.itsaky.androidide.utils

object KotlinClassBuilder {

  @JvmStatic
  fun createClass(packageName: String, className: String): String {
    return """
    package $packageName
    
    class $className {}
    """
      .trimIndent()
  }

  @JvmStatic
  fun createInterface(packageName: String, className: String): String {
    return """
    package $packageName
    
    interface $className {}
    """
      .trimIndent()
  }

  @JvmStatic
  fun createEnum(packageName: String, className: String): String {
    return """
    package $packageName
    
    enum class $className {}
    """
      .trimIndent()
  }

  @JvmStatic
  fun createActivity(packageName: String, className: String): String {
    return """
    package $packageName
    
    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    
    class $className : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      }
    }
    """
      .trimIndent()
  }
}
