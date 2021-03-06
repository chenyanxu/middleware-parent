# 说明
  本模块主要功能是用于封装了oauth2的服务,参考地址 https://github.com/ameizi/oltu-oauth2-example
# 备注
1. 先注册应用

http://localhost:8181/oauth2/client/list.jsp

会生成client_id和client_secret 这两个以后会用到

client_id               c1ebe466-1cdc-4bd3-ab69-77c3561b9dee    应用id
client_secret           d8346ea2-6017-43ed-ad68-19c0f971738b    应用secret

2. 请求授权码

http://localhost:8181/oauth2/authorize?client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee&response_type=code&redirect_uri=http://localhost:8181/oauth2/accesstoken.jsp

参数说明

client_id               应用id
response_type           返回授权码的标识
redirect_uri            回调地址

上面的网站会打开oauth server的用户登录页面。用户输入正确的用户名和密码以POST方式提交后会重定向到用户所填的回调地址并在地址后携带授权码.

请求成功后会返回如下的页面:

http://aimeizi.net/?code=63910432da9186b22b1ad888d55ae8ae

这里code=63910432da9186b22b1ad888d55ae8ae 即授权码

3. 换取accessToken (POST操作)

首先GET方式请求http://localhost:8181/autho2/access会打开一个表单在该表单中填入必填项，具体表单参数详见说明部分

表单将会以POST方式提交到http://localhost:8181/autho2/accessToken,最终返回accessToken

需要以POST方式提交以下参数换取accessToken

client_id       c1ebe466-1cdc-4bd3-ab69-77c3561b9dee            应用id
client_secret   d8346ea2-6017-43ed-ad68-19c0f971738b            应用secret
grant_type      authorization_code                              用于传递授权码的参数名authorization_code
code            63910432da9186b22b1ad888d55ae8ae                用户登录授权后的授权码
redirect_uri    http://aimeizi.net                              回调地址

最终返回如下数据

{"expires_in":3600,"access_token":"223ae05dfbb0794396fb60a0960c197e"}

4. 测试accessToken

http://localhost:8181/oauth2/client?access_token=223ae05dfbb0794396fb60a0960c197e

测试ok的话返回用户名信息,access_token=223ae05dfbb0794396fb60a0960c197e 为上一步获取的access_token

注：其中的参数名不要随意更改，固定写法。

# demo 例子
com.kalix.middleware.oauth.client.UrlClient

```
    public static void main(String[] args) throws Exception {
        String authCode = getAuthCode();
        String accessToken = getAccessToken(authCode);
        getService(accessToken);
    }

```

result is 
```
http://aimeizi.net?code=c83304c8010c288079c6554664b2c8aa
{"access_token":"a91eae3db4241432cf4ff2ca9f96afd0","expires_in":3600}
a91eae3db4241432cf4ff2ca9f96afd0
demo address is :http://localhost:8181/oauth2/clientdemo?access_token=a91eae3db4241432cf4ff2ca9f96afd0
[{"clientName":"chapter17-client","clientId":"c1ebe466-1cdc-4bd3-ab69-77c3561b9dee","clientSecret":"d8346ea2-6017-43ed-ad68-19c0f971738b","id":1,"version_":1},{"clientName":"hello1212","clientId":"684e6ebc-fb6b-4f1c-8897-75b094960062","clientSecret":"57da96d8-2d32-4e52-b271-957e450fc38a","id":5206,"version_":2,"creationDate":"Apr 19, 2017 12:50:47 PM","updateDate":"Apr 19, 2017 3:09:25 PM"},{"clientName":"hello","clientId":"8e0edce2-3cd7-486a-9341-ebe374301778","clientSecret":"f33a4607-6454-41af-8c10-acbc6b6aaa23","id":5208,"version_":1,"creationDate":"Apr 19, 2017 4:33:21 PM","updateDate":"Apr 19, 2017 4:33:21 PM"}]
```

# oauth2 参考
http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html

# 备注
本次实现缺少对scope，授权范围的支持。