package com.mohnage7.marvelapp.network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MarvelInterceptor implements Interceptor {

    private static final String API_KEY = "apikey";
    private static final String TIME_STAMP = "ts";
    private static final String HASH = "hash";

    private String publicKey;
    private String privateKey;

    public MarvelInterceptor(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        long timeStamp = System.currentTimeMillis();
        String hash = generateHashForMarvelServer(privateKey, publicKey, timeStamp);
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter(TIME_STAMP, String.valueOf(timeStamp))
                .addQueryParameter(API_KEY, publicKey)
                .addQueryParameter(HASH, hash)
                .build();

        Request.Builder requestBuilder = original.newBuilder().url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    /**
     * this method returns
     *
     * @param timeStamp current time in milli seconds
     * @return special hash key to access marvel services.
     */
    private String generateHashForMarvelServer(String privateKey, String publicKey, long timeStamp) {
        String hash = timeStamp + privateKey + publicKey;
        return md5(hash);
    }

    private String md5(final String hash) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(hash.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder stringBuilder = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (stringBuilder.length() < 2) {
                    stringBuilder.insert(0, "0");
                }
                hexString.append(stringBuilder);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("Exception", e.getLocalizedMessage());
        }
        return "";
    }
}
