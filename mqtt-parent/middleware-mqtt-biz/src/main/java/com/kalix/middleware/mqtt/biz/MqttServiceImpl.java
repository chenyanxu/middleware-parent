package com.kalix.middleware.mqtt.biz;

import com.kalix.middleware.mqtt.api.biz.IMqttService;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class MqttServiceImpl implements IMqttService {

    @Override
    public void sendMessage(List<String> receivers, String msg) {

    }

    @Override
    public void sendMessage(String sender, String senderPass, List<String> receivers, String msg) {


    }
}
