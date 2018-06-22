package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 17/06/17.
 *
 * Updated by fanhua on 2017-08-04.
 */
@Deprecated
//@Data
public class OAuth2Config {

    @SerializedName("provision_key")
    private String provisionKey;

    @SerializedName("scopes")
    List<String> scopes;

    @SerializedName("mandatory_scope")
    Boolean mandatoryScope;

    @SerializedName("token_expiration")
    Integer tokenExpiration;

    @SerializedName("enable_authorization_code")
    Boolean enableAuthorizationCode;

    @SerializedName("enable_client_credentials")
    Boolean enableClientCredentials;

    @SerializedName("enable_implicit_grant")
    Boolean enableImplicitGrant;

    @SerializedName("enable_password_grant")
    Boolean enablePasswordGrant;

    @SerializedName("hide_credentials")
    Boolean hideCredentials;

    @SerializedName("global_credentials")
    Boolean globalCredentials;

    @SerializedName("accept_http_if_already_terminated")
    Boolean acceptHttpIfAlreadyTerminated;

    @SerializedName("anonymous")
    String anonymous;

    public String getProvisionKey() {
        return provisionKey;
    }

    public void setProvisionKey(String provisionKey) {
        this.provisionKey = provisionKey;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public Boolean getMandatoryScope() {
        return mandatoryScope;
    }

    public void setMandatoryScope(Boolean mandatoryScope) {
        this.mandatoryScope = mandatoryScope;
    }

    public Integer getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Integer tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public Boolean getEnableAuthorizationCode() {
        return enableAuthorizationCode;
    }

    public void setEnableAuthorizationCode(Boolean enableAuthorizationCode) {
        this.enableAuthorizationCode = enableAuthorizationCode;
    }

    public Boolean getEnableClientCredentials() {
        return enableClientCredentials;
    }

    public void setEnableClientCredentials(Boolean enableClientCredentials) {
        this.enableClientCredentials = enableClientCredentials;
    }

    public Boolean getEnableImplicitGrant() {
        return enableImplicitGrant;
    }

    public void setEnableImplicitGrant(Boolean enableImplicitGrant) {
        this.enableImplicitGrant = enableImplicitGrant;
    }

    public Boolean getEnablePasswordGrant() {
        return enablePasswordGrant;
    }

    public void setEnablePasswordGrant(Boolean enablePasswordGrant) {
        this.enablePasswordGrant = enablePasswordGrant;
    }

    public Boolean getHideCredentials() {
        return hideCredentials;
    }

    public void setHideCredentials(Boolean hideCredentials) {
        this.hideCredentials = hideCredentials;
    }

    public Boolean getGlobalCredentials() {
        return globalCredentials;
    }

    public void setGlobalCredentials(Boolean globalCredentials) {
        this.globalCredentials = globalCredentials;
    }

    public Boolean getAcceptHttpIfAlreadyTerminated() {
        return acceptHttpIfAlreadyTerminated;
    }

    public void setAcceptHttpIfAlreadyTerminated(Boolean acceptHttpIfAlreadyTerminated) {
        this.acceptHttpIfAlreadyTerminated = acceptHttpIfAlreadyTerminated;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }
}
