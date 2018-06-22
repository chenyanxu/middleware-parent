package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.ldap;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 17/06/17.
 */
//@Data
public class LdapConfig {

    @SerializedName("hide_credentials")
    Boolean hideCredentials;
    @SerializedName("anonymous")
    String anonymous;
    @SerializedName("ldap_host")
    String ldapHost;
    @SerializedName("ldap_port")
    Integer ldapPort;
    @SerializedName("start_tls")
    Boolean startTls;
    @SerializedName("base_dn")
    String baseDn;
    @SerializedName("verify_ldap_host")
    Boolean verifyLdapHost;
    @SerializedName("attribute")
    String attribute;
    @SerializedName("cache_ttl")
    Integer cacheTtl;
    @SerializedName("timeout")
    Integer timeout;
    @SerializedName("keepalive")
    Integer keepalive;

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

    public String getLdapHost() {
        return ldapHost;
    }

    public void setLdapHost(String ldapHost) {
        this.ldapHost = ldapHost;
    }

    public Integer getLdapPort() {
        return ldapPort;
    }

    public void setLdapPort(Integer ldapPort) {
        this.ldapPort = ldapPort;
    }

    public Boolean getStartTls() {
        return startTls;
    }

    public void setStartTls(Boolean startTls) {
        this.startTls = startTls;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public Boolean getVerifyLdapHost() {
        return verifyLdapHost;
    }

    public void setVerifyLdapHost(Boolean verifyLdapHost) {
        this.verifyLdapHost = verifyLdapHost;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Integer getCacheTtl() {
        return cacheTtl;
    }

    public void setCacheTtl(Integer cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
    }
}
