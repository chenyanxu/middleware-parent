package com.kalix.middleware.kongclient.biz.kong.api.admin;

import com.kalix.middleware.kongclient.biz.kong.model.admin.plugin.EnabledPlugins;

public interface PluginRepoService {

	EnabledPlugins retrieveEnabledPlugins();

	Object retrievePluginSchema(String pluginName);
}
