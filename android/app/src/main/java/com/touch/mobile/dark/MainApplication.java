package com.touch.mobile.dark;

import android.app.Application;
import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.os.Build;
import android.system.Os;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.soloader.SoLoader;
import com.touch.mobile.dark.newarchitecture.MainApplicationReactNativeHost;
import com.touch.mobile.dark.modules.GtaSetupModulePackage;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
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
          packages.add(new GtaSetupModulePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  private final ReactNativeHost mNewArchitectureNativeHost =
      new MainApplicationReactNativeHost(this);

  @Override
  public ReactNativeHost getReactNativeHost() {
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      return mNewArchitectureNativeHost;
    } else {
      return mReactNativeHost;
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    prepareNativeLibLink();
    saveLastNativeCrash();
    dumpFilesTree();
    // If you opted-in for the New Architecture, we enable the TurboModule system
    ReactFeatureFlags.useTurboModules = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }


  /**
   * libsamp.so hardcodes /data/data/com.touch.mobile.dark/lib/libbass.so.
   * On newer Android that path may not exist, so link it to the real native lib dir.
   */
  private void prepareNativeLibLink() {
    try {
      File link = new File(getApplicationInfo().dataDir, "lib");
      if (!link.exists()) {
        Os.symlink(getApplicationInfo().nativeLibraryDir, link.getAbsolutePath());
      }
    } catch (Throwable ignored) {
    }
  }

  /** Debug: writes files/tree.txt = device info + every game file with its size. */
  private void dumpFilesTree() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          File root = getExternalFilesDir(null);
          if (root == null) return;
          StringBuilder sb = new StringBuilder();
          sb.append("device: ").append(Build.MANUFACTURER).append(' ').append(Build.MODEL).append('\n');
          sb.append("hardware: ").append(Build.HARDWARE).append(" board: ").append(Build.BOARD).append('\n');
          sb.append("android sdk: ").append(Build.VERSION.SDK_INT).append('\n');
          sb.append("abis: ").append(Arrays.toString(Build.SUPPORTED_ABIS)).append('\n');
          sb.append("nativeLibDir: ").append(getApplicationInfo().nativeLibraryDir).append('\n');
          File bass = new File("/data/data/" + getPackageName() + "/lib/libbass.so");
          sb.append("libbass link ok: ").append(bass.exists()).append('\n');
          sb.append("root: ").append(root.getAbsolutePath()).append("\n\n");
          walkTree(root, root.getAbsolutePath().length() + 1, sb, new int[] {0});
          try (FileOutputStream out = new FileOutputStream(new File(root, "tree.txt"))) {
            out.write(sb.toString().getBytes("UTF-8"));
          }
        } catch (Throwable ignored) {
        }
      }
    }).start();
  }

  private static void walkTree(File dir, int cut, StringBuilder sb, int[] count) {
    File[] list = dir.listFiles();
    if (list == null) return;
    Arrays.sort(list);
    for (File f : list) {
      if (count[0] > 6000) return;
      if (f.isDirectory()) {
        walkTree(f, cut, sb, count);
      } else {
        count[0]++;
        sb.append(f.getAbsolutePath().substring(cut)).append("  ").append(f.length()).append('\n');
      }
    }
  }

  /** Android 11+: saves the last native crash report to files/tombstone.txt for debugging. */
  private void saveLastNativeCrash() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return;
    try {
      ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
      for (ApplicationExitInfo info : am.getHistoricalProcessExitReasons(null, 0, 3)) {
        if (info.getReason() != ApplicationExitInfo.REASON_CRASH_NATIVE) continue;
        InputStream in = info.getTraceInputStream();
        if (in == null) continue;
        File dir = getExternalFilesDir(null);
        if (dir == null) continue;
        try (FileOutputStream out = new FileOutputStream(new File(dir, "tombstone.txt"))) {
          byte[] b = new byte[8192];
          int n;
          while ((n = in.read(b)) > 0) out.write(b, 0, n);
        }
        in.close();
        break;
      }
    } catch (Throwable ignored) {
    }
  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.touch.mobile.dark.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
