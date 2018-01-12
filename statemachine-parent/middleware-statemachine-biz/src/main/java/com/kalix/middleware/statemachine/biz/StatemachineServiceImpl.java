package com.kalix.middleware.statemachine.biz;

import com.kalix.middleware.statemachine.api.biz.IStatemachineService;
import com.kalix.middleware.statemachine.core.action.FSMAction;
import com.kalix.middleware.statemachine.core.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Map;

/**
 * Created by zangyanming on 2016/12/29.
 */
public class StatemachineServiceImpl implements IStatemachineService {
    private FSM fsm = null;

    class OurFSMAction extends FSMAction {
        OurFSMAction() {
        }

        @Override
        public boolean action(String curState, String message, String nextState, Object args) {
            return true;
        }
    }

    @Override
    public void initFSM(InputStream is, String initState) {
        OurFSMAction ourFSMAction = new OurFSMAction();
        try {
            this.fsm = new FSM(is, initState, ourFSMAction);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object processFSM(String newState) {
        Object o = this.fsm.ProcessFSM(newState);
        if (o == null)
            throw new RuntimeException("new state not found");
        return o;
    }

    @Override
    public String getCurrentState() {
        return this.fsm.getCurrentState();
    }

    public static void main(String args[]) {
        StatemachineServiceImpl statemachineService = new StatemachineServiceImpl();
        InputStream is = null;
        try {
            is = new FileInputStream(new File("C:/config.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        statemachineService.initFSM(is, "START");
        System.out.println(statemachineService.getCurrentState());
        statemachineService.processFSM("MOVELEFT");
        statemachineService.processFSM("START");
        System.out.println(statemachineService.getCurrentState());
        Map map = statemachineService.fsm.getCurrentFSMState().getNewTransitionMap();
        System.out.println(map);
    }
}


