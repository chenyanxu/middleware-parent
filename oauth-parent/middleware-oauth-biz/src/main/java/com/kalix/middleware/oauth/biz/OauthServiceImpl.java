package com.kalix.middleware.oauth.biz;

import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.middleware.oauth.api.biz.IOauthService;
import com.kalix.middleware.oauth.api.dao.IClientBeanDao;

/**
 * @author sunlf
 */
public class OauthServiceImpl implements IOauthService {
    private ICacheManager cacheManager;
    private IClientBeanDao clientBeanDao;
    private Long expireIn;

    public OauthServiceImpl(){
        //this.expireIn=3600L;
    }

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setClientBeanDao(IClientBeanDao clientBeanDao) {
        this.clientBeanDao = clientBeanDao;
    }

    @Override
    public void addAuthCode(String authCode, String username) {
        cacheManager.save(authCode, username,getExpireIn().intValue());
    }

    @Override
    public void addAccessToken(String accessToken, String username) {
        cacheManager.save(accessToken, username,getExpireIn().intValue());
    }

    @Override
    public boolean checkAuthCode(String authCode) {
        return cacheManager.get(authCode) != null;
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        return cacheManager.get(accessToken) != null;
    }

    @Override
    public String getUsernameByAuthCode(String authCode) {
        return cacheManager.get(authCode);
    }

    @Override
    public String getUsernameByAccessToken(String accessToken) {
        return cacheManager.get(accessToken);
    }

    @Override
    public Long getExpireIn() {
        return this.expireIn;
    }

    public void setExpireIn(Long expireIn){
        this.expireIn=expireIn;
    }

    @Override
    public boolean checkClientId(String clientId) {
        return clientBeanDao.findByClientId(clientId) != null;
    }

    @Override
    public boolean checkClientSecret(String clientSecret) {
        return clientBeanDao.findByClientSecret(clientSecret) != null;
    }
}
