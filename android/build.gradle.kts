/*
 * Copyright (c) react-native-community
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import com.android.build.gradle.internal.tasks.factory.dependsOn
import groovy.json.JsonSlurper
import org.apache.tools.ant.taskdefs.condition.Os
import java.util.Properties

val reactNativeDir = findNodePackageDir("react-native")
val reactNativeManifest = file("${reactNativeDir}/package.json")
val reactNativeManifestAsJson = JsonSlurper().parseText(reactNativeManifest.readText()) as Map<*, *>
val reactNativeVersion = reactNativeManifestAsJson["version"] as String
val (major, minor, patch) = reactNativeVersion.split("-")[0].split(".")
val rnMinorVersion = minor.toInt()
val rnPatchVersion = patch.toInt()
val prefabHeadersDir = file("${layout.buildDirectory.get()}/prefab-headers")

if (findProperty("hermesEnabled") == "true") {
  throw GradleException("Please disable Hermes because Hermes will transform the JavaScript bundle as bytecode bundle.\n")
}

val reactProperties = Properties().apply {
  file("${reactNativeDir}/ReactAndroid/gradle.properties").inputStream().use { load(it) }
}

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "io.github.reactnativecommunity.javascriptcore"
  compileSdk = safeExtGet("compileSdkVersion", 35)

  if (rootProject.hasProperty("ndkPath")) {
    ndkPath = rootProject.extra["ndkPath"].toString()
  }
  if (rootProject.hasProperty("ndkVersion")) {
    ndkVersion = rootProject.extra["ndkVersion"].toString()
  }

  defaultConfig {
    minSdk = safeExtGet("minSdkVersion", 24)

    @Suppress("UnstableApiUsage")
    externalNativeBuild.cmake {
      arguments += listOf(
        "-DANDROID_STL=c++_shared",
        "-DREACT_NATIVE_DIR=${reactNativeDir.toPlatformString()}",
        "-DREACT_NATIVE_MINOR_VERSION=${rnMinorVersion}",
        "-DREACT_NATIVE_PATCH_VERSION=${rnPatchVersion}",
      )
      targets("jscruntimefactory")
      abiFilters(*reactNativeArchitectures().toTypedArray())
    }
  }

  externalNativeBuild {
    cmake {
      path("CMakeLists.txt")
    }
  }

  lint {
    abortOnError = false
    targetSdk = safeExtGet("targetSdkVersion", 35)
  }

  packaging {
    // Uncomment to keep debug symbols
    // doNotStrip.add("**/*.so")

    jniLibs {
      excludes.add("**/libc++_shared.so")
      excludes.add("**/libfbjni.so")
      excludes.add("**/libjsi.so")
      excludes.add("**/libreactnative.so")
      pickFirsts.add("**/libjscruntimefactory.so")
    }
  }

  buildFeatures {
    prefab = true
    prefabPublishing = true
  }

  prefab {
    register("jscruntimefactory") {
      headers = file(prefabHeadersDir).absolutePath
    }
  }
}

dependencies {
  implementation("com.facebook.yoga:proguard-annotations:1.19.0")
  compileOnly("com.facebook.fbjni:fbjni:0.7.0")
  implementation("com.facebook.react:react-android")

  //noinspection GradleDynamicVersion
  implementation("io.github.react-native-community:jsc-android:2026004.+")
}

val createPrefabHeadersDir by
  tasks.registering(Copy::class) {
    from("src/main/cpp")
    from("../common")
    include("*.h")
    into(prefabHeadersDir)
  }

tasks.named("preBuild").dependsOn(createPrefabHeadersDir)

private fun findNodePackageDir(packageName: String, absolute: Boolean = true): File {
  val nodeCommand = listOf("node", "--print", "require.resolve('${packageName}/package.json')")
  val proc = ProcessBuilder(nodeCommand)
    .directory(rootDir)
    .start()
  val error = proc.errorStream.bufferedReader().readText()
  if (error.isNotEmpty()) {
    val nodeCommandString = nodeCommand.joinToString(" ")
    throw GradleException(
      "findNodePackageDir() execution failed - nodeCommand[$nodeCommandString]\n$error"
    )
  }
  val dir = File(proc.inputStream.bufferedReader().readText().trim()).parentFile
  return if (absolute) dir.absoluteFile else dir
}

private fun File.toPlatformString(): String {
  var result = path.toString()
  if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    result = result.replace(File.separatorChar, '/')
  }
  return result
}

private fun reactNativeArchitectures(): List<String> {
  val value = findProperty("reactNativeArchitectures")
  return value?.toString()?.split(",") ?: listOf("armeabi-v7a", "x86", "x86_64", "arm64-v8a")
}

private fun <T> safeExtGet(prop: String, fallback: T): T {
  @Suppress("UNCHECKED_CAST")
  return rootProject.extra[prop] as? T ?: fallback
}
