package com.kalix.middleware.wechat.biz;

import com.google.gson.JsonObject;
import com.kalix.framework.core.api.cache.ICacheManager;
import com.kalix.middleware.wechat.api.biz.IWechatCoreService;
import com.kalix.middleware.wechat.api.model.AccessToken;
import com.kalix.middleware.wechat.api.model.WeixinConfig;
import com.kalix.middleware.wechat.api.utils.WeixinUtils;
import org.apache.http.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WechatCoreServiceImpl implements IWechatCoreService {

    private WeixinConfig config = null;
    private ICacheManager cacheManager;

    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public WechatCoreServiceImpl() {
        config = WeixinUtils.getWexinConfig();
    }

    public boolean checkSignature(String signature,String timestamp,String nonce) {
        String[] arr = new String[]{config.getToken(),timestamp,nonce};
        //字典排序
        Arrays.sort(arr);

        //生成字符串
        StringBuffer content = new StringBuffer();
        for(int i=0;i<arr.length;i++){
            content.append(arr[i]);
        }

        //sha1加密
        String temp = WeixinUtils.getSha1(content.toString());

        return temp.equals(signature);
    }

    public String getWxAccessToken() {
        String wxCacheAccessToken = (String)cacheManager.get("wx_access_token");
        String wxAccessToken = "";
        long wxExpiresTime = 0L;
        if (wxCacheAccessToken == null || wxCacheAccessToken.isEmpty()) {
            wxAccessToken = cacheAccessToken();
        } else {
            String expiresStr = cacheManager.get("wx_expires_in");
            wxExpiresTime = Long.parseLong(expiresStr);
            long nowTime = System.currentTimeMillis();
            if (nowTime >= wxExpiresTime) {
                wxAccessToken = cacheAccessToken();
            } else {
                wxAccessToken = wxCacheAccessToken;
            }
        }
        return wxAccessToken;
    }

    public AccessToken getAccessToken() throws ParseException, IOException {
        String ACCESS_TOKEN_URL = WeixinUtils.ACCESS_TOKEN_URL;
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", config.getAppId()).replace("APPSECRET", config.getAppSecret());
        JsonObject jsonObject = WeixinUtils.doGetStr(url);
        if(jsonObject!=null){
            token.setToken(jsonObject.get("access_token").getAsString());
            token.setExpiresIn(jsonObject.get("expires_in").getAsInt());
        }
        return token;
    }


    private String cacheAccessToken() {
        try {
            AccessToken accessToken = this.getAccessToken();
            String wxAccessToken = accessToken.getToken();
            int wxExpiresIn = accessToken.getExpiresIn();
            cacheManager.save("wx_access_token", wxAccessToken);
            long nowTime = System.currentTimeMillis();
            long expiresTime = nowTime + (wxExpiresIn*1000);
            String expiresTimeStr = String.valueOf(expiresTime);
            cacheManager.save("wx_expires_in", expiresTimeStr);
            return wxAccessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> createMenu(String token, String menu) throws ParseException, IOException {
        String CREATE_MENU_URL = WeixinUtils.CREATE_MENU_URL;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JsonObject jsonObject = WeixinUtils.doPostStr(url, menu);
        Map<String, Object> map = null;
        if(jsonObject != null) {
            map = new HashMap<>();
            int result = jsonObject.get("errcode").getAsInt();
            String msg = jsonObject.get("errmsg").getAsString();
            map.put("errcode", result);
            map.put("errmsg", msg);
        }
        return map;
    }

    public JsonObject queryMenu(String token) throws ParseException, IOException {
        String QUERY_MENU_URL = WeixinUtils.QUERY_MENU_URL;
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
        JsonObject jsonObject = WeixinUtils.doGetStr(url);
        return jsonObject;
    }

    public int deleteMenu(String token) throws ParseException, IOException {
        String DELETE_MENU_URL = WeixinUtils.DELETE_MENU_URL;
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JsonObject jsonObject = WeixinUtils.doGetStr(url);
        int result = 0;
        if(jsonObject != null){
            result = jsonObject.get("errcode").getAsInt();
        }
        return result;
    }


    public String getOpenid(String code) throws ParseException, IOException {
        String openid = null;
        String OPENID_URL = WeixinUtils.OPENID_URL;
        String url = OPENID_URL.replace("APPID", config.getAppId()).replace("APPSECRET", config.getAppSecret()).replace("CODE", code);
        // 发起GET请求获取凭证
//        JsonObject jsonObject = httpsRequest(requestUrl, "GET", null);
        JsonObject jsonObject = WeixinUtils.doGetStr(url);

        if (null != jsonObject) {
            try {
                openid = jsonObject.get("openid").getAsString();
            } catch (Exception e) {
                openid = null;
                e.printStackTrace();
                // 获取openid失败
                //log.error("获取openid失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return openid;
    }
}
