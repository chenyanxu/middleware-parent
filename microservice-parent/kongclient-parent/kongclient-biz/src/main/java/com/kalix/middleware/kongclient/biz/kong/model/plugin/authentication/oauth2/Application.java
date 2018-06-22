package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 15/06/17.
 */
//@Data
public class Application {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("redirect_uri")
    private List<String> redirectUri;

    @SerializedName("created_at")
    private Long createdAt;

    public Application(String name, List<String> redirectUri, String clientId, String clientSecret) {
        this.name = name;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Application(String name, List<String> redirectUri) {
        this(name, redirectUri, null, null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(List<String> redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
