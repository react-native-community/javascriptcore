/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.github.reactnativecommunity.javascriptcore

import com.facebook.jni.HybridData
import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.react.bridge.JavaScriptExecutor
import com.facebook.react.bridge.ReadableNativeMap
import com.facebook.soloader.SoLoader

@DoNotStrip
internal class JSCExecutor(jscConfig: ReadableNativeMap) :
    JavaScriptExecutor(initHybrid(jscConfig)) {
  override fun getName(): String = "JSCExecutor"

  private companion object {
    init {
      loadLibrary()
    }

    @JvmStatic
    @Throws(UnsatisfiedLinkError::class)
    fun loadLibrary() {
      SoLoader.loadLibrary("jscruntimefactory")
    }

    @JvmStatic private external fun initHybrid(jscConfig: ReadableNativeMap): HybridData
  }
}
