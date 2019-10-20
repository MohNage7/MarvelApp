package com.mohnage7.marvelapp.base;

import com.google.gson.annotations.SerializedName;

public abstract class BaseResponse<T> {

    @SerializedName("code")
    private int mStatusCode;
    @SerializedName("status_message")
    private String mStatusMessage;

    public abstract T getData();

    public int getCode() {
        return mStatusCode;
    }

    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    public String getStatusMessage() {
        return mStatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        mStatusMessage = statusMessage;
    }

}
