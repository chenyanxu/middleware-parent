package com.kalix.middleware.oauth.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.middleware.oauth.api.dao.IUserBeanDao;
import com.kalix.middleware.oauth.entities.UserBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Administrator on 2017-04-13.
 */
public class UserBeanDaoImpl extends GenericDao<UserBean, String> implements IUserBeanDao {
    @Override
    @PersistenceContext(unitName = "oauth-cm")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
