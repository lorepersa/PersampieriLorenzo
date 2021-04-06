package it.unibo.cautiousExplorerWithActor;

import it.unibo.supports2021.ActorBasicJava;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ControllerActor extends ActorBasicJava {

    private boolean pathSent = false;
    private boolean mustTerminate = false;


    public ControllerActor(String name) {
        super(name);
    }

    @Override
    protected void handleInput(String s) {
        if (s == null) {
            return;
        }

        System.out.println("received msg " + s);

        JSONObject json = new JSONObject(s);

        if (json.has("action")) {
            if (json.getString("action").equals("doBasicMoves")) {
                doBasicMoves();
            }
            else if (json.getString("action").equals("doBuildMap"))
                doBuildMap();
        } else if (json.has("endpath")){
            System.out.println("ControllerActor | endpath | " + json.getString("endpath"));

            if (mustTerminate) {
                JSONObject terJson = new JSONObject();
                terJson.put("stop", "true");
                updateObservers(terJson.toString());
                this.terminate();
            }
        } else if (json.has("stop")) {
            mustTerminate = true;
            if (!pathSent) {
                updateObservers(json.toString());
                this.terminate();
            }
        }
    }


    private void doBuildMap() {

    }

    private void doBasicMoves() {
        if (pathSent || mustTerminate)
            return;

        String path = "wwwwwwwwwwwlwwwwwwwwwwwlwwwwwwwwwwlwwwwwwwwwwwl";
        JSONObject json = new JSONObject();
        json.put("path", path);
        updateObservers(json.toString());
        pathSent = true;
    }
}
