package com.kalix.middleware.websocket.biz.servlet;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.middleware.websocket.api.biz.IWebsocketService;
import org.eclipse.jetty.websocket.api.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2018/6/4.
 */
public class KarafWebSocketActivator {

    private static final long WEBSOCKET_TIMEOUT = -1;
    private static final Map<String, Set<Session>> clientSessions = new HashMap<String, Set<Session>>();

    public static void registerConnection(String key, Session session) {
        session.setIdleTimeout(KarafWebSocketActivator.WEBSOCKET_TIMEOUT);
        if (clientSessions.containsKey(key)) {
            clientSessions.get(key).add(session);
        } else {
            Set<Session> sessions = new HashSet<Session>();
            sessions.add(session);
            clientSessions.put(key, sessions);
        }
    }

    public static void unregisterConnection(String key, Session session) {
        clientSessions.get(key).remove(session);
    }

    /**
     * 接收前台消息后，转发给前台自己（实现可重写，如群发消息等取clientSessions值）
     * @param message
     * @param client
     */
    public static void onMessage(String message, Session client) {
        try {
            client.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Set<Session>> getClientSessions() {
        return KarafWebSocketActivator.clientSessions;
    }
}
