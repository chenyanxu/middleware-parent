package com.kalix.middleware.oauth.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.middleware.oauth.api.dao.IClientBeanDao;
import com.kalix.middleware.oauth.entities.ClientBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Administrator on 2017-04-13.
 */
public class ClientBeanDaoImpl extends GenericDao<ClientBean, Long> implements IClientBeanDao {
    @Override
    @PersistenceContext(unitName = "oauth-cm")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }

    @Override
    public ClientBean findByClientId(String clientId) {
        String sql = "SELECT c FROM ClientBean c WHERE c.clientId=?1";
        List<ClientBean> clientList = find(sql, clientId);
        if (clientList.size() == 0) {
            return null;
        }
        return clientList.get(0);
    }

    @Override
    public ClientBean findByClientSecret(String clientSecret) {
        String sql = "select c from ClientBean c where c.clientSecret=?1";
        List<ClientBean> clientList = find(sql, clientSecret);
        if (clientList.size() == 0) {
            return null;
        }
        return clientList.get(0);
    }
}
