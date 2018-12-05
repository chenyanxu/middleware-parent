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
    private final String configName = "ConfigSystem";

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
        cacheManager.save(authCode, username, getExpireIn().intValue());
    }

    @Override
    public void addAccessToken(String accessToken, String username) {
        cacheManager.save(accessToken, username, getExpireIn().intValue());
    }

    @Override
    public void addRefreshToken(String refreshToken, String username) {
        // 刷新token 5天清空一次
        cacheManager.save(refreshToken, username, 432000);
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
    public Long getExpireIn() {
//        String auth2Timeout = (String)ConfigUtil.getConfigProp("auth2TimeOut", configName);
//        Long timeout = Long.valueOf(auth2Timeout);
//        this.expireIn = timeout;
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
