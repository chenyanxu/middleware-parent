package com.kalix.middleware.kongclient.biz.kong.model.plugin.security.acl;

//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 18/06/17.
 */
//@Data
public class AclConfig {
    List<String> whitelist;
    List<String> blacklist;

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }
}
