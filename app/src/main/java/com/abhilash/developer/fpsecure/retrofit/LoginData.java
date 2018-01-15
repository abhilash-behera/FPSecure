package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class LoginData implements Serializable {
    private String name;
    private String mobile;
    private String device_token;

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDevice_token() {
        return device_token;
    }
}
