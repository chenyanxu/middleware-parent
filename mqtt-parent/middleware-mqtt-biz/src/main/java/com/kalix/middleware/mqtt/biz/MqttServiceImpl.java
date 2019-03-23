package com.kalix.middleware.mqtt.biz;

import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.SystemUtil;
import com.kalix.middleware.mqtt.api.biz.IMqttService;
import org.apache.log4j.Logger;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.fusesource.mqtt.codec.MQTTFrame;

/**
 * Created by sunlf on 2018/12/10.
 */
public class MqttServiceImpl implements IMqttService {
    private static final String CONFIG_MQTT_DB = "ConfigMqtt";
    private final Logger logger = Logger.getLogger(this.getClass());
    private MQTT mqtt = null;
    //使用回调式API
    private CallbackConnection callbackConnection = null;
    private String host, user, password;
    private int port;

    public MqttServiceImpl() {
        initConnection();
    }

    @Override
    public void publish(String topic, String message) {
        try {
            //发布消息
            callbackConnection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, true, new Callback<Void>() {
                public void onSuccess(Void v) {
                    logger.info("===========消息发布成功============" + message);
                }

                public void onFailure(Throwable value) {
                    logger.info("========消息发布失败=======" + message);
                    callbackConnection.disconnect(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(String topic) {
        try {


            //连接
            callbackConnection.connect(new Callback<Void>() {

                //连接失败
                public void onFailure(Throwable value) {
                    logger.info("============连接失败：" + value.getLocalizedMessage() + "============");
                }

                // 连接成功
                @Override
                public void onSuccess(Void v) {


                    //订阅主题
                    Topic[] topics = {new Topic(topic, QoS.AT_LEAST_ONCE)};
                    callbackConnection.subscribe(topics, new Callback<byte[]>() {
                        @Override
                        public void onSuccess(byte[] o) {
                            logger.info("========订阅成功=======");
                        }

                        //订阅主题失败
                        public void onFailure(Throwable value) {
                            logger.info("========订阅失败=======");
                            callbackConnection.disconnect(null);
                        }
                    });
                }
            });

            //连接监听
            callbackConnection.listener(new Listener() {

                //接收订阅话题发布的消息
                @Override
                public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
                    logger.info("=============receive msg================" + new String(payload.toByteArray()));
                    ack.run();
                }

                //连接失败
                @Override
                public void onFailure(Throwable value) {
                    logger.info("===========connect failure===========");
                    callbackConnection.disconnect(null);
                }

                //连接断开
                @Override
                public void onDisconnected() {
                    logger.info("====mqtt disconnected=====");

                }

                //连接成功
                @Override
                public void onConnected() {
                    logger.info("====mqtt connected=====");

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readConfig() {
        host = (String) ConfigUtil.getConfigProp("HOST", CONFIG_MQTT_DB);
        port = Integer.parseInt((String) ConfigUtil.getConfigProp("PORT", CONFIG_MQTT_DB));
        user = (String) ConfigUtil.getConfigProp("USER", CONFIG_MQTT_DB);
        password = (String) ConfigUtil.getConfigProp("PASSWORD", CONFIG_MQTT_DB);
    }

    private void initConnection() {
        readConfig();
        try {
            mqtt.setHost(host, port);
            mqtt.setUserName(user);
            mqtt.setPassword(password);
            mqtt = new MQTT();
            //使用回调式API
            callbackConnection = mqtt.callbackConnection();
            //设置跟踪器
            mqtt.setTracer(new Tracer() {
                @Override
                public void onReceive(MQTTFrame frame) {
                    logger.debug("recv: " + frame);
                }

                @Override
                public void onSend(MQTTFrame frame) {
                    logger.debug("send: " + frame);
                }

                @Override
                public void debug(String message, Object... args) {
                    logger.debug(String.format("debug: " + message, args));
                }
            });
            SystemUtil.succeedPrintln("succeed connect to mqtt! IP address is " + host);
        } catch (Exception e) {
            SystemUtil.errorPrintln("can not connect to mqtt address " + host + "!");
            e.printStackTrace();
        }
    }
}
