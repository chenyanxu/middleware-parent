package com.kalix.middleware.mqtt.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;

import java.util.List;

/**
 * Created by sunlf on 2018/12/10.
 */
public interface IMqttService extends IService {
    /**
     * 发布一个消息在指定的topic上
     * @param topic
     * @param message
     */
    void publish(String topic, String message);

    /**
     * 订阅一个主题
     * @param topic
     */
    void subscribe(String topic);
}
