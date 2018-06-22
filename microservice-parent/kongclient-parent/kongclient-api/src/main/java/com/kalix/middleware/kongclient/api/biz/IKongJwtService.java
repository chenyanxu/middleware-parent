package com.kalix.middleware.kongclient.api.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;

public interface IKongJwtService {

    /**
     * 创建kong Gateway,加密采用HS256
     * @return
     */
    public JsonStatus createAPIGateWayHS256();

    public JsonStatus createAPIGateWayRS256();

    /**
     * 清空kong Gateway,加密采用HS256
     * @return
     */
    public JsonStatus clearAPIGateWayHS256();

    public JsonStatus clearAPIGateWayRS256();


    /**
     * 获取HS256 加密的Jwt token
     * @return
     */
    public String getJwtTokenHS256();

    /**
     * 获取RS256 加密的Jwt token
     * @return
     */
    public String getJwtTokenRS256();

}
