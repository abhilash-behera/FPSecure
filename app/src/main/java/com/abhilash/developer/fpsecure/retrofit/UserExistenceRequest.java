package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class UserExistenceRequest implements Serializable {
    private String mobile;

    public UserExistenceRequest(String mobile) {
        this.mobile = mobile;
    }
}
