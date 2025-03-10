/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#ifdef __cplusplus

#import <cxxreact/MessageQueueThread.h>
#import <jsi/jsi.h>
#import <react/runtime/JSRuntimeFactory.h>

namespace facebook {
namespace react {

class RCTJscInstance : public JSRuntimeFactory {
 public:
  RCTJscInstance();

  std::unique_ptr<JSRuntime> createJSRuntime(
      std::shared_ptr<MessageQueueThread> msgQueueThread) noexcept override;

  ~RCTJscInstance(){};
};
} // namespace react
} // namespace facebook

#endif
