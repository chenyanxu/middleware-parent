package com.kalix.middleware.kongclient.biz.kong.model.admin.aim;

import com.google.gson.annotations.SerializedName;

public class Target {

    @SerializedName("id")
    private String id;
    @SerializedName("target")
    private String target;
    @SerializedName("weight")
    private Long weight;
    @SerializedName("upstream_id")
    private String upstreamId;
    @SerializedName("created_at")
    private Long createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getUpstreamId() {
        return upstreamId;
    }

    public void setUpstreamId(String upstreamId) {
        this.upstreamId = upstreamId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
