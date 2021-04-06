package it.unibo.cautiousExplorerWithActor;

import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class CautiousExplorerActor extends ActorBasicJava {

    private static final String haltSonarMsg = "{\"robotmove\":\"alarm\", \"time\": 2000}";
    final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private int currentIndexMove = 0;
    private boolean sonarHandled = false;
    private String currentPath = null;
    private StringBuilder pathBuilder = new StringBuilder();
    private boolean returnToDenMode = false;

    private IssWsHttpJavaSupport support;
    public CautiousExplorerActor(String name, IssWsHttpJavaSupport support) {
        super(name);
        this.support = support;
    }

    @Override
    protected void handleInput(String s) {
        System.out.println("CautiousExplorerActor | " + s);
        JSONObject json = new JSONObject(s);
        msgDriven(json);
    }

    private void msgDriven( JSONObject infoJson) {
        if (infoJson.has("endmove")) doOperation(infoJson.getString("move"), infoJson.getString("endmove"));
        else if (infoJson.has("sonarName")) handleSonar(infoJson);
        else if (infoJson.has("collision")) handleCollision(infoJson);
        else if (infoJson.has("path")) handleNewPath(infoJson);
    }

    private void handleNewPath(JSONObject pathinfo) {
        if (currentPath == null) {
            currentPath = pathinfo.getString("path");
            pathBuilder = new StringBuilder();
            currentIndexMove = 0;
            sonarHandled = false;
            returnToDenMode = false;
            doNextMove();
        }
    }

    private void doOperation(String move, String endmove) {

        switch ( endmove ) {
            case "true":
                if (!returnToDenMode)
                    doNextMove();
                else
                    doReturnToDen();

                break;

            case "false":
                System.out.println("CautiousExplorerActor | collision detected | return to den");
                startReturnToDen();
                break;
        }

    }

    private void convertAndForward(char move) {
        if (move == 'w') {
            if ( !returnToDenMode)
                support.forward(forwardMsg);
            else
                support.forward(backwardMsg);
        } else if (move == 's') {
            if (!returnToDenMode)
                support.forward(backwardMsg);
            else
                support.forward(forwardMsg);
        } else if (move == 'l') {
            if (!returnToDenMode)
                support.forward(turnLeftMsg);
            else
                support.forward(turnRightMsg);
        } else if (move == 'r') {
            if (!returnToDenMode)
                support.forward(turnRightMsg);
            else
                support.forward(turnLeftMsg);
        } else if (move == 'h') {
            support.forward(haltMsg);
        }
    }

    private void sendEndPathToObservers() {
        JSONObject json = new JSONObject();
        json.put("endpath", pathBuilder.toString());
        this.updateObservers(json.toString());
    }

    private void doNextMove() {
        if (currentPath.length() > currentIndexMove) {
            char move = currentPath.charAt(currentIndexMove);
            convertAndForward(move);
            currentIndexMove += 1;
            pathBuilder.append(move);
        } else {
            // reached the end of the path
            sendEndPathToObservers();
        }
    }

    private void startReturnToDen() {
        if (currentPath.length() > currentIndexMove) {
            // reached the end of the path
            this.updateObservers(pathBuilder.toString());
        } else {
            returnToDenMode = true;
            currentIndexMove -= 1;
            doReturnToDen();
        }
    }


    private void doReturnToDen() {

        if (currentIndexMove >= 0) {
            char move = currentPath.charAt(currentIndexMove);
            convertAndForward(move);
            currentIndexMove -= 1;
        }  else {
            // reached the end of the path
            sendEndPathToObservers();
        }

    }


    private void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        System.out.println("CautiousExplorerActor | handleSonar:" + sonarname + " distance=" + distance);
        /*
        FIXME - rimane bloccato sul sonar
        if (!sonarHandled) {
            support.forward(haltSonarMsg);
            sonarHandled = true;
        }
         */
    }
    private void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        String move   = (String) collisioninfo.get("move");
        System.out.println("CautiousExplorerActor | collision | path = " + this.pathBuilder.toString());
    }

}
