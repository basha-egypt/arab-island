package com.touch.mobile.dark;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import com.wardrumstudios.utils.WarMedia;

import com.touch.mobile.dark.gui.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MainGTA extends WarMedia {
    public static MainGTA self = null;
    static String vmVersion;
    private boolean once = false;

    private static void writeDebugLog(String msg) {
        try {
            File dir = new File("/storage/emulated/0/Android/data/com.arabisland.launcher/files");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File logFile = new File(dir, "crash_debug.txt");
            FileOutputStream fos = new FileOutputStream(logFile, true);
            fos.write((msg + "\n").getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Throwable ignored) {
        }
    }

    static {
        vmVersion = null;
        writeDebugLog("=== MainGTA static init ===");
        System.out.println("**** Loading SO's");
        try {
            vmVersion = System.getProperty("java.vm.version");
            System.out.println("vmVersion " + vmVersion);
            writeDebugLog("vmVersion: " + vmVersion);
            System.loadLibrary("ImmEmulatorJ");
            writeDebugLog("Loaded ImmEmulatorJ");
        } catch (Throwable t) {
            writeDebugLog("ImmEmulatorJ err: " + t);
        }

        try {
            writeDebugLog("Before loadLibrary GTASA");
            System.loadLibrary("GTASA");
            writeDebugLog("After loadLibrary GTASA");
        } catch (Throwable t) {
            writeDebugLog("GTASA load err: " + t);
            throw t;
        }

        try {
            writeDebugLog("Before loadLibrary samp");
            System.loadLibrary("samp");
            writeDebugLog("After loadLibrary samp");
        } catch (Throwable t) {
            writeDebugLog("samp load err: " + t);
            throw t;
        }
    }

    public boolean ServiceAppCommand(String str, String str2) {
        return false;
    }
    public int ServiceAppCommandValue(String str, String str2) {
        return 0;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
        writeDebugLog("MainGTA onCreate start");
        if(!once) {
            once = true;
        }

        System.out.println("MainGTA onCreate");
        self = this;
        wantsMultitouch = true;
        wantsAccelerometer = true;
        super.onCreate(bundle);
        Utils.currentContext = this;
        writeDebugLog("MainGTA onCreate end");
    }

    public void onDestroy() {
        writeDebugLog("MainGTA onDestroy");
        System.out.println("MainGTA onDestroy");
        super.onDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public void onPause() {
        writeDebugLog("MainGTA onPause");
        System.out.println("MainGTA onPause");
        super.onPause();
    }

    public void onRestart() {
        writeDebugLog("MainGTA onRestart");
        System.out.println("MainGTA onRestart");
        super.onRestart();
    }

    public void onResume() {
        writeDebugLog("MainGTA onResume");
        System.out.println("MainGTA onResume");
        super.onResume();
    }

    public void onStart() {
        writeDebugLog("MainGTA onStart");
        System.out.println("MainGTA onStart");
        super.onStart();
    }

    public void onStop() {
        writeDebugLog("MainGTA onStop");
        System.out.println("MainGTA onStop");
        super.onStop();
    }
}
