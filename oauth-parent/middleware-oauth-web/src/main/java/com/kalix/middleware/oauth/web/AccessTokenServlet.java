package com.kalix.middleware.oauth.web;

import com.kalix.framework.core.api.dto.AudienceBean;
import com.kalix.framework.core.api.jwt.IJwtService;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.OsgiUtil;
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
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2017-04-13.
 */
public class AccessTokenServlet extends HttpServlet {
    private IJwtService jwtService;
    private HttpService httpService;
    private IOauthService oAuthService;
    private static final String SERVLET_URL = "/accessToken";

    public AccessTokenServlet() {

    }

    public void postConstruct() {
        try {
            httpService.registerServlet(SERVLET_URL, this, null, null);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    public void preDestroy() {
        httpService.unregister(SERVLET_URL);
    }

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void setoAuthService(IOauthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    public void setJwtService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resp) {

        try {
            /**
             if(jwtService==null) {
             if(JNDIHelper.getJNDIServiceForNameNoCatch(IJwtService.class.getName()))
             {
             jwtService = JNDIHelper.getJNDIServiceForName(IJwtService.class.getName());
             }

             }
             **/
            if (oAuthService == null) {
                try {
                    this.oAuthService = OsgiUtil.waitForServices(IOauthService.class, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            //检查提交的客户端id是否正确
            if (!oAuthService.checkClientId(oauthRequest.getClientId())) {

                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription(Constants.INVALID_CLIENT_ID)
                                .buildJSONMessage();
                Util.respWrite(resp, response);
                return;

            }

            // 检查客户端安全KEY是否正确
            if (!oAuthService.checkClientSecret(oauthRequest.getClientSecret())) {
                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                                .setErrorDescription(Constants.INVALID_CLIENT_ID)
                                .buildJSONMessage();
                Util.respWrite(resp, response);
                return;
            }

            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (!oAuthService.checkAuthCode(authCode)) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(Constants.INVALID_AUTH_CODE)
                            .buildJSONMessage();
                    Util.respWrite(resp, response);
                    return;
                }
            }

            String accessToken = "";
            String refreshToken = "";
//            if(jwtService!=null)
//            {
//                AudienceBean audienceEntity = jwtService.getAudien();
//                accessToken = jwtService.createJWT( oAuthService.getUsernameByAuthCode(authCode),  oAuthService.getUsernameByAuthCode(authCode),
//                        "", audienceEntity.getClientId(), audienceEntity.getName(),
//                        audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());
//                refreshToken = jwtService.createJWT( oAuthService.getUsernameByAuthCode(authCode),  oAuthService.getUsernameByAuthCode(authCode),
//                        "", audienceEntity.getClientId(), audienceEntity.getName(),
//                        audienceEntity.getRefresh_expiresSecond()* 1000, audienceEntity.getBase64Secret());
//            }else {
            //生成Access Token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            accessToken = oauthIssuerImpl.accessToken();
            refreshToken = oauthIssuerImpl.refreshToken();

//            }

            oAuthService.addAccessToken(accessToken, oAuthService.getUsernameByAuthCode(authCode));
            oAuthService.addRefreshToken(refreshToken, oAuthService.getUsernameByAuthCode(authCode));
            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(oAuthService.getExpireIn().toString())
                    .setRefreshToken(refreshToken)
                    .buildJSONMessage();
            Util.respWrite(resp, response);

        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }
}
