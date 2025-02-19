#
# Copyright (c) react-native-community
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.
#

cmake_minimum_required(VERSION 3.13)
project(jscruntimefactory)

set(CMAKE_VERBOSE_MAKEFILE ON)
set (CMAKE_CXX_STANDARD 20)

string(APPEND CMAKE_CXX_FLAGS
  " -fexceptions"
  " -frtti"
  " -O3"
  " -Wno-unused-lambda-capture"
  " -DREACT_NATIVE_MINOR_VERSION=${REACT_NATIVE_MINOR_VERSION}"
  " -DREACT_NATIVE_PATCH_VERSION=${REACT_NATIVE_PATCH_VERSION}"
)

include("${REACT_NATIVE_DIR}/ReactAndroid/cmake-utils/folly-flags.cmake")
add_compile_options(${folly_FLAGS})

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON CACHE INTERNAL "")

set(PACKAGE_NAME "jscruntimefactory")
set(SRC_DIR "${CMAKE_SOURCE_DIR}/src/main/cpp")

set(COMMON_DIR "${CMAKE_SOURCE_DIR}/../common")
file(GLOB COMMON_SOURCES  "${COMMON_DIR}/*.cpp")

add_library(
  ${PACKAGE_NAME}
  SHARED
  ${COMMON_SOURCES}
  "${SRC_DIR}/JSCExecutorFactory.cpp"
  "${SRC_DIR}/OnLoad.cpp"
)

# includes

target_include_directories(
  ${PACKAGE_NAME}
  PRIVATE
  "${COMMON_DIR}"
  "${REACT_NATIVE_DIR}/ReactCommon"
  "${REACT_NATIVE_DIR}/ReactCommon/jsiexecutor"
  "${REACT_NATIVE_DIR}/ReactCommon/jsitooling"
  "${REACT_NATIVE_DIR}/ReactAndroid/src/main/jni"
)

# find libraries

find_library(
  LOG_LIB
  log
)
find_package(fbjni REQUIRED CONFIG)
find_package(jsc-android REQUIRED CONFIG)
find_package(ReactAndroid REQUIRED CONFIG)

# link to shared libraries

target_link_libraries(
  ${PACKAGE_NAME}
  ${LOG_LIB}
  android
  fbjni::fbjni
  jsc-android::jsc
  ReactAndroid::jsi
  ReactAndroid::reactnative
)
