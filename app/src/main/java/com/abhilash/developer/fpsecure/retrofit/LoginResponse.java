package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class LoginResponse implements Serializable {
    private boolean success;
    private LoginData data;

    public boolean isSuccess() {
        return success;
    }

    public LoginData getData() {
        return data;
    }
}
