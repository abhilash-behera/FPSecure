package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class LoginRequest implements Serializable {
    private String mobile;
    private String password;

    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
