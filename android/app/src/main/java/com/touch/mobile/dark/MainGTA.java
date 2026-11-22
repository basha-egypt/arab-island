package com.touch.mobile.dark;

import android.content.Intent;
import android.os.Bundle;

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
            File dir = new File("/storage/emulated/0/Android/data/com.touch.mobile.dark/files");
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
        } catch (Throwable t) {
            writeDebugLog("vmVersion err: " + t);
        }

        String[] libs = {"ImmEmulatorJ", "SCAnd", "bass", "GTASA", "samp"};
        for (String lib : libs) {
            try {
                writeDebugLog("Before loadLibrary " + lib);
                System.loadLibrary(lib);
                writeDebugLog("After loadLibrary " + lib);
            } catch (Throwable t) {
                writeDebugLog("Load err " + lib + ": " + t);
                if ("GTASA".equals(lib) || "samp".equals(lib)) {
                    throw t;
                }
            }
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

    @Override
    public void onCreate(Bundle bundle) {
        writeDebugLog("MainGTA onCreate start");
        self = this;
        try {
            System.out.println("MainGTA onCreate");
            super.onCreate(bundle);
        } catch (Throwable t) {
            writeDebugLog("MainGTA super.onCreate err: " + t);
            throw t;
        }
        writeDebugLog("MainGTA onCreate end");
    }

    @Override
    public void onDestroy() {
        writeDebugLog("MainGTA onDestroy");
        System.out.println("MainGTA onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        writeDebugLog("MainGTA onPause");
        System.out.println("MainGTA onPause");
        super.onPause();
    }

    @Override
    public void onRestart() {
        writeDebugLog("MainGTA onRestart");
        System.out.println("MainGTA onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        writeDebugLog("MainGTA onResume");
        System.out.println("MainGTA onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        writeDebugLog("MainGTA onStart");
        System.out.println("MainGTA onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        writeDebugLog("MainGTA onStop");
        System.out.println("MainGTA onStop");
        super.onStop();
    }
}
