package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.hmac;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 17/06/17.
 */
//@Data
public class HmacAuthConfig {

    @SerializedName("hide_credentials")
    Boolean hideCredentials;
    @SerializedName("anonymous")
    String anonymous;
    @SerializedName("clock_skew")
    Integer clockSkew;

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

    public Integer getClockSkew() {
        return clockSkew;
    }

    public void setClockSkew(Integer clockSkew) {
        this.clockSkew = clockSkew;
    }
}
