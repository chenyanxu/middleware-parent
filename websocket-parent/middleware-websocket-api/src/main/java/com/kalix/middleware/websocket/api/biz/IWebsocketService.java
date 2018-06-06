package com.kalix.middleware.websocket.api.biz;

import org.json.JSONObject;

/**
 * @author hqj
 */
public interface IWebsocketService {
    JSONObject getData(String key);
}
