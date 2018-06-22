package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 15/06/17.
 */
//@Data
public class Redirect {
    @SerializedName("redirect_uri")
    String redirectUri;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
