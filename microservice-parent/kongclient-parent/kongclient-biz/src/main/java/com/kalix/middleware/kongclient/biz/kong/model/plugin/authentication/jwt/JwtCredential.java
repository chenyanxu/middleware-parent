package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.jwt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhav on 16/06/17.
 *
 * Updated by dvilela on 17/10/17.
 */
public class JwtCredential {

    @SerializedName("rsa_public_key")
    private String rsaPublicKey;
    @SerializedName("consumer_id")
    private String consumerId;
    @SerializedName("id")
    private String id;
    @SerializedName("created_at")
    private Long createdAt;
    @SerializedName("key")
    private String key;
    @SerializedName("algorithm")
    private String algorithm;
    @SerializedName("secret")
    private String secret;

    public JwtCredential() {

    }

    public JwtCredential(String rsaPublicKey, String algorithm) {
        this.rsaPublicKey = rsaPublicKey;
        this.algorithm = algorithm;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
