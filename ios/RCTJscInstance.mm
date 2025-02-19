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

std::unique_ptr<JSRuntime> RCTJscInstance::createJSRuntime(std::shared_ptr<MessageQueueThread> msgQueueThread) noexcept
{
  return std::make_unique<JSIRuntimeHolder>(jsc::makeJSCRuntime());
}

} // namespace react
} // namespace facebook
