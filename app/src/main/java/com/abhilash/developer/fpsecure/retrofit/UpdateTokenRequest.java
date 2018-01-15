package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class UpdateTokenRequest implements Serializable {
    private String mobile;
    private String token;

    public UpdateTokenRequest(String mobile, String token) {
        this.mobile = mobile;
        this.token = token;
    }
}
