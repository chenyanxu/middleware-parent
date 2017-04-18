package com.kalix.middleware.oauth.api.biz;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.middleware.oauth.entities.UserBean;

/**
 * Created by sunlf on 2017-04-17.
 */
public interface IUserBeanService extends IBizService<UserBean> {
    UserBean findByUsername(String username);

    /**
     * 验证登录
     *
     * @param username   用户名
     * @param password   密码
     * @param salt       盐
     * @param encryptpwd 加密后的密码
     * @return
     */
    boolean checkUser(String username, String password, String salt, String encryptpwd);
    //在此添加新的业务方法
}