package com.kalix.middleware.kongclient.biz.kong.api.plugin.authentication;

import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.key.KeyAuthCredential;
import com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.key.KeyAuthCredentialList;

/**
 * Created by vaibhav on 15/06/17.
 *
 * Updated by dvilela on 17/10/17.
 */
public interface KeyAuthService {
    KeyAuthCredential addCredentials(String consumerIdOrUsername, String key);
    KeyAuthCredentialList listCredentials(String consumerIdOrUsername, Long size, String offset);
    void deleteCredential(String consumerIdOrUsername, String id);
}
