package com.example.albbamon.file;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static File bitmapToFile(Context context, Bitmap bitmap) {
        // 현재 시간 + 랜덤 숫자로 파일명 생성
        String fileName = System.currentTimeMillis() + "_" + (int)(Math.random() * 100000) + ".jpg";

        File file = new File(context.getCacheDir(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
