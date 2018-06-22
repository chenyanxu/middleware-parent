package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 15/06/17.
 *
 * Updated by fanhua on 2017-08-07.
 */
//@Data
public class AuthorizationRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("client_id")
    private String clientId;

    /**
     * The response type, "code" or "token".
     * "code" for Authorization Code process, "token" for implicit grant process.
     * */
    @SerializedName("response_type")
    private String responseType;

    @SerializedName("created_at")
    private Long createdAt;

    @SerializedName("provision_key")
    private String provisionKey;

    @SerializedName("scope")
    private String scope;

    @SerializedName("authenticated_userid")
    private String authenticatedUserid;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getProvisionKey() {
        return provisionKey;
    }

    public void setProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthenticatedUserid() {
        return authenticatedUserid;
    }

    public void setAuthenticatedUserid(String authenticatedUserid) {
        this.authenticatedUserid = authenticatedUserid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
