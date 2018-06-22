package com.kalix.middleware.kongclient.biz.kong.model.admin.plugin;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.Map;

/**
 * Created by vaibhav on 13/06/17.
 *
 * You can add a plugin in four different ways:
 *  For every API and Consumer. Don't set api_id and consumer_id.
 *  For every API and a specific Consumer. Only set consumer_id.
 *  For every Consumer and a specific API. Only set api_id.
 *  For a specific Consumer and API. Set both api_id and consumer_id.
 * Note that not all plugins allow to specify consumer_id. Check the plugin documentation.
 *
 */
//@Data
public class Plugin {

    @SerializedName("id")
    private String id;

    @SerializedName("api_id")
    private String apiId;

    @SerializedName("consumer_id")
    private String consumerId;

    @SerializedName("name")
    private String name;    //must

    @SerializedName("config")
    private Object config;  //must

    @SerializedName("enabled")
    private Boolean enabled;

    @SerializedName("created_at")
    private Long createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
