package com.kalix.middleware.oauth.biz;

import com.kalix.framework.core.impl.biz.GenericBizServiceImpl;
import com.kalix.framework.core.util.PasswordHelper;
import com.kalix.middleware.oauth.api.biz.IUserBeanService;
import com.kalix.middleware.oauth.api.dao.IUserBeanDao;
import com.kalix.middleware.oauth.entities.UserBean;

import java.util.List;

/**
 * Created by sunlf on 2017-04-17.
 */
public class UserServiceImpl extends GenericBizServiceImpl<IUserBeanDao, UserBean> implements IUserBeanService {
    @Override
    public UserBean findByUsername(String username) {
        String sql = "SELECT c FROM UserBean c WHERE c.username=?1";
        List<UserBean> userList = dao.find(sql, username);
        if (userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    /**
     * 验证登录
     *
     * @param username   用户名
     * @param password   密码
     * @param salt       盐
     * @param encryptpwd 加密后的密码
     * @return
     */
    public boolean checkUser(String username, String password, String salt, String encryptpwd) {
        String pwd = PasswordHelper.encryptPassword(username, password, salt);
        return pwd.equals(encryptpwd);
    }
}