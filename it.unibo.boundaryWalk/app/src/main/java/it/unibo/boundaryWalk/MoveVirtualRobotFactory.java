package it.unibo.boundaryWalk;

public class MoveVirtualRobotFactory {

    public static MoveVirtualRobotInterface getMoveVirtualRobotWS() {
        return new MoveVirtualRobotWS();
    }

    public static MoveVirtualRobotInterface getMoveVirtualRobotHTTP() {
        return new MoveVirtualRobot();
    }
}
