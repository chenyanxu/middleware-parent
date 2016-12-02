package com.kalix.middleware.xmpp.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface IXmppMsgService extends IService {

    JsonStatus sendMessage(String tousers, String msg);

    JsonStatus sendMessage(String fromuser, String fromuserpass, String tousers, String msg);

}
