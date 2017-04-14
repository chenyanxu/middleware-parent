package com.kalix.middleware.oauth.entities;

import com.kalix.framework.core.api.persistence.PersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sunlf on 2017-04-13.
 */
@Entity
@Table(name = "middleware_oauth_user")
public class UserBean extends PersistentEntity {
    private String username; //用户名
    private String password; //密码
    private String salt; //加密密码的盐

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
