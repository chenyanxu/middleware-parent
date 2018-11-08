package com.kalix.middleware.oauth.api.dao;

import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.middleware.oauth.entities.ClientBean;

/**
 * Created by sunlf on 2017-04-13.
 */
public interface IClientBeanDao extends IGenericDao<ClientBean, String> {
    //在此添加新的DAO方法
    ClientBean findByClientId(String clientId);

    ClientBean findByClientSecret(String clientSecret);
}