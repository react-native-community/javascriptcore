# React Native JavaScriptCore

Opt-in to use JavaScriptCore runtime in React Native.

_Note: JavaScriptCore has been extracted from core react-native as a part of [Lean Core JSC RFC](https://github.com/react-native-community/discussions-and-proposals/pull/836)._

## Installation

> [!IMPORTANT]  
> This library only supports React Native 0.79 and above with new architecture enabled.

```sh
yarn add @react-native-community/javascriptcore
```

### iOS

Install pods:

```sh
cd ios && USE_THIRD_PARTY_JSC=1 USE_HERMES=0 bundle exec pod install
```
> [!NOTE]  
> Note: `USE_THIRD_PARTY_JSC=1` is required to use JavaScriptCore from this package until JavaScriptCore is removed from core react-native.

### Android

Add the following to your `android/gradle.properties`:

```properties
# Disable Hermes
hermesEnabled=false

# Enable third-party JSC
useThirdPartyJSC=true
```

## Usage

### iOS

Open AppDelegate.swift and overwrite `createJSRuntimeFactory` method:

```diff
import React
import React_RCTAppDelegate
import ReactAppDependencyProvider
import UIKit
+import ReactJSC

// AppDelegate code

class ReactNativeDelegate: RCTDefaultReactNativeFactoryDelegate {
  override func sourceURL(for bridge: RCTBridge) -> URL? {
    self.bundleURL()
  }

  override func bundleURL() -> URL? {
    #if DEBUG
    RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: "index")
    #else
    Bundle.main.url(forResource: "main", withExtension: "jsbundle")
    #endif
  }

+  override func createJSRuntimeFactory() -> JSRuntimeFactory {
+    jsrt_create_jsc_factory() // Use JavaScriptCore runtime
+  }
}
```

### Android

Open `MainApplication.java` and overwrite `getJavaScriptExecutorFactory` method:

```diff
+import io.github.reactnativecommunity.javascriptcore.JSCExecutorFactory
+import io.github.reactnativecommunity.javascriptcore.JSCRuntimeFactory

class MainApplication : Application(), ReactApplication {

  override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
            PackageList(this).packages.apply {
              // Packages that cannot be autolinked yet can be added manually here, for example:
              // add(MyReactNativePackage())
            }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED

+        override fun getJavaScriptExecutorFactory(): JavaScriptExecutorFactory =
+          JSCExecutorFactory(packageName, AndroidInfoHelpers.getFriendlyDeviceName())
      }

+  override val reactHost: ReactHost
+    get() = getDefaultReactHost(applicationContext, reactNativeHost, JSCRuntimeFactory())

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, OpenSourceMergedSoMapping)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }
  }
}
```

## Maintainers

- [Callstack](https://callstack.com/) – A Total Software Engineering Consultancy that transforms organizations and teams through transformative apps.
- [Expo](https://expo.dev/) - An open-source platform for making universal native apps for Android, iOS, and the web with JavaScript and React.

### Special Thanks

Special thanks to the team who worked on the initial extraction of JavaScriptCore from core react-native:

- [Riccardo Cipolleschi](https://github.com/cipolleschi)
- [Nicola Corti](https://github.com/cortinico)
- [Kudo Chien](https://github.com/Kudo)
- [Oskar Kwaśniewski](https://github.com/okwasniewski)
- [jsc-android](https://github.com/react-native-community/jsc-android-buildscripts) - This project uses **jsc-android** under the hood, which is supported by [Expo](https://expo.dev), [Software Mansion](https://swmansion.com/), and [Wix](https://www.wix.engineering/).

## License

Everything inside this repository is [MIT licensed](./LICENSE).


