package com.kalix.middleware.xmpp.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.middleware.xmpp.api.biz.IXmppMsgService;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by Administrator on 2016/12/1.
 */
public class XmppMsgServiceImpl implements IXmppMsgService {

    @Override
    public JsonStatus sendMessage(String tousers, String msg) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            String xmppAdmin = (String)ConfigUtil.getConfigProp("XMPP_ADMIN","ConfigXMPP");
            String pass = (String)ConfigUtil.getConfigProp("XMPP_ADMINPASS","ConfigXMPP");
            jsonStatus = sendMessage(xmppAdmin,pass,tousers,msg);
        }
        catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg(e.getMessage());
        }
        return jsonStatus;
    }

    @Override
    public JsonStatus sendMessage(String fromuser, String fromuserpass, String tousers, String msg) {

        JsonStatus jsonStatus = new JsonStatus();
        try {
            // 创建一个到jabber.org服务器指定端口的连接
            /*XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("admin", "hanling")
                .setServiceName("node1.cluster.kalix.com")
                .setHost("125.222.244.22").setDebuggerEnabled(true)
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

            // Connect to the server
            AbstractXMPPConnection connection = new XMPPTCPConnection(config);
            xmppManager.setConnection(connection);*/

            // Create the configuration for this new connection
            String xmppHost = (String)ConfigUtil.getConfigProp("XMPP_HOST","ConfigXMPP");
            Integer xmppPort = (Integer)ConfigUtil.getConfigProp("XMPP_PORT","ConfigXMPP");
            String xmppServiceName = (String)ConfigUtil.getConfigProp("XMPP_SERVICENAME","ConfigXMPP");
            ConnectionConfiguration connConfig = new ConnectionConfiguration(xmppHost, xmppPort, xmppServiceName);
            //connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
            //connConfig.setDebuggerEnabled(true);
            //connConfig.setReconnectionAllowed(false);
            connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            connConfig.setCompressionEnabled(false);
            connConfig.setDebuggerEnabled(false);
            XMPPConnection connection = new XMPPTCPConnection(connConfig);
            connection.connect();
            // Log into the server
            String username = fromuser + "@" + xmppServiceName;
            //connection.login("admin@node1.cluster.kalix.com", "hanling", "Work");
            connection.login(username, fromuserpass);

            String userjid = tousers + "@" + xmppServiceName;
            //userjid = "sunlf@node1.cluster.kalix.com";
            Chat chat = ChatManager.getInstanceFor(connection).createChat(userjid, new MessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            System.out.println("Receivedmessage:" + message);
                        }
                    });
            chat.sendMessage("hello wendy!");

            if (connection != null)
                connection.disconnect();

            jsonStatus.setSuccess(true);
            jsonStatus.setMsg("消息发送成功!");
        }
        catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setMsg(e.getMessage());
        }
        return jsonStatus;
    }
}
