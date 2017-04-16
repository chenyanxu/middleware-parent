package com.kalix.middleware.oauth.web;

import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by admin on 2017/4/16.
 */
public class Util {
    public static void respWrite(HttpServletResponse resp, OAuthResponse response) throws IOException {
        resp.setHeader("Content-Type", "application/json; charset=utf-8");
        resp.setStatus(response.getResponseStatus());
        PrintWriter writer = resp.getWriter();
        writer.write(response.getBody());
        writer.flush();
        writer.close();
    }
}
