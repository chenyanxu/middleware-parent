package com.kalix.middleware.statemachine.api.biz;

import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.middleware.statemachine.core.fsm.FSM;


/**
 * Created by zangyanming on 2016/12/29.
 */
public interface IStatemachineService <T extends PersistentEntity> {

    void initFSM(String fileName, String startState);
    Object processFSM(String nextState);
    String getCurrentState();
}
