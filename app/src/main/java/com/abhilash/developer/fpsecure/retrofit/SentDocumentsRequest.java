package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 19-09-2017
 */

public class SentDocumentsRequest implements Serializable {
    private String mobile;

    public SentDocumentsRequest(String mobile) {
        this.mobile = mobile;
    }
}
