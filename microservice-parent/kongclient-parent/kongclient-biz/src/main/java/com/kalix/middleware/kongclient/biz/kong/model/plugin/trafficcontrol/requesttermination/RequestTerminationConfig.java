package com.kalix.middleware.kongclient.biz.kong.model.plugin.trafficcontrol.requesttermination;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 18/06/17.
 */
//@Data
public class RequestTerminationConfig {
    @SerializedName("status_code")
    Integer statusCode;
    @SerializedName("message")
    String message;
    @SerializedName("body")
    String body;
    @SerializedName("content_type")
    String contentType;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
