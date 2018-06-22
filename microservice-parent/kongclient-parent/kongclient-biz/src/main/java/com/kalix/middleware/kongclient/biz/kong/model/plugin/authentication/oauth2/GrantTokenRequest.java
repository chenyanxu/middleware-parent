package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;

//import lombok.Data;

/**
 * Created by fanhua on 2017-08-07.
 */
//@Data
public class GrantTokenRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    /**
     * The grant type: "authorization_code", "password", "client_credentials", or "refresh_token".
     *   "authorization_code" for Authorization Code process, the response will contain both access_token & refresh_token.
     *   "password" for Password Credentials process, the response will contain both access_token & refresh_token.
     *   "client_credentials" for Client Credentials process, the response will contain access_token only.
     *   "refresh_token" for Refresh Token process, the response will contain both access_token & refresh_token.
     * */
    @SerializedName("grant_type")
    private String grantType;

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

    /**
     * Only used in "Authorization Code" process
     * */
    @SerializedName("code")
    private String code;

    /**
     * Only used in "Refresh Token" process
     * */
    @SerializedName("refresh_token")
    private String refreshToken;

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

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
