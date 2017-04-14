package com.kalix.middleware.oauth.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.middleware.oauth.api.dao.IClientBeanDao;
import com.kalix.middleware.oauth.entities.ClientBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Administrator on 2017-04-13.
 */
public class ClientBeanDaoImpl extends GenericDao<ClientBean, Long> implements IClientBeanDao {
    @Override
    @PersistenceContext(unitName = "oauth-cm")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
