package com.kalix.middleware.statemachine.biz;

import com.kalix.middleware.statemachine.api.biz.IStatemachineService;
import com.kalix.middleware.statemachine.core.action.FSMAction;
import com.kalix.middleware.statemachine.core.fsm.FSM;
import com.kalix.middleware.statemachine.core.states.FSMStateList;
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
    public Object processFSM(InputStream is, String oldState, String newState) {
        OurFSMAction ourFSMAction = new OurFSMAction();
        try {
            this.fsm = new FSM(is, oldState, ourFSMAction);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return this.fsm.ProcessFSM(newState);
    }

    @Override
    public String getCurrentState(){
        return this.fsm.getCurrentState();
    }
}

