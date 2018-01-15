package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 19-09-2017
 */

public class ReceivedDocumentsRequest implements Serializable {
    private String mobile;

    public ReceivedDocumentsRequest(String mobile) {
        this.mobile = mobile;
    }
}
