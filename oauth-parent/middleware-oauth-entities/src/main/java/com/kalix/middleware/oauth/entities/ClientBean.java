package com.kalix.middleware.oauth.entities;

import com.kalix.framework.core.api.persistence.PersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Administrator on 2017-04-13.
 */
@Entity
@Table(name = "middleware_oauth_client")
public class ClientBean extends PersistentEntity {
    private String clientName;
    private String clientId = UUID.randomUUID().toString();
    private String clientSecret = UUID.randomUUID().toString();

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
