/*
MoveVirtualRobotWS.java
===============================================================
Moves the virtual robot by using a WebSocket client
===============================================================
*/

package it.unibo.boundaryWalk;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class MoveVirtualRobotWS extends WebSocketClient implements MoveVirtualRobotInterface {

    private static final String localHostName    = "localhost";
    private static final int port                = 8091;
    private static final String protocol         = "ws";
    private static final String URL              = protocol+"://"+localHostName+":"+port+"/api/move";

    private boolean lastCollision = false;
    private Object lock = new Object();

    public MoveVirtualRobotWS() {
        super(URI.create(URL));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket");

        synchronized (lock) {
            lock.notify();
        }
    }

    public void openConnection() {
        super.connect();

        synchronized (lock) {
            try {
                lock.wait();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            super.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String message) {
        JSONObject jsonObj = new JSONObject(message) ;
        try {
            System.out.println("JSON RESPONSE: " + jsonObj.toString());
            if (jsonObj.get("endmove") != null) {
                boolean collision = !jsonObj.get("endmove").toString().equals("true");
                System.out.println("MoveVirtualRobotWS | collision=" + collision);
                lastCollision = collision;

                synchronized (lock) {
                    lock.notify();
                }

            }
        } catch(JSONException e) {

        }
    }

    private boolean sendCmd(String move, int time) {
        if(this.isClosed()) {
            return false;
        }

        String json = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
        this.send(json);

        synchronized (lock) {
            try {
                lock.wait();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return lastCollision;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public boolean moveForward(int duration)  { return sendCmd(MOVE_FORWARD, duration);  }
    public boolean moveBackward(int duration) { return sendCmd(MOVE_BACKWARD, duration); }
    public boolean moveLeft(int duration)     { return sendCmd(TURN_LEFT, duration);     }
    public boolean moveRight(int duration)    { return sendCmd(TURN_RIGHT, duration);    }
    public boolean moveStop(int duration)     { return sendCmd(ALARM, duration);        }

}