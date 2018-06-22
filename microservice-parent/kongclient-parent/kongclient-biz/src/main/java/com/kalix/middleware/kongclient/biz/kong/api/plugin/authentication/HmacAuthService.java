package com.kalix.middleware.kongclient.biz.kong.api.plugin.authentication;

/**
 * Created by vaibhav on 15/06/17.
 */
public interface HmacAuthService {
    void addCredentials(String consumerIdOrUsername, String username, String secret);
}
