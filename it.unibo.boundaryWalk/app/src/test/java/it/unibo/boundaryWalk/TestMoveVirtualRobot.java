package it.unibo.boundaryWalk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMoveVirtualRobot {

    private MoveVirtualRobotInterface appl;
    private MoveVirtualRobotInterface temp;

    @Before
    public void systemSetUp() {
        System.out.println("TestMoveVirtualRobot | setUp: robot should be at HOME-DOWN ");
        temp = MoveVirtualRobotFactory.getMoveVirtualRobotWS(); // WEBSOCKET
        appl = MoveVirtualRobotFactory.getMoveVirtualRobotHTTP(); // HTTP
        appl.openConnection();
    }

    @After
    public void  terminate() {
        appl.closeConnection();
        temp.closeConnection();
        System.out.println("%%%  TestMoveVirtualRobot |  terminates ");
    }

    private String walkAlongBoundaryString() {
        StringBuilder moves = new StringBuilder();
        boolean collision;
        for(int i = 0; i < 4; i++) {
            collision = false;

            while(!collision) {
                collision = appl.moveForward(300);
                moves.append("w");
            }

            collision = appl.moveLeft(300);
            if(!collision) {
                moves.append("l");
            }
        }

        System.out.println("Moves: " + moves);
        return moves.toString();
    }

    @Test
    public void walkAlongBoundary() {

        // ----- TEST PLAN 1 -----

        System.out.println("TEST walkAlongBoundary");
        String moves;
        moves = walkAlongBoundaryString();
        assertTrue(moves.matches("w*lw*lw*lw*l"));
    }
}
