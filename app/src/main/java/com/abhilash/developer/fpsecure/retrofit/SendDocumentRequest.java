package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 17-09-2017
 */

public class SendDocumentRequest implements Serializable {
    private String to;
    private String from;
    private String description;

    public SendDocumentRequest(String to, String from, String description) {
        this.to = to;
        this.from = from;
        this.description = description;
    }
}
