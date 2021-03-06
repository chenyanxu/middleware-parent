package com.kalix.middleware.oauth.api;

public class Constants {
    public static final String RESOURCE_SERVER_NAME = "oauth-server";
    public static final String INVALID_CLIENT_ID = "客户端验证失败，如错误的client_id/client_secret。";
    public static final String INVALID_ACCESS_TOKEN = "accessToken unvalidated or over time";
    public static final String INVALID_REDIRECT_URI = "缺少授权成功后的回调地址。";
    public static final String INVALID_AUTH_CODE = "failed auth code";
    // 验证accessToken
    public static final String CHECK_ACCESS_CODE_URL = "http://localhost:8181/oauth2/checkAccessToken?accessToken=";

}
