package com.kalix.middleware.wechat.pay.biz.utils;

import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.middleware.wechat.pay.api.model.WechatPayConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WechatPayUtils {
    // 微信API
    private final static String weixinConfig = "ConfigWechat";
    // 统一下单接口
    public final static String UNI_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // ticket
    public final static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=ACCESSTOKEN";
    // 证书地址
    public final static String CERT_PATH = "D:/java-develop/project/middleware-parent/apiclient_cert.p12";
    // 微信退款接口
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public static WechatPayConfig getWechatPayConfig() {
        String mchId = (String) ConfigUtil.getConfigProp("MCHID", weixinConfig);
        String mchApiKey = (String) ConfigUtil.getConfigProp("MCH_APIKEY", weixinConfig);
        WechatPayConfig wechatPayConfig = new WechatPayConfig();
        wechatPayConfig.setMchId(mchId);
        wechatPayConfig.setMchApiKey(mchApiKey);
        return wechatPayConfig;
    }

    /**
     * 获取32位随机字符串
     * @return
     */
    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length
     *            int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }
}
