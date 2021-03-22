package it.unibo.resumableBoundaryWalker.wenv;

import it.unibo.resumableBoundaryWalker.annotations.ArilRobotSpec;
import it.unibo.resumableBoundaryWalker.interaction.IssOperations;

import java.lang.annotation.Annotation;

@ArilRobotSpec
public class RobotComm {

    private IssOperations issOperations;

    public RobotComm(IssOperations issOperations) {
        this.issOperations = issOperations;
    }

    public IssOperations getIssOperations() {
        return this.issOperations;
    }

    public static boolean useArilUtility(RobotComm rc) {
        boolean usearil = false;

        try {
            Class<?> clazz = rc.getClass();
            System.out.println("useArilUtility class: " + clazz.getName());
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annot : annotations) {
                if (annot instanceof ArilRobotSpec) {
                    usearil = true;
                    break;
                }
            }//for

        }catch( Exception e){
            System.out.println("useArilUtility ERROR: " + e.getMessage()  );
        }

        return usearil;
    }
}
