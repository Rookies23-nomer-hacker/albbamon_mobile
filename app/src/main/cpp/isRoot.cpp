#include <jni.h>
#include <string>
#include <fstream>
#include <android/log.h>

#define LOG_TAG "rootcheck"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C" {

// Test-Keys 체크
JNIEXPORT jboolean JNICALL
Java_com_example_albbamon_autologin_Splash_CheckTestKey(JNIEnv *env, jobject thiz) {
    std::ifstream build_prop("/system/build.prop");
    std::string line;

    if (build_prop.is_open()) {
        while (std::getline(build_prop, line)) {
            if (line.find("test-keys") != std::string::npos) {
                LOGD("🔴 Test-Keys found in build.prop");
                return JNI_TRUE;
            }
        }
        build_prop.close();
    }
    return JNI_FALSE;
}

// Superuser.apk 체크
JNIEXPORT jboolean JNICALL
Java_com_example_albbamon_autologin_Splash_Superuser(JNIEnv *env, jobject thiz) {
    std::ifstream superuser_apk("/system/app/Superuser.apk");
    if (superuser_apk.good()) {
        LOGD("🔴 Superuser.apk found!");
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

// su, busybox 바이너리 체크
JNIEXPORT jboolean JNICALL
Java_com_example_albbamon_autologin_Splash_checkPaths(JNIEnv *env, jobject thiz) {
    const char *paths[] = {
            "/system/bin/su",
            "/system/xbin/su",
            "/system/sd/xbin/su",
            "/system/bin/busybox",
            "/system/xbin/busybox",
            "/sbin/su",
            "/vendor/bin/su",
            "/system/usr/we-need-root/su-backup"
    };

    for (const char *path: paths) {
        std::ifstream file(path);
        if (file.good()) {
            LOGD("🔴 Root binary found: %s", path);
            return JNI_TRUE;
        }
    }
    return JNI_FALSE;
}

}
