package com.kalix.middleware.excel.api.biz;

import com.kalix.framework.core.api.persistence.JsonData;

/**
 * Created by Administrator on 2018/6/15.
 */
public interface IWordService {
    JsonData reviewWordAtt(String attId, String attName, String attPath);
}
