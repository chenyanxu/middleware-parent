package com.kalix.middleware.statemachine.api.biz;

import java.io.InputStream;

/**
 * Created by zangyanming on 2016/12/29.
 */
public interface IStatemachineService {

    void initFSM(String fileName, String startState);
    void initFSM(InputStream is, String currentState);
    Object processFSM(String nextState);
    String getCurrentState();
}
