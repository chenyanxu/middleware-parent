package com.kalix.middleware.xml.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface IXmlService extends IService {

    void marshal();

    void unmarshal();
}
