package com.example.albbamon.aesbox;

import android.util.Log;

import java.nio.charset.StandardCharsets;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class aesUtil {
    private SecretKeySpec secretKey;

    public aesUtil(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("Key must be 32 bytes (256 bits)");
        }
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    // 암호화 (문자열 입력 → byte[] 출력)
    public byte[] encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
    }

    // 복호화 (byte[] 입력 → 문자열 출력)
    public String decrypt(byte[] ciphertext) {
        try {
            if (ciphertext == null || ciphertext.length == 0) {
                throw new IllegalArgumentException("복호화 대상이 null이거나 비어 있음");
            }

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(ciphertext);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch(BadPaddingException e){
            Log.e("AES_ERROR", "❌ BadPaddingException", e);
            return null;
        }
        catch (IllegalBlockSizeException e) {
            Log.e("AES_ERROR", "❌ IllegalBlockSizeException", e);
            return null;
        }
        catch (Exception e) {
            Log.e("AES_ERROR", "❌ 예외 발생", e);
            return null;
        }
    }

}
