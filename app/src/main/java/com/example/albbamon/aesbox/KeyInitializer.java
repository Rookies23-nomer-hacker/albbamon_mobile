package com.example.albbamon.aesbox;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
public class KeyInitializer {
    private static final String PREF_NAME = "secure_prefs";
    private static final String KEY_NAME = "aes_key";

    public static void initializeAESKeyIfNeeded(Context context) {
        try {
            // 마스터 키 생성
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // 암호화된 SharedPreferences 생성
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // 키가 없을 경우에만 저장 (최초 1회)
            if (!securePrefs.contains(KEY_NAME)) {
                String fixedKey = "albbamon-SECRET-Key-aes-albbamon"; // 고정된 키
                securePrefs.edit().putString(KEY_NAME, fixedKey).apply();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
