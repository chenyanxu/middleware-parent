package com.kalix.middleware.statemachine.api.biz;

import java.io.InputStream;

/**
 * Created by zangyanming on 2016/12/29.
 */
public interface IStatemachineService {
    Object processFSM(InputStream is, String oldState, String newState);
    String getCurrentState();
}
