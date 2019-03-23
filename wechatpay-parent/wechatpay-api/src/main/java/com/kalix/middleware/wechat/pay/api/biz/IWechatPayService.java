package com.kalix.middleware.wechat.pay.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.middleware.wechat.pay.api.model.RefundInfo;
import com.kalix.middleware.wechat.pay.api.model.WechatPayInfo;

public interface IWechatPayService extends IService {
    // 微信支付
    public String goPay(WechatPayInfo wechatPayInfo);
    // 微信退款
    public String goRefund(RefundInfo refundInfo);
}
