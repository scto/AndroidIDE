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

package com.itsaky.androidide.lsp.java.providers.completion

val TOP_LEVEL_KEYWORDS =
  arrayOf(
    "package",
    "import",
    "public",
    "private",
    "protected",
    "abstract",
    "class",
    "interface",
    "@interface",
    "extends",
    "implements",
  )
val PRIMITIVE_TYPE_KEYWORDS =
  arrayOf("byte", "short", "int", "long", "float", "double", "boolean", "char")
val CLASS_BODY_KEYWORDS =
  arrayOf(
    *PRIMITIVE_TYPE_KEYWORDS,
    "public",
    "private",
    "protected",
    "static",
    "final",
    "native",
    "synchronized",
    "abstract",
    "default",
    "class",
    "interface",
    "void",
    "true",
    "false",
    "null",
  )
val METHOD_BODY_KEYWORDS =
  arrayOf(
    *PRIMITIVE_TYPE_KEYWORDS,
    "new",
    "assert",
    "try",
    "catch",
    "finally",
    "throw",
    "return",
    "break",
    "case",
    "continue",
    "default",
    "do",
    "while",
    "for",
    "switch",
    "if",
    "else",
    "instanceof",
    "var",
    "final",
    "class",
    "void",
    "synchronized",
    "true",
    "false",
    "null",
  )
