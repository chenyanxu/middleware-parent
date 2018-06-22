package com.kalix.middleware.kongclient.biz.kong.model.admin.sni;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class Sni {
    @SerializedName("ssl_certificate_id")
    private String sslCertificateId;
    @SerializedName("name")
    private String name;
    @SerializedName("created_at")
    private Long createdAt;

    public String getSslCertificateId() {
        return sslCertificateId;
    }

    public void setSslCertificateId(String sslCertificateId) {
        this.sslCertificateId = sslCertificateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
