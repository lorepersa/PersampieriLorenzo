package it.unibo.boundaryWalk;

public interface MoveVirtualRobotInterface {

    String MOVE_FORWARD = "moveForward";
    String MOVE_BACKWARD = "moveBackward";
    String TURN_LEFT = "turnLeft";
    String TURN_RIGHT = "turnRight";
    String ALARM = "alarm";

    void openConnection();
    void closeConnection();
    boolean moveForward(int duration);
    boolean moveBackward(int duration);
    boolean moveLeft(int duration);
    boolean moveRight(int duration);
    boolean moveStop(int duration);
}
