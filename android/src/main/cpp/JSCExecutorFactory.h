/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#pragma once

#include <fbjni/fbjni.h>
#include <jsireact/JSIExecutor.h>
#include <react/jni/JavaScriptExecutorHolder.h>
#include <react/jni/ReadableNativeMap.h>

#include "JSCRuntime.h"

namespace facebook::react {

// This is not like JSCJavaScriptExecutor, which calls JSC directly.  This uses
// JSIExecutor with JSCRuntime.
class JSCExecutorHolder
    : public jni::HybridClass<JSCExecutorHolder, JavaScriptExecutorHolder> {
public:
  static constexpr auto kJavaDescriptor =
      "Lio/github/reactnativecommunity/javascriptcore/JSCExecutor;";

  static jni::local_ref<jhybriddata> initHybrid(jni::alias_ref<jclass>,
                                                ReadableNativeMap *);

  static void registerNatives() {
    registerHybrid({
        makeNativeMethod("initHybrid", JSCExecutorHolder::initHybrid),
    });
  }

private:
  friend HybridBase;
  using HybridBase::HybridBase;
};

} // namespace facebook::react
