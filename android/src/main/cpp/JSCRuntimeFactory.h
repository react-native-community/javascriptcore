/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#pragma once

#include <fbjni/fbjni.h>
#include <jsi/jsi.h>
#include <react/runtime/JSRuntimeFactory.h>
#include <react/runtime/jni/JJSRuntimeFactory.h>

#include "JSCRuntime.h"

namespace facebook::react {

class JSCRuntimeFactory
    : public jni::HybridClass<JSCRuntimeFactory, JJSRuntimeFactory> {
public:
  static constexpr auto kJavaDescriptor =
      "Lio/github/reactnativecommunity/javascriptcore/JSCRuntimeFactory;";

  static jni::local_ref<jhybriddata> initHybrid(jni::alias_ref<jhybridobject>) {
    return makeCxxInstance();
  }

  static void registerNatives() {
    registerHybrid({
        makeNativeMethod("initHybrid", JSCRuntimeFactory::initHybrid),
    });
  }

  std::unique_ptr<JSRuntime>
  createJSRuntime(std::shared_ptr<MessageQueueThread> msgQueueThread) noexcept {
    return std::make_unique<JSIRuntimeHolder>(jsc::makeJSCRuntime());
  }

private:
  friend HybridBase;
  using HybridBase::HybridBase;
};

} // namespace facebook::react
