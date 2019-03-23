package com.kalix.middleware.wechat.pay.api.model;

public class WechatPayInfo {
    // 商品名
    private String commodityName;
    // 总价
    private String totalPrice;
    // 当前页面所在的浏览器URL全路径,由于该支付为jssdk支付，所以需要url地址.参与后台sign签名
    private String clientUrl;
    // 关注人的openId
    private String openId;
    // 服务端Url路径(http://sun.ngrok.xiaomiqiu.cn/Weixin/pay)
    private String notifyPath;
    // 成功响应的path(http://sun.ngrok.xiaomiqiu.cn/Weixin/paysuccess?totalPrice=：totalPrice)
    private String successPath;
    // 客户端Ip(request.getRemoteAddr())
    private String billCreateIp;
    // 用于微信版本判断request.getHeader("user-agent");
    private String userAgent;

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNotifyPath() {
        return notifyPath;
    }

    public void setNotifyPath(String notifyPath) {
        this.notifyPath = notifyPath;
    }

    public String getSuccessPath() {
        return successPath;
    }

    public void setSuccessPath(String successPath) {
        this.successPath = successPath;
    }

    public String getBillCreateIp() {
        return billCreateIp;
    }

    public void setBillCreateIp(String billCreateIp) {
        this.billCreateIp = billCreateIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
