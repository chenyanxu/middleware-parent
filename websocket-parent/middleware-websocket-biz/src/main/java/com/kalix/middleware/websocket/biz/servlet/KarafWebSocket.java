package com.kalix.middleware.websocket.biz.servlet;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.websocket.ClientEndpoint;

/**
 * Created by Administrator on 2018/6/4.
 */
@WebSocket
@ClientEndpoint
public class KarafWebSocket {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        KarafWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        KarafWebSocket.onlineCount--;
    }

    /**
     * 连接建立成功调用的方法
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnWebSocketConnect
    public void onOpen(Session session) {
        addOnlineCount();           //在线数加1
        String loginname = session.getUpgradeRequest().getParameterMap().get("loginname").get(0);
        KarafWebSocketActivator.registerConnection(loginname, session);
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     * @param session
     * @param statusCode
     * @param reason
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        subOnlineCount();           //在线数减1
        String loginname = session.getUpgradeRequest().getParameterMap().get("loginname").get(0);
        KarafWebSocketActivator.unregisterConnection(loginname, session);
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnWebSocketMessage
    public void onText(Session session, String message) {
        KarafWebSocketActivator.onMessage(message, session);
    }
}
