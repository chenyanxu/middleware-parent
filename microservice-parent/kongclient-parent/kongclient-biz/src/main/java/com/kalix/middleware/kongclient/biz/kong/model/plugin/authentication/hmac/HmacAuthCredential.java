package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.hmac;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;
//import lombok.NoArgsConstructor;

/**
 * Created by vaibhav on 15/06/17.
 */
//@Data
//@NoArgsConstructor
public class HmacAuthCredential {

    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("secret")
    private String secret;
    @SerializedName("consumer_id")
    private String consumerId;
    @SerializedName("created_at")
    private Long createdAt;

    public HmacAuthCredential() {

    }

    public HmacAuthCredential(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
