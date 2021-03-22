package it.unibo.resumableBoundaryWalker.wenv;

import it.unibo.resumableBoundaryWalker.annotations.ArilRobotSpec;
import it.unibo.resumableBoundaryWalker.consolegui.ConsoleGui;
import it.unibo.resumableBoundaryWalker.supports.IssCommSupport;
import it.unibo.resumableBoundaryWalker.supports.RobotApplicationStarter;

import java.lang.annotation.Annotation;

public class ResumableBoundaryWalkerMain {

    public static void main(String [] args) {
        RobotComm rc = (RobotComm) RobotApplicationStarter.createInstance(RobotComm.class);
        assert rc != null;
        IssCommSupport support = (IssCommSupport) rc.getIssOperations();
        boolean usearil = RobotComm.useArilUtility(rc);
        RobotInputController controller = new RobotInputController(support, usearil, true);
        support.registerObserver(controller);

        if(controller == null) {
            System.out.println("ERROR");
            return;
        }

        ConsoleGui consoleGui = new ConsoleGui( controller );

        String map = controller.doBoundary();

        System.out.println("");
        System.out.println("--------------------------");
        System.out.println("MAIN: BOUNDARY END");
        System.out.println("");
        System.out.println(map);
        System.out.println("");
        System.out.println("--------------------------");

        System.exit(0); // task done
    }
}
