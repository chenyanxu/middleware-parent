package com.kalix.middleware.kongclient.biz.kong.api.plugin.security;

import com.kalix.middleware.kongclient.biz.kong.model.plugin.security.acl.AclList;

/**
 * Created by vaibhav on 18/06/17.
 *
 * Upated by dvilela on 22/10/17.
 */
public interface AclService {
    void associateConsumer(String usernameOrId, String group);
    AclList listAcls(String usernameOrId, Long size, String offset);
}
