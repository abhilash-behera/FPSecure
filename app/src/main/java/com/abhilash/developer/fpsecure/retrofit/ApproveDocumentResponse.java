package com.abhilash.developer.fpsecure.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Abhilash on 19-09-2017
 */

public class ApproveDocumentResponse implements Serializable {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }
}
