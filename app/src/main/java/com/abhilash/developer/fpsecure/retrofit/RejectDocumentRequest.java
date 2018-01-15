package com.abhilash.developer.fpsecure.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 26-09-2017
 */

public class RejectDocumentRequest implements Serializable {
    private String to;
    private String from;
    private String id;

    public RejectDocumentRequest(String to, String from, String id) {
        this.to = to;
        this.from = from;
        this.id = id;
    }
}
