package com.kalix.middleware.websocket.biz.quartz;

import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.websocket.api.biz.IWebsocketService;
import com.kalix.middleware.websocket.biz.servlet.KarafWebSocketActivator;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2018/6/6.
 */

public class TimeJob {
    private IWebsocketService websocketService;

    /**
     * 获取消息并推送消息
     */
    public void pushData() {
        Map<String, Set<Session>> maps = KarafWebSocketActivator.getClientSessions();
        Iterator<Map.Entry<String, Set<Session>>> entries = maps.entrySet().iterator();
        // 不同用户session
        while (entries.hasNext()) {
            try {
                Map.Entry<String, Set<Session>> entry = entries.next();
                String key = entry.getKey();
                Set<Session> set = entry.getValue();
                Iterator<Session> it = set.iterator();
                int count = 0;
                List<String> dataList = new ArrayList<String>();
                // 同一用户多session
                while (it.hasNext()) {
                    Session session = it.next();
                    // 仅处理第一条session即可，其他一样
                    if (count == 0) {
                        String wsMessage = session.getUpgradeRequest().getParameterMap().get("wsMessage").get(0);
                        String[] strs = wsMessage.split(",");
                        for (String str : strs) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("wsMessage", str);
                            websocketService = JNDIHelper.getJNDIServiceForName(IWebsocketService.class.getName(), map);
                            JSONObject jsonObject = websocketService.getData(key);
                            if (jsonObject.length() > 0) {
                                String data = jsonObject.toString();
                                dataList.add(data);
                            }
                        }
                    }
                    // 存在新消息主动推送
                    for (String data : dataList) {
                        session.getRemote().sendString(data);
                    }
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
