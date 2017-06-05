package com.kalix.middleware.oauth.api.biz;

/**
 * @author sunlf
 */
public interface IOauthService {
    //添加 auth code
    void addAuthCode(String authCode, String username);

    //添加 access token
    void addAccessToken(String accessToken, String username);

    //添加 refresh token
    void addRefreshToken(String refreshToken, String username);

    //验证auth code是否有效
    boolean checkAuthCode(String authCode);

    //验证refresh code是否有效
    boolean checkRefreshCode(String refreshCode);

    //验证access token是否有效
    boolean checkAccessToken(String accessToken);

    String getUsernameByAuthCode(String authCode);

    String getUsernameByAccessToken(String accessToken);

    //auth code / access token 过期时间
    Long getExpireIn();

    boolean checkClientId(String clientId);

    boolean checkClientSecret(String clientSecret);


}
