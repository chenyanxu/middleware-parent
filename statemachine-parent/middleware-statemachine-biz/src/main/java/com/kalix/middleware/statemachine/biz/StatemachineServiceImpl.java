package com.kalix.middleware.statemachine.biz;

import com.kalix.middleware.statemachine.api.biz.IStatemachineService;
import com.kalix.middleware.statemachine.core.action.FSMAction;
import com.kalix.middleware.statemachine.core.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zangyanming on 2016/12/29.
 */
public class StatemachineServiceImpl implements IStatemachineService {
    private FSM fsm = null;

    class OurFSMAction extends FSMAction{
        OurFSMAction(){
        }

        @Override
        public boolean action(String curState, String message, String nextState, Object args) {
            return true;
        }
    }

    @Override
    public void initFSM(String fileName, String startState) {
        OurFSMAction ourFSMAction = new OurFSMAction();
        try {
            this.fsm = new FSM(fileName, startState, ourFSMAction);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initFSM(InputStream is, String startState) {
        OurFSMAction ourFSMAction = new OurFSMAction();
        try {
            this.fsm = new FSM(is, startState, ourFSMAction);
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

