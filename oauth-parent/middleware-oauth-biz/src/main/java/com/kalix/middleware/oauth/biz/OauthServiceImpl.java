package com.kalix.middleware.oauth.biz;

import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.middleware.oauth.api.biz.IOauthService;
import com.kalix.middleware.oauth.api.dao.IClientBeanDao;

/**
 * @author sunlf
 */
public class OauthServiceImpl implements IOauthService {
    ICacheManager cacheManager;
    IClientBeanDao clientBeanDao;

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setClientBeanDao(IClientBeanDao clientBeanDao) {
        this.clientBeanDao = clientBeanDao;
    }

    @Override
    public void addAuthCode(String authCode, String username) {
        cacheManager.save(authCode, username);
    }

    @Override
    public void addAccessToken(String accessToken, String username) {
        cacheManager.save(accessToken, username);
    }

    @Override
    public void addRefreshToken(String refreshToken, String username) {
        cacheManager.save(refreshToken, username);
    }

    @Override
    public boolean checkAuthCode(String authCode) {
        return cacheManager.get(authCode) != null;
    }

    @Override
    public boolean checkRefreshCode(String refreshCode) {
        return cacheManager.get(refreshCode) != null;
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
    public long getExpireIn() {
        return 3600L;
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
