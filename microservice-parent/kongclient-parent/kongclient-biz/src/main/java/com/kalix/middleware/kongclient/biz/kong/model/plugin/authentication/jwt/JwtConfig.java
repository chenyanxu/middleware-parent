package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 17/06/17.
 */
//@Data
public class JwtConfig {

    @SerializedName("key_claim_name")
    String keyClaimName;
    @SerializedName("anonymous")
    String anonymous;
    @SerializedName("claims_to_verify")
    List<String> claimsToVerify;
    @SerializedName("uri_param_names")
    List<String> uriParamNames;
    @SerializedName("secret_is_base64")
    Boolean secretIsBase64;

    public String getKeyClaimName() {
        return keyClaimName;
    }

    public void setKeyClaimName(String keyClaimName) {
        this.keyClaimName = keyClaimName;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public List<String> getClaimsToVerify() {
        return claimsToVerify;
    }

    public void setClaimsToVerify(List<String> claimsToVerify) {
        this.claimsToVerify = claimsToVerify;
    }

    public List<String> getUriParamNames() {
        return uriParamNames;
    }

    public void setUriParamNames(List<String> uriParamNames) {
        this.uriParamNames = uriParamNames;
    }

    public Boolean getSecretIsBase64() {
        return secretIsBase64;
    }

    public void setSecretIsBase64(Boolean secretIsBase64) {
        this.secretIsBase64 = secretIsBase64;
    }
}
