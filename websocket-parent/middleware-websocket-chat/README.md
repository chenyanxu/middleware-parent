# websocket-chat
kalix websocket 聊天室消息推送功能

## 修改ip地址
var serviceLocation = "ws://192.168.31.201:8181/kalix-websocket/chat/";
``` 
karaf> web:list
```

## osgi.bnd 文件
```
Bundle-Activator: ${bundle.namespace}.internal.InitActivator
Bundle-Category: Kalix Middleware Websocket
Include-Resource: src/main/webapp,target/classes
Web-ContextPath: kalix-websocket
```

## 参考资料

https://github.com/ops4j/org.ops4j.pax.web/tree/master/samples/websocket-jsr356
https://examples.javacodegeeks.com/enterprise-java/jetty/jetty-websocket-example/


