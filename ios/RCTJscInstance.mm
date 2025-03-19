/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "RCTJscInstance.h"
#include "JSCRuntime.h"

namespace facebook {
namespace react {

RCTJscInstance::RCTJscInstance() {}

void RCTJscInstance::setEnableDebugger(bool enableDebugger) {
  enableDebugger_ = enableDebugger;
}

void RCTJscInstance::setDebuggerName(const std::string &debuggerName) {
  debuggerName_ = debuggerName;
}

std::unique_ptr<JSRuntime> RCTJscInstance::createJSRuntime(std::shared_ptr<MessageQueueThread> msgQueueThread) noexcept
{
  jsc::RuntimeConfig rc = {
    .enableDebugger = enableDebugger_,
    .debuggerName = debuggerName_,
  };
  return std::make_unique<JSIRuntimeHolder>(jsc::makeJSCRuntime(std::move(rc)));
}

} // namespace react
} // namespace facebook
