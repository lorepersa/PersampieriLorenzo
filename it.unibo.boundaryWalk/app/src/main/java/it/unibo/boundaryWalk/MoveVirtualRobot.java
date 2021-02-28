/*
MoveVirtualRobot.java
===============================================================
Moves the virtual robot by using an HTTP client
===============================================================
*/
package it.unibo.boundaryWalk;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;

public class MoveVirtualRobot implements MoveVirtualRobotInterface{
    private static final String localHostName    = "localhost";
    private static final int port                = 8090;
    private static final String protocol         = "http";
    private static final String URL              = protocol+"://"+localHostName+":"+port+"/api/move";

    public MoveVirtualRobot() { }

    protected boolean sendCmd(String move, int time)  {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            System.out.println( move + " sendCmd "  );
            //String json         = "{\"robotmove\":\"" + move + "\"}";
            String json         = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
            StringEntity entity = new StringEntity(json);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httppost);
            //System.out.println( "MoveVirtualRobot | sendCmd response= " + response );
            boolean collision = checkCollision(response);
            return collision;
        } catch(Exception e){
            System.out.println("ERROR:" + e.getMessage());
            return true;
        }
    }

    protected boolean checkCollision(CloseableHttpResponse response) throws Exception {
        try{
            //response.getEntity().getContent() is an InputStream
            String jsonStr = EntityUtils.toString( response.getEntity() );
            System.out.println( "MoveVirtualRobot | checkCollision_simple jsonStr= " +  jsonStr );
            //jsonStr = {"endmove":true,"move":"moveForward"}
            JSONObject jsonObj = new JSONObject(jsonStr) ;
            boolean collision = false;
            if( jsonObj.get("endmove") != null ) {
                collision = ! jsonObj.get("endmove").toString().equals("true");
                System.out.println("MoveVirtualRobot | checkCollision_simple collision=" + collision);
            }
            return collision;
        }catch(Exception e){
            System.out.println("MoveVirtualRobot | checkCollision_simple ERROR:" + e.getMessage());
            throw(e);
        }
    }

    @Override
    public void openConnection() {
        // nothing to do
    }

    @Override
    public void closeConnection() {
        // nothing to do
    }

    public boolean moveForward(int duration)  { return sendCmd(MOVE_FORWARD, duration);  }
    public boolean moveBackward(int duration) { return sendCmd(MOVE_BACKWARD, duration); }
    public boolean moveLeft(int duration)     { return sendCmd(TURN_LEFT, duration);     }
    public boolean moveRight(int duration)    { return sendCmd(TURN_RIGHT, duration);    }
    public boolean moveStop(int duration)     { return sendCmd(ALARM, duration);        }
    /*
    MAIN
     */
    public static void main(String[] args)   {
        MoveVirtualRobot appl = new MoveVirtualRobot();
        boolean moveFailed = appl.moveLeft(300);
        System.out.println( "MoveVirtualRobot | moveLeft  failed= " + moveFailed);
        moveFailed = appl.moveRight(1300);
        System.out.println( "MoveVirtualRobot | moveRight failed= " + moveFailed);
    }

}