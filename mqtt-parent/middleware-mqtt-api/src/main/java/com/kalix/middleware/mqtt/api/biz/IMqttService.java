package com.kalix.middleware.mqtt.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface IMqttService extends IService {

    void sendMessage(List<String> receivers, String msg);

    void sendMessage(String sender, String senderPass, List<String> receivers, String msg);
}
