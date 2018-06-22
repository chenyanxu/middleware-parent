package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.key;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 17/06/17.
 */
//@Data
public class KeyAuthConfig {

    @SerializedName("hide_credentials")
    Boolean hideCredentials;
    @SerializedName("anonymous")
    String anonymous;
    @SerializedName("key_names")
    List<String> keyNames;
    @SerializedName("key_in_body")
    Boolean keyInBody;

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

    public List<String> getKeyNames() {
        return keyNames;
    }

    public void setKeyNames(List<String> keyNames) {
        this.keyNames = keyNames;
    }

    public Boolean getKeyInBody() {
        return keyInBody;
    }

    public void setKeyInBody(Boolean keyInBody) {
        this.keyInBody = keyInBody;
    }
}
