package com.kalix.middleware.statemachine.biz;

import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.middleware.statemachine.api.biz.IStatemachineService;
import com.kalix.middleware.statemachine.core.action.FSMAction;
import com.kalix.middleware.statemachine.core.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by zangyanming on 2016/12/29.
 */
public class StatemachineServiceImpl<TP extends PersistentEntity> implements IStatemachineService<TP>{
    private FSM fsm = null;

    @Override
    public void initFSM(String fileName, String startState) {
        OurFSMAction ourFSMAction = new OurFSMAction();
        try {
            this.fsm = new FSM(fileName, "INTERMEDIATE", ourFSMAction);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object processFSM(String nextState){
        return this.fsm.ProcessFSM(nextState);
    }

    @Override
    public String getCurrentState(){
        return this.fsm.getCurrentState();
    }

    public static void main(String args[]){
        StatemachineServiceImpl statemachineService = new StatemachineServiceImpl();
        statemachineService.initFSM("C://config.xml","START");
        System.out.println(statemachineService.getCurrentState());
        statemachineService.processFSM("MOVERIGHT");
        System.out.println(statemachineService.getCurrentState());
    }
}

class OurFSMAction extends FSMAction{
    OurFSMAction(){
    }

    @Override
    public boolean action(String curState, String message, String nextState, Object args) {
        return true;
    }
}

