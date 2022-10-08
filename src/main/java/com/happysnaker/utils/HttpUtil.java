package com.happysnaker.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class HttpUtil {
    public static Response sendRequestByFormData(String url, HashMap formData) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set set = formData.keySet();
        set.forEach((key) -> builder.addFormDataPart((String) key, (String) formData.get(key)));
        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
