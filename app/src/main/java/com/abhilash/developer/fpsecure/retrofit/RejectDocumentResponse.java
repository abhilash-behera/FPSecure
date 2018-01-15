package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 26-09-2017
 */

public class RejectDocumentResponse implements Serializable {
    private boolean success;
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }
}
