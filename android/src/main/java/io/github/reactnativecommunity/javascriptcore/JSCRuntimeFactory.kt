/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.github.reactnativecommunity.javascriptcore

import com.facebook.jni.HybridData
import com.facebook.jni.annotations.DoNotStrip
import com.facebook.jni.annotations.DoNotStripAny
import com.facebook.react.runtime.JSRuntimeFactory
import com.facebook.soloader.SoLoader

@Suppress("Unused")
@DoNotStripAny
class JSCRuntimeFactory : JSRuntimeFactory(initHybrid()) {
  private companion object {
    init {
      SoLoader.loadLibrary("jscruntimefactory")
    }

    @DoNotStrip @JvmStatic private external fun initHybrid(): HybridData
  }
}
