package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class SignUpRequest implements Serializable {
    private String name;
    private String mobile;
    private String password;
    private String device_token;

    public SignUpRequest(String name, String mobile, String password, String device_token) {
        this.name = name;
        this.mobile = mobile;
        this.password = password;
        this.device_token = device_token;
    }
}
