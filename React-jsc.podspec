# Copyright (c) Meta Platforms, Inc. and affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))
version = package['version']

folly_config = get_folly_config()
folly_compiler_flags = folly_config[:compiler_flags]
folly_version = folly_config[:version]
boost_config = get_boost_config()
boost_compiler_flags = boost_config[:compiler_flags]

Pod::Spec.new do |s|
  s.name                   = "React-jsc"
  s.version                = version
  s.summary                = "JavaScriptCore engine for React Native"
  s.homepage               = "https://github.com/react-native-community/javascriptcore"
  s.license                = package["license"]
  s.author                 = "Meta Platforms, Inc. and its affiliates"
  s.platforms              = { :ios => "15.1", :tvos => "15.1", :visionos => "1.0", :osx => "10.15" }
  s.source                 = { :git => "https://github.com/react-native-community/javascriptcore.git", :tag => "#{s.version}" }
  s.source_files           = "common/*.{cpp,h}", "ios/*.{mm,h}"
  s.compiler_flags = folly_compiler_flags + ' ' + boost_compiler_flags
  s.weak_framework         = "JavaScriptCore"

  s.dependency "RCT-Folly", folly_version
  s.dependency "DoubleConversion"
  # s.dependency "React-jsitooling" Uncomment once merged in React Native
  s.dependency "React-cxxreact"
  s.dependency "React-jsi"
  s.dependency "React-jsiexecutor"
end
