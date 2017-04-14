package com.kalix.middleware.oauth.biz;

import com.kalix.middleware.oauth.api.biz.IOauthService;

/**
 * @author sunlf
 */
public class OauthServiceImpl implements IOauthService {

    @Override
    public void addAuthCode(String authCode, String username) {

    }

    @Override
    public void addAccessToken(String accessToken, String username) {

    }

    @Override
    public boolean checkAuthCode(String authCode) {
        return false;
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        return false;
    }

    @Override
    public String getUsernameByAuthCode(String authCode) {
        return null;
    }

    @Override
    public String getUsernameByAccessToken(String accessToken) {
        return null;
    }

    @Override
    public long getExpireIn() {
        return 3600L;
    }

    @Override
    public boolean checkClientId(String clientId) {
        return false;
    }

    @Override
    public boolean checkClientSecret(String clientSecret) {
        return false;
    }
}
