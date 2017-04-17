package com.kalix.middleware.oauth.biz;

import com.kalix.framework.core.impl.biz.GenericBizServiceImpl;
import com.kalix.middleware.oauth.api.biz.IUserBeanService;
import com.kalix.middleware.oauth.api.dao.IUserBeanDao;
import com.kalix.middleware.oauth.entities.UserBean;

/**
 * Created by sunlf on 2017-04-17.
 */
public class UserServiceImpl extends GenericBizServiceImpl<IUserBeanDao, UserBean> implements IUserBeanService {
}