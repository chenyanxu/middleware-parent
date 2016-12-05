package com.kalix.middleware.xmpp.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface IXmppMsgService extends IService {

    void sendMessage(List<String> toUsers, String msg);

    void sendMessage(String fromUser, String fromUserPass, List<String> toUsers, String msg);
}
