package com.mohnage7.marvelapp.utils;

public class Constants {
    public static final String BASE_URL = "https://gateway.marvel.com/v1/public/";
    public static final String PRIVATE_KEY = "";
    public static final String PUBLIC_KEY = "";
    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 60; //  seconds
    public static final int WRITE_TIMEOUT = 60; // seconds
    //Limit the result set to the specified number of resources.
    public static final int LIMIT= 20;
    //Skipped number of resources in the result set of characters API.
    public static final int DEFAULT_OFFSET=0;


    private Constants() {
        // restrict class creation
    }
}
