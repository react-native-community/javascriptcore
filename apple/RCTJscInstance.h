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

  void setEnableDebugger(bool enableDebugger);

  void setDebuggerName(const std::string &debuggerName);

  std::unique_ptr<JSRuntime> createJSRuntime(
      std::shared_ptr<MessageQueueThread> msgQueueThread) noexcept override;

  ~RCTJscInstance(){};

  private:
#if DEBUG
  bool enableDebugger_ = true;
#else
  bool enableDebugger_ = false;
#endif
  std::string debuggerName_ = "JSC React Native";
};
} // namespace react
} // namespace facebook

#endif
