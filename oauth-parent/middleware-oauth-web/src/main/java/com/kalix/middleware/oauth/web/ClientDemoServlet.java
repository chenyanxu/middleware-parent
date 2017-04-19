package com.kalix.middleware.oauth.web;

import com.google.gson.Gson;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.oauth.api.biz.IClientBeanService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by admin on 2017/4/18.
 */
@WebServlet("/clientdemo")
public class ClientDemoServlet extends HttpServlet {
    private IClientBeanService clientService;

    public ClientDemoServlet() {
        try {
            this.clientService = JNDIHelper.getJNDIServiceForName(IClientBeanService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/json; charset=utf-8");
        resp.setStatus(200);
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        writer.write(gson.toJson(clientService.getAllEntity()));
        writer.flush();
        writer.close();
    }
}
