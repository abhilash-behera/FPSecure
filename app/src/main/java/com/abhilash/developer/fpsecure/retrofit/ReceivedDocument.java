package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 19-09-2017
 */

public class ReceivedDocument implements Serializable {
    private String to;
    private String from;
    private String status;
    private String description;
    private String id;

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getId(){
        return id;
    }
}
