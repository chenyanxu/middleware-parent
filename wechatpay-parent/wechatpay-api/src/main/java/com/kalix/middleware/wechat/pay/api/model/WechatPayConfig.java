package com.kalix.middleware.wechat.pay.api.model;

public class WechatPayConfig {
    private String mchId;
    private String mchApiKey;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchApiKey() {
        return mchApiKey;
    }

    public void setMchApiKey(String mchApiKey) {
        this.mchApiKey = mchApiKey;
    }
}
