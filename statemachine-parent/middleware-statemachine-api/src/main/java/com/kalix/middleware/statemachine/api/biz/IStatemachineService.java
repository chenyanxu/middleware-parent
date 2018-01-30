package com.kalix.middleware.statemachine.api.biz;

import java.io.InputStream;

/**
 * Created by zangyanming on 2016/12/29.
 */
public interface IStatemachineService {
    void initFSM(InputStream is, String initState);
    Object processFSM(String newState);
    String getCurrentState();
}
