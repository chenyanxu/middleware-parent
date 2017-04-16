package com.kalix.middleware.oauth.web;

import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.oauth.api.biz.IOauthService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by admin on 2017/4/16.
 * 检查token是否有效
 */
public class CheckAccessTokenServlet extends HttpServlet {
    private IOauthService oAuthService;

    public CheckAccessTokenServlet() {
        try {
            this.oAuthService = JNDIHelper.getJNDIServiceForName(IOauthService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getParameter("accessToken");
        boolean b = oAuthService.checkAccessToken(accessToken);
        if (b) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
