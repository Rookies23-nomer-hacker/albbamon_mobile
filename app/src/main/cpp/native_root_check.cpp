#include <jni.h>
#include <string>
#include <unistd.h>
#include <sys/stat.h>
#include <android/log.h>

#define LOG_TAG "RootCheck"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// 루트 파일 목록
const char* rootFiles[] = {
        "/system/bin/su",
        "/system/xbin/su",
        "/system/app/Superuser.apk",
        "/data/local/bin/su",
        "/data/local/xbin/su",
        "/sbin/su",
        "/system/sd/xbin/su",
        "/system/bin/.ext/su",
        "/system/usr/we-need-root/su"
};

// 특정 파일이 존재하는지 확인
bool fileExists(const char* path) {
    struct stat fileStat;
    return (stat(path, &fileStat) == 0);
}

// 루팅 여부 확인
extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_albbamon_autologin_RootChecker_isDeviceRooted(JNIEnv* env, jobject thiz) {
    for (const char* path : rootFiles) {
        if (fileExists(path)) {
            LOGE("루팅 탐지됨: %s", path);
            return JNI_TRUE;
        }
    }

    // su 바이너리 실행 가능 여부 확인
    if (access("/system/bin/su", F_OK) == 0 || access("/system/xbin/su", F_OK) == 0) {
        LOGE("su 바이너리 발견");
        return JNI_TRUE;
    }

    return JNI_FALSE;
}
