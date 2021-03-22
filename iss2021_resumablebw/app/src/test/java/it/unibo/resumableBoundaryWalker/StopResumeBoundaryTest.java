package it.unibo.resumableBoundaryWalker;

import it.unibo.resumableBoundaryWalker.supports.IssCommSupport;
import it.unibo.resumableBoundaryWalker.supports.RobotApplicationStarter;
import it.unibo.resumableBoundaryWalker.wenv.RobotComm;
import it.unibo.resumableBoundaryWalker.wenv.RobotInputController;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopResumeBoundaryTest {

    private RobotInputController controller;

    @Before public void setup() {
        RobotComm rc = (RobotComm) RobotApplicationStarter.createInstance(RobotComm.class);
        assertNotNull(rc);
        IssCommSupport support = (IssCommSupport) rc.getIssOperations();
        boolean usearil = RobotComm.useArilUtility(rc);
        controller = new RobotInputController(support, usearil, false);
        support.registerObserver(controller);

        if(controller == null) {
            System.out.println("ERROR");
            return;
        }
    }

    @Test public void testStopResumeBoundary() {

        Runnable test = () -> {
            String moves = controller.doBoundary();
            System.out.println("MOVES: " + moves);
            assertNotNull(moves);
            assertTrue(moves.matches("(w+l){4}"));
        };

        Thread threadTest = new Thread(test);
        threadTest.start();

        JSONObject json = new JSONObject();

        try {
            Thread.sleep(50);
            json.put("robotcmd", "RESUME");
            System.out.println("START");
            controller.handleInfo(json);
            while(threadTest.isAlive()) {
                Thread.sleep(100);
                json.remove("robotcmd");
                json.put("robotcmd", "STOP");
                System.out.println("STOP");
                controller.handleInfo(json);
                Thread.sleep(2000);
                json.remove("robotcmd");
                json.put("robotcmd", "RESUME");
                System.out.println("RESUME");
                controller.handleInfo(json);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @After
    public void teardown() {

    }
}
