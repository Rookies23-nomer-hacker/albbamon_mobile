package com.example.albbamon.autologin;

public class RootChecker {
    static {
        System.loadLibrary("native-lib");
    }

    public native boolean isDeviceRooted();
}
