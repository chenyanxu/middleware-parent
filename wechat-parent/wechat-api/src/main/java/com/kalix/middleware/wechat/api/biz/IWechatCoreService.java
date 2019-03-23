package com.kalix.middleware.wechat.api.biz;

import com.google.gson.JsonObject;
import com.kalix.framework.core.api.IService;
import com.kalix.middleware.wechat.api.model.AccessToken;
import org.apache.http.ParseException;

import java.io.IOException;
import java.util.Map;

public interface IWechatCoreService extends IService {
    /**
     * 微信接入验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public boolean checkSignature(String signature,String timestamp,String nonce);

    /**
     * url获取微信access_token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public AccessToken getAccessToken() throws ParseException, IOException;

    /**
     * 缓存处理获取微信access_token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public String getWxAccessToken();
    /**
     * 创建微信菜单
     * @param token
     * @param menu
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public Map<String, Object> createMenu(String token, String menu) throws ParseException, IOException;

    /**
     * 查询微信菜单
     * @param token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public JsonObject queryMenu(String token) throws ParseException, IOException;

    /**
     * 删除微信菜单
     * @param token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public int deleteMenu(String token) throws ParseException, IOException;

    /**
     * 获取openid
     *
     * @param code 凭证
     * @return
     */
    public String getOpenid(String code) throws ParseException, IOException;
}
