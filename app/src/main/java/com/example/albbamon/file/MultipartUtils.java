package com.example.albbamon.file;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartUtils {
    public static MultipartBody.Part fileToMultipart(File file, String partName) {
        RequestBody requestFile = RequestBody.Companion.create(file, MediaType.parse("image/jpeg"));
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
