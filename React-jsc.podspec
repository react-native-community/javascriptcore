# Copyright (c) Meta Platforms, Inc. and affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))
version = package['version']

Pod::Spec.new do |s|
  s.name                   = "React-jsc"
  s.version                = version
  s.summary                = "JavaScriptCore engine for React Native"
  s.homepage               = "https://github.com/react-native-community/javascriptcore"
  s.license                = package["license"]
  s.author                 = "Meta Platforms, Inc. and its affiliates"
  s.platforms              = { :ios => "15.1", :tvos => "15.1", :visionos => "1.0", :osx => "10.15" }
  s.source                 = { :git => "https://github.com/react-native-community/javascriptcore", :tag => "#{s.version}" }
  s.source_files           = "common/*.{cpp,h}", "ios/*.{mm,h}"
  s.weak_framework         = "JavaScriptCore"

  # TODO: Figure this out. Probably we need a copy of JSI
  # s.dependency "React-jsi", version

  s.subspec "Fabric" do |ss|
    ss.pod_target_xcconfig  = { "OTHER_CFLAGS" => "$(inherited)" }
  end
end
