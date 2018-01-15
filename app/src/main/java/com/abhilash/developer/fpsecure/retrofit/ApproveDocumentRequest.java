package com.abhilash.developer.fpsecure.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Abhilash on 19-09-2017
 */

public class ApproveDocumentRequest implements Serializable {
    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("id")
    @Expose
    private String id;

    public ApproveDocumentRequest(String to, String from, String id) {
        this.to = to;
        this.from = from;
        this.id=id;
    }
}
