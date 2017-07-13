package com.kalix.middleware.oauth.web;

import com.kalix.framework.core.util.OsgiUtil;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.middleware.oauth.api.Constants;
import com.kalix.middleware.oauth.api.biz.IClientBeanService;
import com.kalix.middleware.oauth.api.biz.IOauthService;
import com.kalix.middleware.oauth.api.biz.IUserBeanService;
import com.kalix.middleware.oauth.entities.UserBean;
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

import javax.servlet.RequestDispatcher;
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
    private IUserBeanService userService;
    private IOauthService oAuthService;
    private IClientBeanService clientService;

    public AuthorizeServlet() {
        try {
            this.oAuthService = OsgiUtil.waitForServices(IOauthService.class, null);
            this.userService = OsgiUtil.waitForServices(IUserBeanService.class, null);
            this.clientService = OsgiUtil.waitForServices(IClientBeanService.class, null);
        } catch (Exception e) {
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

            //如果用户没有登录，跳转到登陆页面
            if (!login(req)) {//登录失败时跳转到登陆页面
                req.setAttribute("client", clientService.findByClientId(oauthRequest.getClientId()));
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("/oauth2login.jsp");
                requestDispatcher.forward(req, resp);
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
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private boolean login(HttpServletRequest request) {
        if ("get".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("error", "非法的请求");
            return false;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            request.setAttribute("error", "登录失败:用户名或密码不能为空");
            return false;
        }
        try {
            // 写登录逻辑
            UserBean user = userService.findByUsername(username);
            if (user != null) {
                if (!userService.checkUser(username, password, user.getSalt(), user.getPassword())) {
                    request.setAttribute("error", "登录失败:密码不正确");
                    return false;
                } else {
                    return true;
                }
            } else {
                request.setAttribute("error", "登录失败:用户名不正确");
                return false;
            }
        } catch (Exception e) {
            request.setAttribute("error", "登录失败:" + e.getClass().getName());
            return false;
        }
    }
}