package com.kalix.middleware.oauth.web;

import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.oauth.api.Constants;
import com.kalix.middleware.oauth.api.biz.IOauthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017-04-13.
 */
public class AuthorizeServlet extends HttpServlet {
    private static final String SERVLET_URL = "/authorize";
    private HttpService httpService;
    private IOauthService oAuthService;

    public AuthorizeServlet() {
        try {
            this.oAuthService = JNDIHelper.getJNDIServiceForName(IOauthService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //构建OAuth 授权请求
        try {
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(req);
            if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription(Constants.INVALID_CLIENT_ID)
                                .buildJSONMessage();
                resp.setHeader("Content-Type", "application/json; charset=utf-8");
                resp.setStatus(response.getResponseStatus());
                resp.getWriter().print(response.getBody());
                return;
            }

            String username = req.getParameter("username"); //获取用户名
            //生成授权码
            String authorizationCode = null;
            //responseType目前仅支持CODE，另外还有TOKEN
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oauthIssuerImpl.authorizationCode();
                oAuthService.addAuthCode(authorizationCode, username);
            }
            //进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(req, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authorizationCode);
            //得到到客户端重定向地址
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

            //构建响应
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

            //根据OAuthResponse返回ResponseEntity响应
            resp.setHeader("Location", String.valueOf(new URI(response.getLocationUri())));
            resp.setStatus(response.getResponseStatus());

        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}