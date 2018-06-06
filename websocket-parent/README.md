# websocket-parent

kalix websocket消息推送功能

## 后台服务

Servlet基类服务WebSocketServlet：实现前后台握手，建立长连接，连接成功后记录session会话

IWebsocketService接口：定义获取数据方法。实现在具体业务中，如common/message/biz/xml
```
    <service interface="com.kalix.middleware.websocket.api.biz.IWebsocketService"
             ref="beanServiceImpl">
        <service-properties>
            <entry key="wsMessage" value="CommonMessage"/>
        </service-properties>
    </service>
```

websocket-servlet.xml配置文件：配置请求地址"/websocket"

请求参数："?loginname=xxx&wsMessage=CommonMessage,xxx"

## 前台使用

访问地址："ws://localhost:8181/websocket?loginname=xxx&wsMessage=CommonMessage,xxx"
