package com.kalix.middleware.wechat.pay.biz;

import com.kalix.middleware.wechat.api.biz.IWechatCoreService;
import com.kalix.middleware.wechat.api.model.WeixinConfig;
import com.kalix.middleware.wechat.api.utils.WeixinUtils;
import com.kalix.middleware.wechat.pay.api.model.RefundInfo;
import com.kalix.middleware.wechat.pay.api.model.WechatPayInfo;
import com.kalix.middleware.wechat.pay.api.biz.IWechatPayService;
import com.kalix.middleware.wechat.pay.api.model.WechatPayConfig;
import com.kalix.middleware.wechat.pay.biz.utils.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class WechatPayServiceImpl implements IWechatPayService {

    private WeixinConfig config = null;
    private WechatPayConfig payConfig = null;
    private IWechatCoreService wechatCoreService;

    public void setWechatCoreService(IWechatCoreService wechatCoreService) {
        this.wechatCoreService = wechatCoreService;
    }

    public WechatPayServiceImpl() {
        config = WeixinUtils.getWexinConfig();
        payConfig = WechatPayUtils.getWechatPayConfig();
    }

    public String goPay(WechatPayInfo wechatPayInfo) {
        if (wechatPayInfo == null) {
            return null;
        }
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        SortedMap<Object, Object> configMap = new TreeMap<Object, Object>();
        parameters.put("appid", config.getAppId());
        parameters.put("mch_id", payConfig.getMchId());
        parameters.put("nonce_str", WechatPayUtils.getNonceStr());
        parameters.put("body", wechatPayInfo.getCommodityName());// 商品名称

        /** 当前时间 yyyyMMddHHmmss */
        String currTime = WechatPayUtils.getCurrTime();
        /** 8位日期字符串 */
        String strTime = currTime.substring(8, currTime.length());
        /** 四位随机数 */
        String strRandom = WechatPayUtils.buildRandom(4) + "";
        /** 订单号 */
        parameters.put("out_trade_no", strTime + strRandom);
        String outTradeNo = strTime + strRandom;
        System.out.println("weixin--outTradeno=========:" +  outTradeNo);
        /** 订单金额以分为单位，只能为整数 */
        parameters.put("total_fee", wechatPayInfo.getTotalPrice());
        /** 客户端本地ip */
        parameters.put("spbill_create_ip", wechatPayInfo.getBillCreateIp());
        /** 支付回调地址 */
        parameters.put("notify_url", wechatPayInfo.getNotifyPath());
        /** 支付方式为JSAPI支付 */
        parameters.put("trade_type", "JSAPI");
        /** 用户微信的openid，当trade_type为JSAPI的时候，该属性字段必须设置 */
        parameters.put("openid", wechatPayInfo.getOpenId());

        /** 使用MD5进行签名，编码必须为UTF-8 */
        String sign = createSign_ChooseWXPay("UTF-8", parameters);

        /**将签名结果加入到map中，用于生成xml格式的字符串*/
        parameters.put("sign", sign);

        /** 生成xml结构的数据，用于统一下单请求的xml请求数据 */
        String requestXML = getRequestXml(parameters);
        System.out.println("requestXML：" + requestXML);
        // log.info("requestXML：" + requestXML);

        /** 使用POST请求统一下单接口，获取预支付单号prepay_id */
        HttpClient client = new HttpClient();
        PostMethod myPost = new PostMethod(WechatPayUtils.UNI_URL);
        client.getParams().setSoTimeout(300 * 1000);
        String result = null;
        try {
            myPost.setRequestEntity(new StringRequestEntity(requestXML, "text/xml", "utf-8"));
            int statusCode = client.executeMethod(myPost);
            if (statusCode == HttpStatus.SC_OK) {
                //使用流的方式解析微信服务器返回的xml结构的字符串
                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
                byte[] bytes = new byte[1024];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int count = 0;
                while ((count = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, count);
                }
                byte[] strByte = bos.toByteArray();
                result = new String(strByte, 0, strByte.length, "utf-8");
                bos.close();
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** 需要释放掉、关闭连接 */
        myPost.releaseConnection();
        client.getHttpConnectionManager().closeIdleConnections(0);

        System.out.println("请求统一支付返回:" + result);
        String json = "";

        try {
            /** 解析微信返回的信息，以Map形式存储便于取值 */
            Map<String, String> map = XMLUtil.doXMLParse(result);
            System.out.println("预支付单号prepay_id为:" + map.get("prepay_id"));
//			log.info("预支付单号prepay_id为:" + map.get("prepay_id"));

            /**全局map，该map存放前端ajax请求的返回值信息，包括wx.config中的配置参数值，也包括wx.chooseWXPay中的配置参数值*/
            SortedMap<Object, Object> params = new TreeMap<Object, Object>();
            params.put("appId", config.getAppId());
            params.put("timeStamp", new Date().getTime()); //时间戳
            params.put("nonceStr", WechatPayUtils.getNonceStr()); //随机字符串
            params.put("package", "prepay_id=" + map.get("prepay_id")); //主意格式必须为 prepay_id=***
            params.put("signType", "MD5"); //签名的方式必须是MD5
            /**
             *获取预支付prepay_id后，需要再次签名，此次签名是用于前端js中的wx.chooseWXPay中的paySign。
             * 参与签名的参数有5个，分别是：appId、timeStamp、nonceStr、 package、signType 注意参数名称的大小写
             */
            String paySign = createSign_ChooseWXPay("UTF-8", params);
            /** 预支付单号 */
            params.put("packageValue", "prepay_id=" + map.get("prepay_id"));
            params.put("paySign", paySign); //支付签名
            /** 付款成功后同步请求的URL，请求我们自定义的支付成功的页面，展示给用户 */
            params.put("sendUrl", wechatPayInfo.getSuccessPath());
            /** 获取用户的微信客户端版本号，用于前端支付之前进行版本判断，微信版本低于5.0无法使用微信支付 */
            String userAgent = wechatPayInfo.getUserAgent();
            char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
            params.put("agent", new String(new char[] { agent }));

            /**使用JSSDK支付，需要另一个凭证，也就是ticket。这个是JSSDK中使用到的。*/
            String jsapi_ticket = "";
            /**获取ticket，需要token作为参数传递,由于token有有效期限制，和调用次数限制，你可以缓存到session或者数据库中.有效期设置为小于7200秒*/
            // String token = wechatCoreService.getWxAccessToken();
            String token = wechatCoreService.getAccessToken().getToken();
//            String token = TokenUtil.getAccessToken().getAccessToken();
            System.out.println("获取的token值为:" + token);
//			log.info("获取的token值为:" + token);
            /**获取ticket的请求URL，需要token作为参数*/
            // String getTicket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + token;
            String ticketUrl = WechatPayUtils.TICKET_URL;
            String getTicket = ticketUrl.replace("ACCESSTOKEN", token);
            System.out.println("接口调用凭证ticket：" + getTicket);
//			log.info("接口调用凭证ticket：" + getTicket);

            TenpayHttpClient httpClient = new TenpayHttpClient();
            httpClient.setMethod("GET");
            httpClient.setReqContent(getTicket);
            if (httpClient.call()) {
                System.out.println("获取ticket成功");
//				log.info("获取ticket成功");
                String resContent = httpClient.getResContent();
                System.out.println("resContent：" + resContent);
//				log.info("resContent：" + resContent);
                jsapi_ticket = JsonUtil.getJsonValue(resContent, "ticket");
                System.out.println("jsapi_ticket：" + jsapi_ticket);
//				log.info("jsapi_ticket：" + jsapi_ticket);
            }
            // 获取到ticket凭证之后，需要进行一次签名
            String config_nonceStr = WechatPayUtils.getNonceStr();// 获取随机字符串
            long config_timestamp = new Date().getTime();// 时间戳
            // 加入签名的参数有4个，分别是： noncestr、jsapi_ticket、timestamp、url，注意字母全部为小写
            configMap.put("noncestr", config_nonceStr);
            configMap.put("jsapi_ticket", jsapi_ticket);
            configMap.put("timestamp", config_timestamp);
            configMap.put("url", wechatPayInfo.getClientUrl());

            //该签名是用于前端js中wx.config配置中的signature值。
            String config_sign = createSign_wx_config("UTF-8", configMap);

            // 将config_nonceStr、jsapi_ticket 、config_timestamp、config_sign一同传递到前端
            // 这几个参数名称和上面获取预支付prepay_id使用的参数名称是不一样的，不要混淆了。
            // 这几个参数是提供给前端js代码在调用wx.config中进行配置的参数，wx.config里面的signature值就是这个config_sign的值，以此类推
            params.put("config_nonceStr", config_nonceStr);
            params.put("config_timestamp", config_timestamp);
            params.put("config_sign", config_sign);
            params.put("config_outTradeNo", outTradeNo);

            // 将map转换为json字符串，传递给前端ajax回调
//            json = JSONArray.fromObject(params).toString();
            json = JsonUtil.toJson(params);
            System.out.println("用于wx.config配置的json：" + json);
//			log.info("用于wx.config配置的json：" + json);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String goRefund(RefundInfo refundInfo) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", config.getAppId());
        parameters.put("mch_id", payConfig.getMchId());
        parameters.put("nonce_str", WechatPayUtils.getNonceStr());
        parameters.put("op_user_id", payConfig.getMchId());
        parameters.put("out_trade_no", refundInfo.getOut_trade_no());
        parameters.put("out_refund_no", refundInfo.getOut_refund_no());
        parameters.put("refund_fee", refundInfo.getRefund_fee());
        parameters.put("total_fee", refundInfo.getTotal_fee());
        String sign = createSign_ChooseWXPay("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = getRequestXml(parameters);
        System.out.println("requestXML：" + requestXML);
        String json = "";
        try {
            // 发送请求
            CloseableHttpResponse resp = HttpClientUtil.Post(WechatPayUtils.REFUND_URL, requestXML, true);
            // 关闭流
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            System.out.println("refund json:===========");
            System.out.println(jsonStr);
            Map<String, String> map = XMLUtil.doXMLParse(jsonStr);
            SortedMap<Object, Object> params = new TreeMap<Object, Object>();
            params.put("appId", config.getAppId()); // 公众账号ID
            params.put("mch_id", payConfig.getMchId()); // 商户号
            params.put("transaction_id", map.get("transaction_id")); // 微信订单号
            params.put("refund_id", map.get("refund_id")); // 微信退款单号
            params.put("out_trade_no", map.get("out_trade_no")); // 商户订单号
            params.put("out_refund_no", map.get("out_refund_no")); // 商户退款单号
            params.put("return_code", map.get("return_code")); // 返回状态码
            params.put("return_msg", map.get("return_msg")); // 返回信息
            params.put("result_code", map.get("result_code")); // 业务结果
            params.put("refund_fee", map.get("refund_fee")); // 退款金额，单位为分，可做部分退款
            params.put("total_fee", map.get("total_fee")); // 订单总金额，单位为分，只能整数
            params.put("sendUrl", refundInfo.getSuccessPath()); // 成功跳转地址
            json = JsonUtil.toJson(params);
            EntityUtils.consume(resp.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * sign签名，必须使用MD5签名，且编码为UTF-8
     *
     * 作者: zhoubang 日期：2015年6月10日 上午9:31:24
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public String createSign_ChooseWXPay(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<Object, Object>> es = parameters.entrySet();
        Iterator<Map.Entry<Object, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        /** 支付密钥必须参与加密，放在字符串最后面 */
        sb.append("key=" + payConfig.getMchApiKey());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 将请求参数转换为xml格式的string字符串，微信服务器接收的是xml格式的字符串
     *
     * 作者: zhoubang 日期：2015年6月10日 上午9:25:51
     *
     * @param parameters
     * @return
     */
    public String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<Map.Entry<Object, Object>> es = parameters.entrySet();
        Iterator<Map.Entry<Object, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
            String k = (String) entry.getKey();
            String v = String.valueOf(entry.getValue());
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    // SHA1加密，该加密是对wx.config配置中使用到的参数进行SHA1加密，这里不需要key参与加密
    public static String createSign_wx_config(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<Object, Object>> es = parameters.entrySet();
        Iterator<Map.Entry<Object, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        String str = sb.toString();
        String sign = Sha1Util.getSha1(str.substring(0, str.length() - 1));
        return sign;
    }
}
