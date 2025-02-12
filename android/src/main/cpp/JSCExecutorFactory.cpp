/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#include "JSCExecutorFactory.h"

#include <fbjni/fbjni.h>
#include <react/jni/JReactMarker.h>
#include <react/jni/JSLogging.h>

namespace facebook::react {

namespace {

class JSCExecutorFactory : public JSExecutorFactory {
public:
  std::unique_ptr<JSExecutor>
  createJSExecutor(std::shared_ptr<ExecutorDelegate> delegate,
                   std::shared_ptr<MessageQueueThread> jsQueue) override {
    auto installBindings = [](jsi::Runtime &runtime) {
      react::Logger androidLogger =
          static_cast<void (*)(const std::string &, unsigned int)>(
              &reactAndroidLoggingHook);
      react::bindNativeLogger(runtime, androidLogger);
    };
    return std::make_unique<JSIExecutor>(jsc::makeJSCRuntime(), delegate,
                                         JSIExecutor::defaultTimeoutInvoker,
                                         installBindings);
  }
};

} // namespace

// static
jni::local_ref<JSCExecutorHolder::jhybriddata>
JSCExecutorHolder::initHybrid(jni::alias_ref<jclass>, ReadableNativeMap *) {
  // This is kind of a weird place for stuff, but there's no other
  // good place for initialization which is specific to JSC on
  // Android.
  JReactMarker::setLogPerfMarkerIfNeeded();
  // TODO mhorowitz T28461666 fill in some missing nice to have glue
  return makeCxxInstance(std::make_unique<JSCExecutorFactory>());
}

} // namespace facebook::react
