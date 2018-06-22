package com.kalix.middleware.kongclient.biz.kong.model.admin.consumer;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 12/06/17.
 */
//@Data
public class Consumer {

    String id;

    @SerializedName("custom_id")
    String customId;

    @SerializedName("created_at")
    Long createdAt;

    String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
