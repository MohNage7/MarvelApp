package com.mohnage7.marvelapp.utils;

public class Constants {
    public static final String BASE_URL = "https://gateway.marvel.com/v1/public/";
    public static final String PRIVATE_KEY = "0f1d0fdf46a0bf32f962b0b9997233c0395cdf8e";
    public static final String PUBLIC_KEY = "6a7ed890b4b941a925202a5630d5b162";
    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 60; //  seconds
    public static final int WRITE_TIMEOUT = 60; // seconds
    public static final int CACHE_TIMEOUT = 2; //  minutes
    //Limit the result set to the specified number of resources.
    public static final int LIMIT= 20;
    //Skipped number of resources in the result set of characters API.
    public static final int DEFAULT_OFFSET=0;


    private Constants() {
        // restrict class creation
    }
}
