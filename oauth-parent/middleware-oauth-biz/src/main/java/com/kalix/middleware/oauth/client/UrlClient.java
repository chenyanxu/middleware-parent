package com.kalix.middleware.oauth.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sunlf on 2017-04-14.
 * oauth2的client测试代码，演示验证的整个过程
 */
public class UrlClient {
    private static String getAuthCode() throws Exception {

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("username", ClientParams.USERNAME);
        params.put("password", ClientParams.PASSWORD);
        params.put("client_id", ClientParams.CLIENT_ID);
        params.put("response_type", ResponseType.CODE.toString());
        params.put("redirect_uri", ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        processUrl(params, postStr);

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(OAuth.HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        connection.setInstanceFollowRedirects(false);// 必须设置该属性
        String location = connection.getHeaderField("Location");
        System.out.println("第一步：请求授权码");
        System.out.println("请求地址:" + ClientParams.OAUTH_SERVER_URL + postStr.toString());

        System.out.println("返回结果:" + location);
        return location.substring(location.indexOf("=") + 1);
    }

    private static void processUrl(Map<String, Object> params, StringBuilder postStr) throws UnsupportedEncodingException {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postStr.length() != 0) {
                postStr.append('&');
            }
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    private static String getAccessToken(String authCode) throws Exception {

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("client_id", ClientParams.CLIENT_ID);
        params.put("client_secret", ClientParams.CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);
        params.put("redirect_uri", ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        processUrl(params, postStr);

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(OAuth.HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new GsonBuilder().create();
        Map<String, String> map = gson.fromJson(response.toString(), Map.class);
        String accessToken = map.get("access_token");
        System.out.println("第二步：换取访问令牌");
        System.out.println("请求地址:" + ClientParams.OAUTH_SERVER_URL + postStr.toString());
        System.out.println("返回结果:" + response.toString());
        return accessToken;
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    private static void getService(String accessToken) throws Exception {
        String uri = ClientParams.OAUTH_SERVICE_API + "?access_token=" + accessToken;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(OAuth.HttpMethod.GET);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("第三步：测试accessToken");
        System.out.println("请求地址:" + uri);
        System.out.println("返回结果:" + response.toString());
    }


    public static void main(String[] args) throws Exception {
        String authCode = getAuthCode();
        String accessToken = getAccessToken(authCode);
        getService(accessToken);
    }
}
