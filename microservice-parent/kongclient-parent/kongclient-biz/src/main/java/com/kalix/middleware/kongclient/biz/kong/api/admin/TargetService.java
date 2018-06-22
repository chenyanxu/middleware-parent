package com.kalix.middleware.kongclient.biz.kong.api.admin;

import com.kalix.middleware.kongclient.biz.kong.model.admin.target.Target;
import com.kalix.middleware.kongclient.biz.kong.model.admin.target.TargetList;

/**
 * Created by vaibhav on 13/06/17.
 */
@Deprecated
public interface TargetService {
    Target createTarget(String upstreamNameOrId, Target request);
    Target deleteTarget(String upstreamNameOrId, String target);
    TargetList listTargets(String upstreamNameOrId, String id, Integer weight, String target, Long size, String offset);
    TargetList listActiveTargets(String upstreamNameOrId);
}
