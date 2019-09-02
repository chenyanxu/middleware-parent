package com.kalix.middleware.weixin.biz;

import com.kalix.middleware.weixin.api.biz.IWeixinService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

public class WeixinServiceImpl implements IWeixinService {
    private WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
    private WxCpServiceImpl wxCpService = new WxCpServiceImpl();

    @Override
    public Object getWeixinService() {
//        config.setCorpId("wwcf863b047cd7d972");      // 设置微信企业号的appid
////        config.setCorpSecret("...");
//        config.setCorpSecret("45mGD2dj_SOLgzBOxaqHSBd2kEynkhTo_gSKcrwZ8lk"); // 设置微信企业号的app corpSecret
//        config.setAgentId(1000021);     // 设置微信企业号应用ID
//        config.setToken("gFToTqjSTGhDR8UTjm");       // 设置微信企业号应用的token
//        config.setAesKey("m2HcKfu8zbftz5qYt1biTWsvV8bikti1UxFuiyIhwgt");      // 设置微信企业号应用的EncodingAESKey
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

    public void setConfig(WxCpDefaultConfigImpl config) {
        this.config = config;
    }

    /**
     * 用于测试
     */
    public void sendmessage(){
        //            wxCpService.getMenuService().create(wxMenu);
//            System.out.println(wxCpService.getUserService().getById("Qi").toJson());
        getWeixinService();
        WxCpMessage message = WxCpMessage.TEXT().toUser("SunLingFeng").content("Hello World12131").build();
        try {
            wxCpService.messageSend(message);
            System.out.println(wxCpService.getUserService().getById("SunLingFeng").toJson());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
//            System.out.println(wxCpService.getAccessToken());
    }
}
