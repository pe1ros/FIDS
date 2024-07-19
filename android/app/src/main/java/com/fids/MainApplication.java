package com.fids;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.soloader.SoLoader;

import java.lang.reflect.Field;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.JSBundleLoader;

public class MainApplication extends Application implements ReactApplication {
    private static final String BUNDLE_URL = "http://10.0.2.2:3000/get_bundle";
    private static final String BUNDLE_FILE_NAME = "index.bundle";

    private final ReactNativeHost mReactNativeHost = new DefaultReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }

        @Override
        protected boolean isNewArchEnabled() {
          return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
        }

        @Override
        protected Boolean isHermesEnabled() {
          return BuildConfig.IS_HERMES_ENABLED;
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      DefaultNewArchitectureEntryPoint.load();
    }
    ReactNativeFlipper.initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
    Executors.newSingleThreadExecutor().execute(new Runnable() {
        @Override
        public void run() {
            downloadBundle();
        }
    });
}

    private void downloadBundle() {
        Log.d("MainApplication", "Starting bundle download");
        try {
            URL url = new URL(BUNDLE_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("MainApplication", "Server returned HTTP " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
                return;
            }

            File bundleFile = new File(getFilesDir(), BUNDLE_FILE_NAME);
            FileOutputStream fileOutput = new FileOutputStream(bundleFile);
            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }

            fileOutput.close();
            inputStream.close();
            Log.d("MainApplication", "Bundle download completed");
            ReactInstanceManager instanceManager = getReactNativeHost().getReactInstanceManager();
            setJSBundle(instanceManager, bundleFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("MainApplication", "Error downloading bundle", e);
        }
    }

    private void setJSBundle(ReactInstanceManager instanceManager, String latestJSBundleFile) {
        try {
            JSBundleLoader latestJSBundleLoader;
            latestJSBundleLoader = JSBundleLoader.createFileLoader(latestJSBundleFile);
            Field bundleLoaderField = instanceManager.getClass().getDeclaredField("mBundleLoader");
            bundleLoaderField.setAccessible(true);
            bundleLoaderField.set(instanceManager, latestJSBundleLoader);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d("MainApplication", "Recreating React context");
//                    instanceManager.onHostDestroy();
                    instanceManager.recreateReactContextInBackground();
                }
            });
        } catch (Exception e) {
            Log.e("MainApplication", "Could not set JS Bundle", e);
        }
    }
}
