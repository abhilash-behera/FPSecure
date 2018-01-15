package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 19-09-2017
 */

public class SentDocument implements Serializable {
    private String to;
    private String from;
    private String description;
    private String status;

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
