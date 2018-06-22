package com.kalix.middleware.kongclient.biz.kong.model.admin.certificate;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 14/06/17.
 */
//@Data
public class Certificate {

    @SerializedName("id")
    private String id;
    @SerializedName("cert")
    private String cert;
    @SerializedName("key")
    private String key;
    @SerializedName("snis")
    private List<String> snis;
    @SerializedName("created_at")
    private Long createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getSnis() {
        return snis;
    }

    public void setSnis(List<String> snis) {
        this.snis = snis;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
