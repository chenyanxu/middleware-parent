package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 15/06/17.
 */
@Deprecated
//@Data
public class RefreshRequest {

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("grant_type")
    private String grantType = "refresh_token";

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
}
