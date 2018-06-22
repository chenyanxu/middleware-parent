package com.kalix.middleware.kongclient.biz.kong.model.admin.plugin;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 14/06/17.
 */
//@Data
public class EnabledPlugins {

    @SerializedName("enabled_plugins")
    List<String> enabledPlugins;

    public List<String> getEnabledPlugins() {
        return enabledPlugins;
    }

    public void setEnabledPlugins(List<String> enabledPlugins) {
        this.enabledPlugins = enabledPlugins;
    }
}
