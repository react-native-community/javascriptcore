/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.jscexample

import android.content.Context
import com.facebook.react.ReactHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JSBundleLoader
import com.facebook.react.bridge.ReactContext
import com.facebook.react.common.annotations.UnstableReactNativeAPI
import com.facebook.react.common.build.ReactBuildConfig
import com.facebook.react.defaults.DefaultComponentsRegistry
import com.facebook.react.defaults.DefaultReactHostDelegate
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate
import com.facebook.react.fabric.ComponentFactory
import com.facebook.react.runtime.ReactHostImpl
import com.facebook.react.runtime.cxxreactpackage.CxxReactPackage
import io.github.reactnativecommunity.javascriptcore.JSCRuntimeFactory

/**
 * A utility class that allows you to simplify the setup of a [ReactHost] for new apps in Open
 * Source.
 *
 * [ReactHost] is an interface responsible of handling the lifecycle of a React Native app when
 * running in bridgeless mode.
 */
internal object JSCReactHost {
  private var reactHost: ReactHost? = null

  /**
   * Util function to create a default [ReactHost] to be used in your application. This method is
   * used by the New App template.
   *
   * @param context the Android [Context] to use for creating the [ReactHost]
   * @param packageList the list of [ReactPackage]s to use for creating the [ReactHost]
   * @param jsMainModulePath the path to your app's main module on Metro. Usually `index` or
   *   `index.<platform>`
   * @param jsBundleAssetPath the path to the JS bundle relative to the assets directory. Will be
   *   composed in a `asset://...` URL
   * @param isHermesEnabled whether to use Hermes as the JS engine, default to true.
   * @param useDevSupport whether to enable dev support, default to ReactBuildConfig.DEBUG.
   * @param cxxReactPackageProviders a list of cxxreactpackage providers (to register c++ turbo
   *   modules)
   * @param jsBundleLoader a [JSBundleLoader] to use for creating the [ReactHost]
   * @param exceptionHandler Callback that can be used by React Native host applications to react to
   *   exceptions thrown by the internals of React Native.
   *
   * TODO(T186951312): Should this be @UnstableReactNativeAPI?
   */
  @OptIn(UnstableReactNativeAPI::class)
  fun getJSCReactHost(
    context: Context,
    packageList: List<ReactPackage>,
    jsMainModulePath: String = "index",
    jsBundleAssetPath: String = "index",
    jsBundleFilePath: String? = null,
    useDevSupport: Boolean = ReactBuildConfig.DEBUG,
    cxxReactPackageProviders: List<(ReactContext) -> CxxReactPackage> = emptyList(),
    exceptionHandler: (Exception) -> Unit = { throw it },
  ): ReactHost {
    if (reactHost == null) {

      val bundleLoader =
        if (jsBundleFilePath != null) {
          if (jsBundleFilePath.startsWith("assets://")) {
            JSBundleLoader.createAssetLoader(context, jsBundleFilePath, true)
          } else {
            JSBundleLoader.createFileLoader(jsBundleFilePath)
          }
        } else {
          JSBundleLoader.createAssetLoader(context, "assets://$jsBundleAssetPath", true)
        }
      val jsRuntimeFactory = JSCRuntimeFactory()
      val defaultTmmDelegateBuilder = DefaultTurboModuleManagerDelegate.Builder()
      cxxReactPackageProviders.forEach { defaultTmmDelegateBuilder.addCxxReactPackage(it) }
      val defaultReactHostDelegate =
        DefaultReactHostDelegate(
          jsMainModulePath = jsMainModulePath,
          jsBundleLoader = bundleLoader,
          reactPackages = packageList,
          jsRuntimeFactory = jsRuntimeFactory,
          turboModuleManagerDelegateBuilder = defaultTmmDelegateBuilder,
          exceptionHandler = exceptionHandler)
      val componentFactory = ComponentFactory()
      DefaultComponentsRegistry.register(componentFactory)
      // TODO: T164788699 find alternative of accessing ReactHostImpl for initialising reactHost
      reactHost =
        ReactHostImpl(
          context,
          defaultReactHostDelegate,
          componentFactory,
          true /* allowPackagerServerAccess */,
          useDevSupport,
        )
    }
    return reactHost as ReactHost
  }
}
