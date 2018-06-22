package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.basic;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 17/06/17.
 */
//@Data
public class BasicAuthConfig {

    @SerializedName("hide_credentials")
    Boolean hideCredentials;
    @SerializedName("anonymous")
    String anonymous;

    public Boolean getHideCredentials() {
        return hideCredentials;
    }

    public void setHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }
}
