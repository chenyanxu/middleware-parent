package com.kalix.middleware.oauth.biz;

import com.kalix.framework.core.impl.biz.GenericBizServiceImpl;
import com.kalix.middleware.oauth.api.biz.IClientBeanService;
import com.kalix.middleware.oauth.api.dao.IClientBeanDao;
import com.kalix.middleware.oauth.entities.ClientBean;

/**
 * Created by Administrator on 2017-04-17.
 */
public class ClientServiceImpl extends GenericBizServiceImpl<IClientBeanDao, ClientBean> implements IClientBeanService {
    @Override
    public ClientBean findByClientId(String clientid) {
        return dao.findByClientId(clientid);
    }
}
