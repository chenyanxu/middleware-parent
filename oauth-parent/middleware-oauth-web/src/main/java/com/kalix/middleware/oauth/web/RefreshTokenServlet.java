package com.kalix.middleware.oauth.web;

import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.oauth.api.Constants;
import com.kalix.middleware.oauth.api.biz.IOauthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sunlf on 2017/4/25.
 * 刷新access_token
 * 如果用户访问的时候，客户端的"访问令牌"已经过期，则需要使用"更新令牌"申请一个新的访问令牌。
 * 客户端发出更新令牌的HTTP请求，包含以下参数：
 * granttype：表示使用的授权模式，此处的值固定为"refreshtoken"，必选项。
 * refresh_token：表示早前收到的更新令牌，必选项。
 * scope：表示申请的授权范围，不可以超出上一次申请的范围，如果省略该参数，则表示与上一次一致。
 */
public class RefreshTokenServlet extends HttpServlet {
    private IOauthService oAuthService;
    private static final String SERVLET_URL = "/refreshToken";

    public RefreshTokenServlet() {
        try {
            this.oAuthService = JNDIHelper.getJNDIServiceForName(IOauthService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            String refreshCode = oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN);
            //验证授权类型是否为refresh_token
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                if (!oAuthService.checkRefreshCode(refreshCode)) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(Constants.INVALID_AUTH_CODE)
                            .buildJSONMessage();
                    Util.respWrite(resp, response);
                    return;
                } else {
                    //重新生成Access Token
                    OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                    final String accessToken = oauthIssuerImpl.accessToken();
                    //通过refreshCode获得用户名，因为此时accessCode已经过期了
                    String username = oAuthService.getUsernameByAuthCode(refreshCode);
                    oAuthService.addAccessToken(accessToken, username);
                    oAuthService.addRefreshToken(refreshCode, username);

                    //生成OAuth响应
                    OAuthResponse response = OAuthASResponse
                            .tokenResponse(HttpServletResponse.SC_OK)
                            .setAccessToken(accessToken)
                            .setRefreshToken(refreshCode)
                            .setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
                            .buildJSONMessage();
                    Util.respWrite(resp, response);

                }
            }
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse resp) {
        doGet(request, resp);
    }
}
